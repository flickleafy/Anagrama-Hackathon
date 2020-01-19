/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.cuda;

import controle.singlethread.SetCombinacoes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;

import static jcuda.driver.JCudaDriver.cuCtxCreate;
import static jcuda.driver.JCudaDriver.cuCtxDestroy;
import static jcuda.driver.JCudaDriver.cuCtxSynchronize;
import static jcuda.driver.JCudaDriver.cuDeviceGet;
import static jcuda.driver.JCudaDriver.cuInit;
import static jcuda.driver.JCudaDriver.cuLaunchKernel;
import static jcuda.driver.JCudaDriver.cuMemAlloc;
import static jcuda.driver.JCudaDriver.cuMemFree;
import static jcuda.driver.JCudaDriver.cuMemcpyDtoH;
import static jcuda.driver.JCudaDriver.cuMemcpyHtoD;
import static jcuda.driver.JCudaDriver.cuModuleGetFunction;
import static jcuda.driver.JCudaDriver.cuModuleLoad;
import static jcuda.driver.JCudaDriver.cuModuleUnload;

/**
 * Implementa em CUDA a etapa numérica de combinação de palavras.
 *
 * <p>Se o dispositivo, o driver, o PTX ou a biblioteca nativa JCuda não
 * estiver disponível, a mesma enumeração em bases mistas é executada na CPU.</p>
 *
 * @author Enzo Erbano
 */
public final class CudaCombinador
{
    /** Identifica onde a combinação foi efetivamente executada. */
    public enum Backend
    {
        /** Kernel executado por um dispositivo CUDA. */
        CUDA,
        /** Implementação numérica de contingência executada pela JVM. */
        CPU
    }

    // 256 threads é um tamanho de bloco portável para as GPUs suportadas.
    private static final int TAMANHO_BLOCO = 256;
    // Limita a alocação temporária da saída, independentemente do tamanho
    // total do produto cartesiano.
    private static final int TAMANHO_LOTE = 65_536;

    private Backend backendUsado = Backend.CPU;
    private String detalheFalhaCuda;

    /**
     * Tenta executar em CUDA e usa a CPU automaticamente em caso de falha.
     *
     * @param listaPalavrasCombinadas destino que receberá os resultados
     * @param colecoesPalavras palavras agrupadas por comprimento
     * @param setCombinacoes planos que descrevem as combinações
     */
    public CudaCombinador(
            ArrayList<String> listaPalavrasCombinadas,
            ArrayList<ArrayList<String>> colecoesPalavras,
            SetCombinacoes setCombinacoes)
    {
        this(listaPalavrasCombinadas, colecoesPalavras, setCombinacoes, true);
    }

    /**
     * Executa a alternativa numérica no mecanismo solicitado.
     *
     * <p>Os resultados são acrescentados ao destino somente depois que um
     * mecanismo conclui a enumeração inteira.</p>
     *
     * @param listaPalavrasCombinadas destino que será acrescido
     * @param colecoesPalavras palavras agrupadas por comprimento
     * @param setCombinacoes planos que descrevem as combinações
     * @param tentarCuda {@code true} para tentar CUDA antes da contingência;
     *        {@code false} para forçar CPU
     */
    public CudaCombinador(
            ArrayList<String> listaPalavrasCombinadas,
            ArrayList<ArrayList<String>> colecoesPalavras,
            SetCombinacoes setCombinacoes,
            boolean tentarCuda)
    {
        WordToVector vetores = new WordToVector(colecoesPalavras);
        if (tentarCuda)
        {
            try
            {
                ArrayList<String> resultadoCuda = combinarComCuda(
                        vetores, setCombinacoes);
                listaPalavrasCombinadas.addAll(resultadoCuda);
                backendUsado = Backend.CUDA;
                return;
            }
            catch (IOException | RuntimeException | LinkageError e)
            {
                // O resultado CUDA permanece local até a conclusão. Assim, o
                // caminho em CPU pode reiniciar sem duplicar uma saída parcial.
                detalheFalhaCuda = e.getClass().getSimpleName()
                        + (e.getMessage() == null ? "" : ": " + e.getMessage());
            }
        }

        listaPalavrasCombinadas.addAll(
                combinarNaCpu(vetores, setCombinacoes));
        backendUsado = Backend.CPU;
    }

