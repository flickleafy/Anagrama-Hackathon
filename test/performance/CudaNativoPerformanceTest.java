/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package performance;

import controle.cuda.CudaCombinador;
import java.util.ArrayList;
import testes.TestSupport;

/**
 * Mede o caminho CUDA nativo quando explicitamente solicitado.
 *
 * <p>Requer biblioteca nativa JCuda, driver e GPU compatíveis. O alvo Ant
 * {@code test-performance-cuda} habilita o teste; a suíte rotineira não tenta
 * transformar a contingência em CPU em uma medição de GPU.</p>
 */
public final class CudaNativoPerformanceTest
{
    private static final int AQUECIMENTOS = 1;
    private static final int ITERACOES = 3;

    private CudaNativoPerformanceTest()
    {
    }

    public static boolean executarSeSolicitado()
    {
        if (!Boolean.getBoolean("anagrama.performance.cuda"))
        {
            System.out.println(
                    "CUDA nativo não solicitado; use ant test-performance-cuda");
            return false;
        }

        final CenarioDesempenho cenario = new CenarioDesempenho();
        final ArrayList<String> referencia = cenario.executarSingleThread();
        CenarioDesempenho.ResultadoCuda verificacao = cenario.executarCuda(true);
        TestSupport.verdadeiro(
                verificacao.combinador.getBackendUsado()
                        == CudaCombinador.Backend.CUDA,
                "O benchmark nativo exige CUDA, mas ocorreu contingência: "
                + verificacao.combinador.getDetalheFalhaCuda());
        TestSupport.igual(
                referencia,
                verificacao.combinacoes,
                "O kernel deve manter a correção antes da medição");

        long nanos = MedidorDesempenho.medirNanosPorOperacao(
                AQUECIMENTOS,
                ITERACOES,
                new MedidorDesempenho.Operacao()
                {
                    @Override
                    public int executar()
                    {
                        CenarioDesempenho.ResultadoCuda resultado =
                                cenario.executarCuda(true);
                        if (resultado.combinador.getBackendUsado()
                                != CudaCombinador.Backend.CUDA)
                        {
                            throw new AssertionError(
                                    "CUDA deixou de estar disponível durante a medição: "
                                    + resultado.combinador.getDetalheFalhaCuda());
                        }
                        return resultado.combinacoes.size();
                    }
                });
        TestSupport.verdadeiro(nanos > 0L, "A medição CUDA deve ser positiva");
        System.out.println(
                "CUDA nativo: " + nanos + " ns/operação; aquecimentos="
                + AQUECIMENTOS + ", iterações=" + ITERACOES);
        return true;
    }
}
