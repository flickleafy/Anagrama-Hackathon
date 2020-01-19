/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link CombinacaoPlanos}. */
public final class CombinacaoPlanosUnitTest
{
    private CombinacaoPlanosUnitTest()
    {
    }

    public static void executar()
    {
        List<List<Integer>> planos = new ArrayList<>();
        planos.add(Arrays.asList(1, 1));
        planos.add(Collections.singletonList(2));
        planos.add(Collections.<Integer>emptyList());
        planos.add(Collections.singletonList(3));

        List<List<String>> colecoes = new ArrayList<>();
        colecoes.add(Arrays.asList("A", "B"));
        colecoes.add(Arrays.asList("AB", "BA"));

        HashMap<Integer, Integer> mapa = new HashMap<>();
        mapa.put(1, 0);
        mapa.put(2, 1);

        final ArrayList<String> selecoes = new ArrayList<>();
        final ArrayList<int[]> vetoresRecebidos = new ArrayList<>();
        CombinacaoPlanos.visitar(
                planos,
                colecoes,
                mapa,
                new CombinacaoPlanos.Visitante()
                {
                    @Override
                    public void aceitar(List<Integer> plano, int[] indicesPalavras)
                    {
                        selecoes.add(plano + ":" + Arrays.toString(indicesPalavras));
                        vetoresRecebidos.add(indicesPalavras);
                    }
                });

        TestSupport.igual(
                Arrays.asList(
                        "[1, 1]:[0, 0]",
                        "[1, 1]:[0, 1]",
                        "[1, 1]:[1, 1]",
                        "[2]:[0]",
                        "[2]:[1]"),
                selecoes,
                "O percurso deve ser canônico e ignorar planos sem coleção");

        vetoresRecebidos.get(0)[0] = 99;
        TestSupport.igual(
                0,
                vetoresRecebidos.get(1)[0],
                "Cada chamada do visitante deve receber um vetor independente");
    }
}
