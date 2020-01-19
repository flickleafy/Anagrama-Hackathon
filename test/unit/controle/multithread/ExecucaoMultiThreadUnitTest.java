/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Callable;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link ExecucaoMultiThread}. */
public final class ExecucaoMultiThreadUnitTest
{
    private ExecucaoMultiThreadUnitTest()
    {
    }

    public static void executar()
    {
        TestSupport.igual(0, ExecucaoMultiThread.definirNumeroDeThreads(0), "Carga vazia não deve criar threads");
        TestSupport.igual(0, ExecucaoMultiThread.definirNumeroDeThreads(-1), "Carga negativa não deve criar threads");
        int quantidade = ExecucaoMultiThread.definirNumeroDeThreads(3);
        TestSupport.verdadeiro(quantidade >= 1 && quantidade <= 3, "A quantidade deve respeitar carga e processadores");

        TestSupport.igual(
                Collections.emptyList(),
                ExecucaoMultiThread.executar(Collections.<Callable<String>>emptyList()),
                "Nenhuma tarefa deve produzir uma lista vazia");

        ArrayList<Callable<String>> tarefas = new ArrayList<>();
        tarefas.add(new Callable<String>()
        {
            @Override
            public String call()
            {
                return "primeiro";
            }
        });
        tarefas.add(new Callable<String>()
        {
            @Override
            public String call()
            {
                return "segundo";
            }
        });
        TestSupport.igual(
                Arrays.asList("primeiro", "segundo"),
                ExecucaoMultiThread.executar(tarefas),
                "Os resultados devem seguir a ordem das tarefas");

        final ArrayList<Callable<String>> tarefaFalha = new ArrayList<>();
        tarefaFalha.add(new Callable<String>()
        {
            @Override
            public String call()
            {
                throw new IllegalArgumentException("falha controlada");
            }
        });
        TestSupport.lanca(
                IllegalArgumentException.class,
                new TestSupport.Acao()
                {
                    @Override
                    public void executar()
                    {
                        ExecucaoMultiThread.executar(tarefaFalha);
                    }
                },
                "Uma exceção da tarefa deve ser propagada");
    }
}
