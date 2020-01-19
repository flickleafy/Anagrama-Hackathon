/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import controle.CombinacaoPlanos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Combina palavras sequencialmente a partir dos planos de comprimento.
 *
 * @author Enzo Erbano
 */
public final class Combinador
{
    private Combinador()
    {
    }

    /**
     * Acrescenta ao destino todas as combinações descritas pelos planos.
     *
     * <p>O parâmetro {@code stringEntrada} é mantido por compatibilidade com a
     * API original; o comprimento-alvo já está representado nos planos.</p>
     *
     * @param listaPalavrasCombinadas destino que receberá os resultados
     * @param colecoesPalavras palavras agrupadas por comprimento
     * @param stringEntrada expressão-alvo mantida por compatibilidade
     * @param setCombinacoesManopla planos e mapa das coleções
     */
    public static void combinadorPalavras(
            ArrayList<String> listaPalavrasCombinadas,
            ArrayList<ArrayList<String>> colecoesPalavras,
            String stringEntrada,
            SetCombinacoes setCombinacoesManopla)
    {
        combinarPlanos(
                listaPalavrasCombinadas,
                colecoesPalavras,
                setCombinacoesManopla.getListaDeSetCombinacoes(),
                setCombinacoesManopla.getMapaListasPalavrasEmArray());
    }

    /**
     * Combina somente a partição de planos informada.
     *
     * <p>Este limite permite que as versões sequencial e multithread reutilizem
     * exatamente a mesma regra de enumeração canônica.</p>
     *
     * @param listaPalavrasCombinadas destino que será acrescido
     * @param colecoesPalavras palavras agrupadas por comprimento
     * @param planos partição de planos a percorrer
     * @param mapaColecoes associa comprimentos aos índices das coleções
     */
    public static void combinarPlanos(
            final ArrayList<String> listaPalavrasCombinadas,
            final ArrayList<ArrayList<String>> colecoesPalavras,
            List<? extends List<Integer>> planos,
            final HashMap<Integer, Integer> mapaColecoes)
    {
        CombinacaoPlanos.visitar(
                planos,
                colecoesPalavras,
                mapaColecoes,
                new CombinacaoPlanos.Visitante()
                {
                    @Override
                    public void aceitar(List<Integer> plano, int[] indicesPalavras)
                    {
                        StringBuilder combinacao = new StringBuilder();
                        for (int i = 0; i < plano.size(); i++)
                        {
                            int indiceColecao = mapaColecoes.get(plano.get(i));
                            combinacao.append(
                                    colecoesPalavras.get(indiceColecao)
                                            .get(indicesPalavras[i]));
                        }
                        listaPalavrasCombinadas.add(combinacao.toString());
                    }
                });
    }
}