    /**
     * Informa o mecanismo que concluiu a combinação.
     *
     * @return {@link Backend#CUDA} ou {@link Backend#CPU}
     */
    public Backend getBackendUsado()
    {
        return backendUsado;
    }

    /**
     * Descreve a falha que acionou a contingência em CPU.
     *
     * @return tipo e mensagem da falha CUDA, ou {@code null} se não houve
     *         tentativa malsucedida
     */
    public String getDetalheFalhaCuda()
    {
        return detalheFalhaCuda;
    }

    /**
     * Inicializa o driver, carrega o módulo e executa todos os planos.
     *
     * <p>Contexto e módulo pertencem exclusivamente a esta chamada e sempre
     * passam pela limpeza, inclusive quando JCuda lança uma exceção.</p>
     */
    private ArrayList<String> combinarComCuda(
            WordToVector vetores, SetCombinacoes setCombinacoes)
            throws IOException
    {
        JCudaDriver.setExceptionsEnabled(true);
        String ptx = CudaKernelCompiler.prepararPtx();

        CUcontext context = new CUcontext();
        CUmodule module = new CUmodule();
        boolean contextoCriado = false;
        boolean moduloCarregado = false;

        try
        {
            cuInit(0);
            CUdevice device = new CUdevice();
            cuDeviceGet(device, 0);
            cuCtxCreate(context, 0, device);
            contextoCriado = true;

            cuModuleLoad(module, ptx);
            moduloCarregado = true;
            CUfunction function = new CUfunction();
            cuModuleGetFunction(function, module, "combinar_indices");

            return executarPlanosCuda(
                    function,
                    vetores,
                    setCombinacoes.getListaDeSetCombinacoes(),
                    setCombinacoes.getMapaListasPalavrasEmArray());
        }
        finally
        {
            // A ordem inversa respeita a dependência do módulo em relação ao
            // contexto e os sinalizadores evitam liberar recursos não criados.
            if (moduloCarregado)
            {
                tentarLiberarModulo(module);
            }
            if (contextoCriado)
            {
                tentarDestruirContexto(context);
            }
        }
    }

    /**
     * Enumera os produtos cartesianos em lotes de tamanho limitado.
     */
    private ArrayList<String> executarPlanosCuda(
            CUfunction function,
            WordToVector vetores,
            List<? extends List<Integer>> planos,
            HashMap<Integer, Integer> mapaColecoes)
    {
        ArrayList<String> combinacoes = new ArrayList<>();
        ArrayList<int[]> colecoesIds = vetores.getColecoesVetores();
        Map<Integer, String> palavrasPorId = vetores.getWordToNumberMapping();

        for (List<Integer> plano : planos)
        {
            int[][] idsPlano = obterIdsDoPlano(plano, colecoesIds, mapaColecoes);
            int[] tamanhos = obterTamanhos(idsPlano);
            long total = calcularProduto(tamanhos);
            if (total == 0L)
            {
                continue;
            }

            CUdeviceptr deviceTamanhos = new CUdeviceptr();
            boolean tamanhosAlocados = false;
            try
            {
                // Os tamanhos das dimensões não mudam entre lotes do mesmo
                // plano, portanto permanecem uma única vez na memória do dispositivo.
                cuMemAlloc(deviceTamanhos, (long) tamanhos.length * Sizeof.INT);
                tamanhosAlocados = true;
                cuMemcpyHtoD(
                        deviceTamanhos,
                        Pointer.to(tamanhos),
                        (long) tamanhos.length * Sizeof.INT);

                // Somente os índices do lote atual ocupam memória na GPU e na
                // JVM; o total pode ser maior que TAMANHO_LOTE.
                for (long inicio = 0; inicio < total; inicio += TAMANHO_LOTE)
                {
                    int quantidade = (int) Math.min(TAMANHO_LOTE, total - inicio);
                    int[] indices = new int[Math.multiplyExact(
                            quantidade, plano.size())];
                    executarLoteCuda(
                            function,
                            deviceTamanhos,
                            plano.size(),
                            inicio,
                            quantidade,
                            indices);
                    adicionarCombinacoes(
                            combinacoes,
                            plano,
                            idsPlano,
                            indices,
                            quantidade,
                            palavrasPorId);
                }
            }
            finally
            {
                if (tamanhosAlocados)
                {
                    tentarLiberarMemoria(deviceTamanhos);
                }
            }
        }
        return combinacoes;
    }

