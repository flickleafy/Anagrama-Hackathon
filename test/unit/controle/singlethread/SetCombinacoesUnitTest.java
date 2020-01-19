/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link SetCombinacoes}. */
public final class SetCombinacoesUnitTest
{
    private SetCombinacoesUnitTest()
    {
    }

    public static void executar()
    {
        ArrayList<ArrayList<String>> colecoes = new ArrayList<>();
        colecoes.add(new ArrayList<>(Arrays.asList("A", "B")));
        colecoes.add(new ArrayList<>(Arrays.asList("AB", "BA")));

        SetCombinacoes planos = new SetCombinacoes(4, colecoes);
        TestSupport.igual(
                Arrays.asList(
                        Arrays.asList(1, 1, 1, 1),
                        Arrays.asList(1, 1, 2),
                        Arrays.asList(2, 2)),
                planos.getListaDeSetCombinacoes(),
                "Os planos devem permitir repetição sem permutações duplicadas");
        TestSupport.igual(0, planos.getMapaListasPalavrasEmArray().get(1), "O mapa deve localizar a coleção de tamanho 1");
        TestSupport.igual(1, planos.getMapaListasPalavrasEmArray().get(2), "O mapa deve localizar a coleção de tamanho 2");

        ArrayList<ArrayList<Integer>> copiaPlanos = planos.getListaDeSetCombinacoes();
        copiaPlanos.get(0).clear();
        TestSupport.falso(
                planos.getListaDeSetCombinacoes().get(0).isEmpty(),
                "A lista de planos deve ser uma cópia profunda");

        planos.getMapaListasPalavrasEmArray().clear();
        TestSupport.falso(
                planos.getMapaListasPalavrasEmArray().isEmpty(),
                "O mapa retornado deve ser uma cópia defensiva");

        SetCombinacoes isolado = new SetCombinacoes(
                2,
                new ArrayList<>(Collections.singletonList(
                        new ArrayList<>(Collections.singletonList("AB")))));
        TestSupport.igual(
                Collections.singletonList(Collections.singletonList(2)),
                isolado.getListaDeSetCombinacoes(),
                "Instâncias não devem compartilhar estado");

        TestSupport.lanca(
                IllegalArgumentException.class,
                new TestSupport.Acao()
                {
                    @Override
                    public void executar()
                    {
                        SetCombinacoes.criarSetDeCombinacoes("A-", new ArrayList<ArrayList<String>>());
                    }
                },
                "Uma expressão inválida deve ser rejeitada");
    }
}
