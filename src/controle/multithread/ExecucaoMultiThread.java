/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Executa de forma limitada as tarefas dos estágios paralelos.
 *
 * @author Enzo Erbano
 */
public final class ExecucaoMultiThread
{
    private ExecucaoMultiThread()
    {
    }

    /**
     * Executa tarefas e devolve os resultados na ordem em que foram recebidas.
     *
     * <p>Uma interrupção restaura o estado da thread chamadora. Falhas das
     * tarefas são propagadas para impedir resultados parciais silenciosos.</p>
     *
     * @param tarefas trabalhos independentes que serão executados
     * @param <T> tipo produzido por cada tarefa
     * @return resultados na mesma ordem da lista de tarefas
     */
    static <T> ArrayList<T> executar(List<? extends Callable<T>> tarefas)
    {
        ArrayList<T> resultados = new ArrayList<>();
        if (tarefas.isEmpty())
        {
            return resultados;
        }

        int quantidadeThreads = definirNumeroDeThreads(tarefas.size());
        ExecutorService executor = Executors.newFixedThreadPool(quantidadeThreads);
        try
        {
            // invokeAll preserva a ordem dos Future, mesmo que as tarefas
            // terminem fora de ordem, mantendo o resultado determinístico.
            List<Future<T>> futuros = executor.invokeAll(tarefas);
            for (Future<T> futuro : futuros)
            {
                resultados.add(futuro.get());
            }
            return resultados;
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("A execução paralela foi interrompida", e);
        }
        catch (ExecutionException e)
        {
            Throwable causa = e.getCause();
            if (causa instanceof RuntimeException)
            {
                throw (RuntimeException) causa;
            }
            throw new IllegalStateException("Falha em uma tarefa paralela", causa);
        }
        finally
        {
            // Em caminhos de falha, interrompe qualquer tarefa remanescente;
            // no caminho normal, todos os Future já foram concluídos.
            executor.shutdownNow();
        }
    }

    /**
     * Calcula o número máximo de threads úteis para uma carga.
     *
     * @param tamanhoLista quantidade de unidades independentes de trabalho
     * @return zero para carga vazia; caso contrário, o menor valor entre a
     *         carga e os processadores disponíveis
     */
    public static int definirNumeroDeThreads(int tamanhoLista)
    {
        if (tamanhoLista <= 0)
        {
            return 0;
        }
        return Math.min(
                Runtime.getRuntime().availableProcessors(),
                tamanhoLista);
    }
}
