/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package integration;

import controle.multithread.ControleMultiThread;
import controle.singlethread.Colecoes;
import controle.singlethread.Combinador;
import controle.singlethread.Filtro;
import controle.singlethread.SetCombinacoes;
import controle.singlethread.Validacao;
import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Compara a orquestração multithread com o fluxo sequencial de referência. */
public final class FluxoMultithreadIntegrationTest
{
    private FluxoMultithreadIntegrationTest()
    {
    }

    public static void executar()
    {
        ArrayList<String> origem = new ArrayList<>(Arrays.asList(
                "A", "B", "AB", "BA", "AA", "C"));
        ArrayList<String> single = new ArrayList<>(origem);
        ArrayList<String> multi = new ArrayList<>(origem);
        Filtro.eliminarPalavrasEmLista(single, "AB");
        ControleMultiThread.controlarMultiFiltroLista(multi, "AB");
        TestSupport.igual(single, multi, "Os filtros devem produzir o mesmo resultado e ordem");

        ArrayList<ArrayList<String>> colecoes = Colecoes.divisaoEmColecoes(single);
        SetCombinacoes planos = SetCombinacoes.criarSetDeCombinacoes("AB", colecoes);
        ArrayList<String> combinacoesSingle = new ArrayList<>();
        ArrayList<String> combinacoesMulti = new ArrayList<>();
        Combinador.combinadorPalavras(combinacoesSingle, colecoes, "AB", planos);
        ControleMultiThread.controlarMultiCombinacoesLista(
                combinacoesMulti, colecoes, "AB", planos);
        TestSupport.igual(
                combinacoesSingle,
                combinacoesMulti,
                "A divisão dos planos deve preservar resultados e ordem");
        TestSupport.igual(
                Validacao.contarAnagramasEmListas(combinacoesSingle, "AB"),
                ControleMultiThread.contarMultiValidacaoLista(combinacoesMulti, "AB"),
                "As contagens parciais devem ser agregadas uma única vez");
    }
}
