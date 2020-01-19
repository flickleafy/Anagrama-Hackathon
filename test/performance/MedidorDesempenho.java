/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package performance;

/** Mede uma operação depois de aquecer o mesmo caminho de execução. */
final class MedidorDesempenho
{
    interface Operacao
    {
        int executar();
    }

    private static volatile long consumidor;

    private MedidorDesempenho()
    {
    }

    static long medirNanosPorOperacao(
            int aquecimentos,
            int iteracoes,
            Operacao operacao)
    {
        for (int i = 0; i < aquecimentos; i++)
        {
            consumidor += operacao.executar();
        }

        long inicio = System.nanoTime();
        for (int i = 0; i < iteracoes; i++)
        {
            consumidor += operacao.executar();
        }
        return (System.nanoTime() - inicio) / iteracoes;
    }
}
