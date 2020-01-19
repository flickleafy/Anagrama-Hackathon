/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link MultiCombinador}. */
public final class MultiCombinadorUnitTest
{
    private MultiCombinadorUnitTest()
    {
    }

    public static void executar()
    {
        ArrayList<ArrayList<String>> colecoes = new ArrayList<>();
        colecoes.add(new ArrayList<>(Arrays.asList("A", "B")));
        ArrayList<ArrayList<Integer>> planos = new ArrayList<>();
        planos.add(new ArrayList<>(Arrays.asList(1, 1)));
        HashMap<Integer, Integer> mapa = new HashMap<>();
        mapa.put(1, 0);

        TestSupport.igual(
                Arrays.asList("AA", "AB", "BB"),
                new MultiCombinador(colecoes, planos, mapa).call(),
                "A tarefa deve combinar somente os planos de sua partição");
    }
}
