/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import anagrama.Anagrama;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Cria os planos de comprimentos disponíveis para uma expressão-alvo.
 *
 * @author Enzo Erbano
 */
public final class SetCombinacoes
{
    private final ArrayList<ArrayList<Integer>> listaDeSetCombinacoes;
    private final HashMap<Integer, Integer> mapaListasPalavrasEmArray;

    /**
     * Cria os planos cuja soma corresponde ao comprimento normalizado da
     * entrada.
     *
     * @param stringEntrada expressão que define o comprimento total
     * @param colecoesPalavras palavras previamente agrupadas por comprimento
     * @return conjunto independente de planos e do mapa de coleções
     * @throws IllegalArgumentException se a expressão contiver caracteres
     *         inválidos
     */
    public static SetCombinacoes criarSetDeCombinacoes(
            String stringEntrada, ArrayList<ArrayList<String>> colecoesPalavras)
    {
        String entradaNormalizada = Anagrama.normalizar(stringEntrada);
        if (entradaNormalizada == null)
        {
            throw new IllegalArgumentException("A entrada contém caracteres inválidos");
        }
        return new SetCombinacoes(entradaNormalizada.length(), colecoesPalavras);
    }

    /**
     * Gera combinações com repetição dos comprimentos disponíveis.
     *
     * @param comprimentoExpressao quantidade total de letras do alvo
     * @param colecoesPalavras uma coleção não vazia para cada comprimento
     */
    public SetCombinacoes(
            int comprimentoExpressao, ArrayList<ArrayList<String>> colecoesPalavras)
    {
        listaDeSetCombinacoes = new ArrayList<>();
        mapaListasPalavrasEmArray = new HashMap<>();

        ArrayList<Integer> comprimentosDisponiveis = new ArrayList<>();
        for (int i = 0; i < colecoesPalavras.size(); i++)
        {
            ArrayList<String> colecao = colecoesPalavras.get(i);
            if (colecao == null || colecao.isEmpty())
            {
                continue;
            }

            String palavraNormalizada = Anagrama.normalizar(colecao.get(0));
            if (palavraNormalizada == null || palavraNormalizada.isEmpty())
            {
                continue;
            }

            int comprimento = palavraNormalizada.length();
            mapaListasPalavrasEmArray.put(comprimento, i);
            comprimentosDisponiveis.add(comprimento);
        }
        Collections.sort(comprimentosDisponiveis);

        if (comprimentoExpressao > 0)
        {
            combinarRecursivamente(
                    comprimentosDisponiveis,
                    comprimentoExpressao,
                    0,
                    0,
                    new ArrayList<Integer>());
        }
    }

    private void combinarRecursivamente(
            ArrayList<Integer> comprimentos,
            int total,
            int indiceInicial,
            int soma,
            ArrayList<Integer> parcial)
    {
        if (soma == total)
        {
            listaDeSetCombinacoes.add(new ArrayList<>(parcial));
            return;
        }

        for (int i = indiceInicial; i < comprimentos.size(); i++)
        {
            int comprimento = comprimentos.get(i);
            // Como os comprimentos estão ordenados, os próximos também
            // excederiam o total e todo esse ramo pode ser descartado.
            if (soma + comprimento > total)
            {
                break;
            }

            parcial.add(comprimento);
            // Reutilizar o mesmo índice permite planos como 3+3. Não voltar
            // aos índices anteriores evita gerar as permutações 2+3 e 3+2.
            combinarRecursivamente(
                    comprimentos, total, i, soma + comprimento, parcial);
            parcial.remove(parcial.size() - 1);
        }
    }

    /**
     * Retorna uma cópia profunda dos planos gerados.
     *
     * @return planos que podem ser alterados sem afetar esta instância
     */
    public ArrayList<ArrayList<Integer>> getListaDeSetCombinacoes()
    {
        ArrayList<ArrayList<Integer>> copia = new ArrayList<>();
        for (ArrayList<Integer> plano : listaDeSetCombinacoes)
        {
            copia.add(new ArrayList<>(plano));
        }
        return copia;
    }

    /**
     * Retorna a associação entre comprimento e posição da coleção.
     *
     * @return cópia independente do mapa interno
     */
    public HashMap<Integer, Integer> getMapaListasPalavrasEmArray()
    {
        return new HashMap<>(mapaListasPalavrasEmArray);
    }
}
