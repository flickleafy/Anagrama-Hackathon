/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.cuda;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Localiza o PTX distribuído e compila o código-fonte CUDA somente como
 * contingência.
 */
final class CudaKernelCompiler
{
    private static final int VERSAO_PTX_MAXIMA_MAIOR = 6;
    private static final int VERSAO_PTX_MAXIMA_MENOR = 4;
    private static final int ARQUITETURA_CUDA_MAXIMA = 75;
    private static final Pattern DIRETIVA_VERSAO = Pattern.compile(
            "^\\.version\\s+([0-9]{1,3})\\.([0-9]{1,3})\\s*$");
    private static final Pattern DIRETIVA_ARQUITETURA = Pattern.compile(
            "^\\.target\\s+sm_([0-9]{1,3})(?:\\s*,.*)?$");
    private static final String RECURSO_PTX =
            "controle/cuda/kernels/CudaCombinadorKernel.ptx";
    private static final String RECURSO_KERNEL =
            "controle/cuda/kernels/CudaCombinadorKernel.cu";
    private static volatile String ptxEmCache;

    private CudaKernelCompiler()
    {
    }

    /**
     * Obtém um arquivo PTX acessível pelo driver CUDA.
     *
     * <p>A busca prioriza a árvore de trabalho e depois o recurso do classpath. O
     * {@code nvcc} só é executado quando o PTX não está disponível.</p>
     *
     * @return caminho do PTX existente ou criado em diretório temporário
     * @throws IOException se o recurso não puder ser extraído ou compilado
     */
    static synchronized String prepararPtx() throws IOException
    {
        if (ptxEmCache != null && Files.isRegularFile(Paths.get(ptxEmCache)))
        {
            validarCompatibilidadePtx(Paths.get(ptxEmCache));
            return ptxEmCache;
        }

        Path ptxExistente = localizarPtx();
        if (ptxExistente != null)
        {
            validarCompatibilidadePtx(ptxExistente);
            ptxEmCache = ptxExistente.toString();
            return ptxEmCache;
        }

        // Compilar é a última opção para que a distribuição normal não
        // dependa da presença do kit de desenvolvimento CUDA completo.
        Path fonte = localizarFonte();
        Path ptx = Files.createTempFile("anagrama-cuda-", ".ptx");
        ptx.toFile().deleteOnExit();

        String nvcc = System.getProperty("anagrama.nvcc", "nvcc");
        ProcessBuilder builder = new ProcessBuilder(
                nvcc,
                "-m64",
                "--ptx",
                "--gpu-architecture=compute_75",
                fonte.toString(),
                "-o",
                ptx.toString());
        builder.redirectErrorStream(true);

        Process processo = builder.start();
        String saida;
        // Consumir a saída antes de waitFor evita que o processo filho bloqueie
        // caso o buffer do pipe seja preenchido por mensagens do compilador.
        try (InputStream input = processo.getInputStream();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream())
        {
            byte[] buffer = new byte[4096];
            int lidos;
            while ((lidos = input.read(buffer)) != -1)
            {
                bytes.write(buffer, 0, lidos);
            }
            saida = new String(bytes.toByteArray(), StandardCharsets.UTF_8);
        }

        int codigo;
        try
        {
            codigo = processo.waitFor();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new IOException("A compilação CUDA foi interrompida", e);
        }

        if (codigo != 0)
        {
            throw new IOException(
                    "nvcc retornou " + codigo + ": " + saida.trim());
        }

        // O PTX compilado precisa obedecer ao limite aceito pelo driver
        // CUDA 10.1 antes de ser armazenado em cache.
        validarCompatibilidadePtx(ptx);
        ptxEmCache = ptx.toString();
        return ptxEmCache;
    }

