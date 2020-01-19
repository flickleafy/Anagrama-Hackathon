/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package integration;

import controle.singlethread.Colecoes;
import controle.singlethread.Combinador;
import controle.singlethread.Filtro;
import controle.singlethread.SetCombinacoes;
import controle.singlethread.Validacao;
import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Integra os estágios do fluxo sequencial com uma entrada determinística. */
public final class FluxoSingleThreadIntegrationTest
{
    private FluxoSingleThreadIntegrationTest()
    {
    }

    public static void executar()
    {
        ArrayList<String> palavras = new ArrayList<>(Arrays.asList(
                "A", "B", "AB", "BA", "AA", "C"));
        Filtro.eliminarPalavrasEmLista(palavras, "AB");
        TestSupport.igual(
                Arrays.asList("A", "B", "AB", "BA"),
                palavras,
                "O filtro deve entregar somente palavras utilizáveis ao agrupamento");

        ArrayList<ArrayList<String>> colecoes = Colecoes.divisaoEmColecoes(palavras);
        SetCombinacoes planos = SetCombinacoes.criarSetDeCombinacoes("AB", colecoes);
        ArrayList<String> combinacoes = new ArrayList<>();
        Combinador.combinadorPalavras(combinacoes, colecoes, "AB", planos);

        TestSupport.igual(
                Arrays.asList("AA", "AB", "BB", "AB", "BA"),
                combinacoes,
                "Agrupamento, planejamento e combinação devem preservar a ordem contratada");
        TestSupport.igual(
                Arrays.asList("AB", "AB", "BA"),
                Validacao.encontrarAnagramasEmListas(combinacoes, "AB"),
                "A validação deve consumir corretamente a saída do combinador");
    }
}
