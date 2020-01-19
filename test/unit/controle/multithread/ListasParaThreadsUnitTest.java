/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link ListasParaThreads}. */
public final class ListasParaThreadsUnitTest
{
    private ListasParaThreadsUnitTest()
    {
    }

    public static void executar()
    {
        ArrayList<Integer> origem = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        ArrayList<ArrayList<Integer>> partes = ListasParaThreads.dividirLista(origem, 3);
        TestSupport.igual(Arrays.asList(1, 2, 3), partes.get(0), "A primeira parte deve receber o excedente");
        TestSupport.igual(Arrays.asList(4, 5), partes.get(1), "A segunda parte deve permanecer contígua");
        TestSupport.igual(Arrays.asList(6, 7), partes.get(2), "A terceira parte deve preservar a ordem");

        partes.get(0).clear();
        TestSupport.igual(7, origem.size(), "As partições devem ser cópias independentes");
        TestSupport.igual(
                Collections.emptyList(),
                ListasParaThreads.dividirLista(Collections.<Integer>emptyList(), 4),
                "Uma entrada vazia não deve criar partições");
        TestSupport.igual(
                1,
                ListasParaThreads.dividirLista(Arrays.asList(1, 2), 0).size(),
                "Uma quantidade não positiva deve usar uma partição útil");

        TestSupport.lanca(
                IllegalArgumentException.class,
                new TestSupport.Acao()
                {
                    @Override
                    public void executar()
                    {
                        ListasParaThreads.dividirLista(null, 2);
                    }
                },
                "Uma lista nula deve ser rejeitada");
    }
}
