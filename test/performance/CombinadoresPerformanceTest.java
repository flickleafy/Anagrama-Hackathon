/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package performance;

import java.util.ArrayList;
import java.util.List;
import testes.TestSupport;

/**
 * Compara os combinadores sob a mesma carga determinística em memória.
 *
 * <p>A carga possui 15 palavras, três comprimentos e alvo de seis letras. Cada
 * medição usa três aquecimentos e dez iterações; a métrica informada é a
 * média de nanossegundos por operação. Não há limite de tempo eliminatório,
 * pois máquinas compartilhadas introduzem variação excessiva.</p>
 */
public final class CombinadoresPerformanceTest
{
    private static final int AQUECIMENTOS = 3;
    private static final int ITERACOES = 10;

    private CombinadoresPerformanceTest()
    {
    }

    public static void executar()
    {
        final CenarioDesempenho cenario = new CenarioDesempenho();
        ArrayList<String> referencia = cenario.executarSingleThread();
        List<String> referenciaOrdenada = CenarioDesempenho.ordenar(referencia);

        TestSupport.igual(
                referencia,
                cenario.executarMultiThread(),
                "A medição multithread deve usar uma saída equivalente");
        TestSupport.igual(
                referenciaOrdenada,
                CenarioDesempenho.ordenar(cenario.executarArvore()),
                "A medição da trie deve usar o mesmo multiconjunto");
        TestSupport.igual(
                referencia,
                cenario.executarCuda(false).combinacoes,
                "A medição CUDA em CPU deve usar uma saída equivalente");

        long single = medir(new MedidorDesempenho.Operacao()
        {
            @Override
            public int executar()
            {
                return cenario.executarSingleThread().size();
            }
        });
        long multi = medir(new MedidorDesempenho.Operacao()
        {
            @Override
            public int executar()
            {
                return cenario.executarMultiThread().size();
            }
        });
        long arvore = medir(new MedidorDesempenho.Operacao()
        {
            @Override
            public int executar()
            {
                return cenario.executarArvore().size();
            }
        });
        long cudaCpu = medir(new MedidorDesempenho.Operacao()
        {
            @Override
            public int executar()
            {
                return cenario.executarCuda(false).combinacoes.size();
            }
        });

        TestSupport.verdadeiro(
                single > 0L && multi > 0L && arvore > 0L && cudaCpu > 0L,
                "As medições devem produzir durações positivas");
        System.out.println(
                "Carga: 15 palavras, alvo 6, " + referencia.size()
                + " combinações; aquecimentos=" + AQUECIMENTOS
                + ", iterações=" + ITERACOES);
        imprimir("single-thread", single);
        imprimir("multithread", multi);
        imprimir("trie", arvore);
        imprimir("CUDA em CPU", cudaCpu);
    }

    private static long medir(MedidorDesempenho.Operacao operacao)
    {
        return MedidorDesempenho.medirNanosPorOperacao(
                AQUECIMENTOS, ITERACOES, operacao);
    }

    private static void imprimir(String nome, long nanos)
    {
        System.out.println(nome + ": " + nanos + " ns/operação");
    }
}