    /**
     * Executa um lote e copia para a JVM os índices decodificados pelo kernel.
     */
    private void executarLoteCuda(
            CUfunction function,
            CUdeviceptr deviceTamanhos,
            int dimensoes,
            long inicio,
            int quantidade,
            int[] indices)
    {
        CUdeviceptr deviceSaida = new CUdeviceptr();
        boolean saidaAlocada = false;
        long bytesSaida = (long) indices.length * Sizeof.INT;
        try
        {
            cuMemAlloc(deviceSaida, bytesSaida);
            saidaAlocada = true;

            Pointer parametros = Pointer.to(
                    Pointer.to(new long[]{inicio}),
                    Pointer.to(new int[]{quantidade}),
                    Pointer.to(new int[]{dimensoes}),
                    Pointer.to(deviceTamanhos),
                    Pointer.to(deviceSaida));
            int tamanhoGrade = (quantidade + TAMANHO_BLOCO - 1) / TAMANHO_BLOCO;
            cuLaunchKernel(
                    function,
                    tamanhoGrade, 1, 1,
                    TAMANHO_BLOCO, 1, 1,
                    0, null,
                    parametros, null);
            cuCtxSynchronize();
            cuMemcpyDtoH(Pointer.to(indices), deviceSaida, bytesSaida);
        }
        finally
        {
            if (saidaAlocada)
            {
                tentarLiberarMemoria(deviceSaida);
            }
        }
    }

    /**
     * Reproduz na CPU a mesma enumeração em bases mistas usada pelo kernel.
     */
    private ArrayList<String> combinarNaCpu(
            WordToVector vetores, SetCombinacoes setCombinacoes)
    {
        ArrayList<String> combinacoes = new ArrayList<>();
        ArrayList<int[]> colecoesIds = vetores.getColecoesVetores();
        Map<Integer, String> palavrasPorId = vetores.getWordToNumberMapping();
        HashMap<Integer, Integer> mapaColecoes =
                setCombinacoes.getMapaListasPalavrasEmArray();

        for (List<Integer> plano : setCombinacoes.getListaDeSetCombinacoes())
        {
            int[][] idsPlano = obterIdsDoPlano(plano, colecoesIds, mapaColecoes);
            int[] tamanhos = obterTamanhos(idsPlano);
            long total = calcularProduto(tamanhos);
            int[] indices = new int[plano.size()];

            for (long linear = 0; linear < total; linear++)
            {
                decodificarIndice(linear, tamanhos, indices, 0);
                if (indicesCanonicos(plano, indices, 0))
                {
                    combinacoes.add(
                            criarCombinacao(idsPlano, indices, 0, palavrasPorId));
                }
            }
        }
        return combinacoes;
    }

    private static int[][] obterIdsDoPlano(
            List<Integer> plano,
            ArrayList<int[]> colecoesIds,
            HashMap<Integer, Integer> mapaColecoes)
    {
        int[][] idsPlano = new int[plano.size()][];
        for (int i = 0; i < plano.size(); i++)
        {
            Integer indiceColecao = mapaColecoes.get(plano.get(i));
            if (indiceColecao == null)
            {
                throw new IllegalArgumentException(
                        "Não há coleção para palavras de tamanho " + plano.get(i));
            }
            idsPlano[i] = colecoesIds.get(indiceColecao);
        }
        return idsPlano;
    }

