/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package integration;

import controle.arvore.ArvoreCombinador;
import controle.singlethread.Colecoes;
import controle.singlethread.Combinador;
import controle.singlethread.SetCombinacoes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import testes.TestSupport;

/** Integra o combinador em trie ao formato de coleções do fluxo padrão. */
public final class CombinadorArvoreIntegrationTest
{
    private CombinadorArvoreIntegrationTest()
    {
    }

    public static void executar()
    {
        ArrayList<ArrayList<String>> colecoes = Colecoes.divisaoEmColecoes(
                new ArrayList<>(Arrays.asList("A", "B", "AB", "BA")));
        SetCombinacoes planos = SetCombinacoes.criarSetDeCombinacoes("AB", colecoes);
        ArrayList<String> referencia = new ArrayList<>();
        ArrayList<String> arvore = new ArrayList<>();
        Combinador.combinadorPalavras(referencia, colecoes, "AB", planos);
        ArvoreCombinador.combinadorPalavras(arvore, colecoes, planos);

        TestSupport.igual(
                ordenar(referencia),
                ordenar(arvore),
                "A trie deve produzir o mesmo multiconjunto da implementação padrão");
    }

    private static List<String> ordenar(List<String> valores)
    {
        ArrayList<String> ordenados = new ArrayList<>(valores);
        Collections.sort(ordenados);
        return ordenados;
    }
}