    /**
     * Verifica se as diretivas do módulo respeitam o teto do CUDA 10.1.
     *
     * <p>A validação ocorre antes de entregar o caminho ao driver. Um PTX com
     * diretivas acima dos limites configurados produz uma falha explícita, em
     * vez de parecer apenas indisponibilidade da GPU.</p>
     *
     * @param ptx módulo que será carregado pelo driver
     * @throws IOException se as diretivas forem ausentes, inválidas ou
     *         superiores aos limites PTX 6.4 e {@code sm_75}
     */
    static void validarCompatibilidadePtx(Path ptx) throws IOException
    {
        int versaoMaior = -1;
        int versaoMenor = -1;
        int arquitetura = -1;

        try (BufferedReader leitor = Files.newBufferedReader(
                ptx, StandardCharsets.UTF_8))
        {
            String linha;
            while ((linha = leitor.readLine()) != null)
            {
                String diretiva = linha.trim();
                if (diretiva.startsWith(".version"))
                {
                    Matcher versao = DIRETIVA_VERSAO.matcher(diretiva);
                    if (!versao.matches())
                    {
                        throw new IOException(
                                "Diretiva .version inválida no PTX: " + diretiva);
                    }
                    versaoMaior = Integer.parseInt(versao.group(1));
                    versaoMenor = Integer.parseInt(versao.group(2));
                }
                else if (diretiva.startsWith(".target"))
                {
                    Matcher alvo = DIRETIVA_ARQUITETURA.matcher(diretiva);
                    if (!alvo.matches())
                    {
                        throw new IOException(
                                "Diretiva .target inválida no PTX: " + diretiva);
                    }
                    arquitetura = Integer.parseInt(alvo.group(1));
                }
            }
        }

        if (versaoMaior < 0 || versaoMenor < 0)
        {
            throw new IOException("O PTX não declara a diretiva .version");
        }
        if (arquitetura < 0)
        {
            throw new IOException("O PTX não declara a diretiva .target");
        }
        if (versaoMaior > VERSAO_PTX_MAXIMA_MAIOR
                || (versaoMaior == VERSAO_PTX_MAXIMA_MAIOR
                && versaoMenor > VERSAO_PTX_MAXIMA_MENOR))
        {
            throw new IOException(
                    "PTX " + versaoMaior + "." + versaoMenor
                    + " excede o limite 6.4 do CUDA 10.1");
        }
        if (arquitetura > ARQUITETURA_CUDA_MAXIMA)
        {
            throw new IOException(
                    "Arquitetura sm_" + arquitetura
                    + " excede o limite sm_75 do CUDA 10.1");
        }
    }

    /**
     * Localiza o PTX na árvore de trabalho ou o extrai para um arquivo temporário.
     */
    private static Path localizarPtx() throws IOException
    {
        Path checkout = Paths.get(
                "src", "controle", "cuda", "kernels", "CudaCombinadorKernel.ptx");
        if (Files.isRegularFile(checkout))
        {
            return checkout.toAbsolutePath();
        }

        InputStream recurso = CudaKernelCompiler.class.getClassLoader()
                .getResourceAsStream(RECURSO_PTX);
        if (recurso == null)
        {
            return null;
        }

        Path temporario = Files.createTempFile("anagrama-cuda-", ".ptx");
        temporario.toFile().deleteOnExit();
        try (InputStream input = recurso)
        {
            Files.copy(input, temporario, StandardCopyOption.REPLACE_EXISTING);
        }
        return temporario;
    }

    /**
     * Localiza o fonte CUDA apenas para o caminho excepcional de compilação.
     */
    private static Path localizarFonte() throws IOException
    {
        Path checkout = Paths.get(
                "src", "controle", "cuda", "kernels", "CudaCombinadorKernel.cu");
        if (Files.isRegularFile(checkout))
        {
            return checkout.toAbsolutePath();
        }

        InputStream recurso = CudaKernelCompiler.class.getClassLoader()
                .getResourceAsStream(RECURSO_KERNEL);
        if (recurso == null)
        {
            throw new IOException("Kernel CUDA não encontrado: " + RECURSO_KERNEL);
        }

        Path temporario = Files.createTempFile("anagrama-cuda-", ".cu");
        temporario.toFile().deleteOnExit();
        try (InputStream input = recurso)
        {
            Files.copy(input, temporario, StandardCopyOption.REPLACE_EXISTING);
        }
        return temporario;
    }
}
