/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link Combinador}. */
public final class CombinadorUnitTest
{
    private CombinadorUnitTest()
    {
    }

    public static void executar()
    {
        ArrayList<ArrayList<String>> colecoes = new ArrayList<>();
        colecoes.add(new ArrayList<>(Arrays.asList("A", "B")));
        colecoes.add(new ArrayList<>(Arrays.asList("AB", "BA")));

        ArrayList<ArrayList<Integer>> planos = new ArrayList<>();
        planos.add(new ArrayList<>(Arrays.asList(1, 1)));
        planos.add(new ArrayList<>(Arrays.asList(2)));
        HashMap<Integer, Integer> mapa = new HashMap<>();
        mapa.put(1, 0);
        mapa.put(2, 1);

        ArrayList<String> resultado = new ArrayList<>(Arrays.asList("INICIAL"));
        Combinador.combinarPlanos(resultado, colecoes, planos, mapa);
        TestSupport.igual(
                Arrays.asList("INICIAL", "AA", "AB", "BB", "AB", "BA"),
                resultado,
                "O combinador deve acrescentar resultados canônicos ao destino");

        SetCombinacoes conjunto = new SetCombinacoes(2, colecoes);
        ArrayList<ArrayList<Integer>> antes = conjunto.getListaDeSetCombinacoes();
        ArrayList<String> primeira = new ArrayList<>();
        ArrayList<String> segunda = new ArrayList<>();
        Combinador.combinadorPalavras(primeira, colecoes, "AB", conjunto);
        Combinador.combinadorPalavras(segunda, colecoes, "AB", conjunto);
        TestSupport.igual(primeira, segunda, "Chamadas consecutivas devem ser reentrantes");
        TestSupport.igual(antes, conjunto.getListaDeSetCombinacoes(), "Os planos não podem ser consumidos");
    }
}
