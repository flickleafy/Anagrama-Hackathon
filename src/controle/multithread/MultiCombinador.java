/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import controle.singlethread.Combinador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Combina uma partição independente dos planos de comprimento.
 *
 * @author Enzo Erbano
 */
final class MultiCombinador implements Callable<ArrayList<String>>
{
    private final ArrayList<ArrayList<String>> colecoesPalavras;
    private final ArrayList<ArrayList<Integer>> planos;
    private final HashMap<Integer, Integer> mapaColecoes;

    MultiCombinador(
            ArrayList<ArrayList<String>> colecoesPalavras,
            ArrayList<ArrayList<Integer>> planos,
            HashMap<Integer, Integer> mapaColecoes)
    {
        this.colecoesPalavras = colecoesPalavras;
        this.planos = planos;
        this.mapaColecoes = mapaColecoes;
    }

    @Override
    public ArrayList<String> call()
    {
        ArrayList<String> combinacoes = new ArrayList<>();
        Combinador.combinarPlanos(
                combinacoes, colecoesPalavras, planos, mapaColecoes);
        return combinacoes;
    }
}
