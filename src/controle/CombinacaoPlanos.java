/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Percorre seleções de palavras descritas por planos de comprimentos.
 */
public final class CombinacaoPlanos
{
    /**
     * Recebe cada seleção canônica produzida durante o percurso.
     */
    public interface Visitante
    {
        /**
         * Processa os índices escolhidos para um plano.
         *
         * @param plano comprimentos das palavras na ordem da combinação
         * @param indicesPalavras índice escolhido dentro de cada coleção
         */
        void aceitar(List<Integer> plano, int[] indicesPalavras);
    }

    private CombinacaoPlanos()
    {
    }

    /**
     * Visita todas as seleções válidas dos planos informados.
     *
     * <p>Planos vazios ou sem uma coleção correspondente são ignorados. Para
     * comprimentos repetidos, somente a ordem canônica dos índices é emitida,
     * evitando permutações equivalentes.</p>
     *
     * @param planos sequências de comprimentos que devem ser percorridas
     * @param colecoes palavras agrupadas por comprimento
     * @param mapaColecoes associa cada comprimento ao índice de sua coleção
     * @param visitante consumidor chamado para cada seleção encontrada
     */
    public static void visitar(
            List<? extends List<Integer>> planos,
            List<? extends List<String>> colecoes,
            Map<Integer, Integer> mapaColecoes,
            Visitante visitante)
    {
        for (List<Integer> plano : planos)
        {
            if (plano == null || plano.isEmpty())
            {
                continue;
            }
            visitarPlano(
                    plano,
                    colecoes,
                    mapaColecoes,
                    visitante,
                    new int[plano.size()],
                    0);
        }
    }

    private static void visitarPlano(
            List<Integer> plano,
            List<? extends List<String>> colecoes,
            Map<Integer, Integer> mapaColecoes,
            Visitante visitante,
            int[] indices,
            int posicao)
    {
        if (posicao == plano.size())
        {
            visitante.aceitar(plano, Arrays.copyOf(indices, indices.length));
            return;
        }

        Integer indiceColecao = mapaColecoes.get(plano.get(posicao));
        if (indiceColecao == null || indiceColecao < 0
                || indiceColecao >= colecoes.size())
        {
            return;
        }

        List<String> colecao = colecoes.get(indiceColecao);
        int indiceInicial = 0;
        if (posicao > 0 && plano.get(posicao).equals(plano.get(posicao - 1)))
        {
            // Mantém índices não decrescentes: a mesma palavra pode ser
            // reutilizada, mas a permutação equivalente não é gerada novamente.
            indiceInicial = indices[posicao - 1];
        }

        for (int i = indiceInicial; i < colecao.size(); i++)
        {
            indices[posicao] = i;
            visitarPlano(
                    plano,
                    colecoes,
                    mapaColecoes,
                    visitante,
                    indices,
                    posicao + 1);
        }
    }
}