    private static int[] obterTamanhos(int[][] idsPlano)
    {
        int[] tamanhos = new int[idsPlano.length];
        for (int i = 0; i < idsPlano.length; i++)
        {
            tamanhos[i] = idsPlano[i].length;
        }
        return tamanhos;
    }

    /**
     * Calcula o total de combinações e rejeita estouro aritmético silencioso.
     */
    private static long calcularProduto(int[] tamanhos)
    {
        long produto = 1L;
        for (int tamanho : tamanhos)
        {
            if (tamanho == 0)
            {
                return 0L;
            }
            produto = Math.multiplyExact(produto, tamanho);
        }
        return produto;
    }

    private static void adicionarCombinacoes(
            ArrayList<String> destino,
            List<Integer> plano,
            int[][] idsPlano,
            int[] indicesLineares,
            int quantidade,
            Map<Integer, String> palavrasPorId)
    {
        int dimensoes = plano.size();
        for (int linha = 0; linha < quantidade; linha++)
        {
            int deslocamento = linha * dimensoes;
            if (indicesCanonicos(plano, indicesLineares, deslocamento))
            {
                destino.add(criarCombinacao(
                        idsPlano, indicesLineares, deslocamento, palavrasPorId));
            }
        }
    }

    /**
     * Aceita somente índices não decrescentes para comprimentos repetidos.
     *
     * <p>Essa regra preserva a reutilização de palavras, mas elimina
     * permutações equivalentes da mesma coleção.</p>
     */
    private static boolean indicesCanonicos(
            List<Integer> plano, int[] indices, int deslocamento)
    {
        for (int i = 1; i < plano.size(); i++)
        {
            if (plano.get(i).equals(plano.get(i - 1))
                    && indices[deslocamento + i] < indices[deslocamento + i - 1])
            {
                return false;
            }
        }
        return true;
    }

    private static String criarCombinacao(
            int[][] idsPlano,
            int[] indices,
            int deslocamento,
            Map<Integer, String> palavrasPorId)
    {
        StringBuilder combinacao = new StringBuilder();
        for (int i = 0; i < idsPlano.length; i++)
        {
            int id = idsPlano[i][indices[deslocamento + i]];
            combinacao.append(palavrasPorId.get(id));
        }
        return combinacao.toString();
    }

    /**
     * Decompõe um índice linear nas coordenadas de um produto cartesiano.
     */
    private static void decodificarIndice(
            long linear, int[] tamanhos, int[] destino, int deslocamento)
    {
        long restante = linear;
        for (int i = tamanhos.length - 1; i >= 0; i--)
        {
            destino[deslocamento + i] = (int) (restante % tamanhos[i]);
            restante /= tamanhos[i];
        }
    }

    private static void tentarLiberarMemoria(CUdeviceptr pointer)
    {
        try
        {
            cuMemFree(pointer);
        }
        catch (RuntimeException | LinkageError ignored)
        {
            // A limpeza é de melhor esforço: ao destruir o contexto, a
            // alocação é recuperada sem invalidar os dados já copiados.
        }
    }

    private static void tentarLiberarModulo(CUmodule module)
    {
        try
        {
            cuModuleUnload(module);
        }
        catch (RuntimeException | LinkageError ignored)
        {
            // O contexto ainda será destruído e liberará os recursos do
            // módulo; as combinações já materializadas permanecem válidas.
        }
    }

    private static void tentarDestruirContexto(CUcontext context)
    {
        try
        {
            cuCtxDestroy(context);
        }
        catch (RuntimeException | LinkageError ignored)
        {
            // Os resultados já estão na JVM e o driver recuperará o contexto
            // ao encerrar o processo; a saída válida não deve ser descartada.
        }
    }
}
