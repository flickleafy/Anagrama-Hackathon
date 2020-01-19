/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.arvore;

import controle.singlethread.SetCombinacoes;
import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link ArvoreCombinador}. */
public final class ArvoreCombinadorUnitTest
{
    private ArvoreCombinadorUnitTest()
    {
    }

    public static void executar()
    {
        new ArvoreCombinador();

        ArrayList<ArrayList<String>> colecoes = new ArrayList<>();
        colecoes.add(new ArrayList<>(Arrays.asList("A", "B")));
        SetCombinacoes planos = new SetCombinacoes(2, colecoes);

        ArrayList<String> destino = new ArrayList<>(Arrays.asList("INICIAL"));
        new ArvoreCombinador(destino, colecoes, planos);
        TestSupport.igual(
                Arrays.asList("INICIAL", "AA", "AB", "BB"),
                destino,
                "O construtor deve acrescentar cada sequência canônica uma vez");

        ArrayList<String> chamadaEstatica = new ArrayList<>();
        ArvoreCombinador.combinadorPalavras(chamadaEstatica, colecoes, planos);
        TestSupport.igual(
                Arrays.asList("AA", "AB", "BB"),
                chamadaEstatica,
                "A operação estática deve produzir o mesmo conjunto ordenado");
    }
}
