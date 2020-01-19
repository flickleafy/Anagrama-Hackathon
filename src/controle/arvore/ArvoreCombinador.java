/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.arvore;

import controle.CombinacaoPlanos;
import controle.singlethread.SetCombinacoes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Combina palavras armazenando sequências de identificadores em uma trie.
 *
 * @author Enzo Erbano
 */
public final class ArvoreCombinador
{
    /** Cria uma instância sem iniciar uma combinação. */
    public ArvoreCombinador()
    {
    }

    /**
     * Executa imediatamente a alternativa baseada em trie.
     *
     * @param listaPalavrasCombinadas destino que receberá os resultados
     * @param colecoesPalavras palavras agrupadas por comprimento
     * @param setCombinacoes planos que descrevem as combinações
     */
    public ArvoreCombinador(
            ArrayList<String> listaPalavrasCombinadas,
            ArrayList<ArrayList<String>> colecoesPalavras,
            SetCombinacoes setCombinacoes)
    {
        combinadorPalavras(
                listaPalavrasCombinadas,
                colecoesPalavras,
                setCombinacoes);
    }

    /**
     * Acrescenta ao destino as mesmas combinações da versão sequencial.
     *
     * <p>A trie armazena identificadores, em vez dos textos, para compartilhar
     * prefixos sem tornar palavras iguais de posições distintas
     * indistinguíveis.</p>
     *
     * @param listaPalavrasCombinadas destino que será acrescido
     * @param colecoesPalavras palavras agrupadas por comprimento
     * @param setCombinacoes planos e mapa das coleções
     */
    public static void combinadorPalavras(
            final ArrayList<String> listaPalavrasCombinadas,
            final ArrayList<ArrayList<String>> colecoesPalavras,
            SetCombinacoes setCombinacoes)
    {
        final Trie trie = new Trie();
        final int[][] idsPorColecao = criarIdsPorColecao(colecoesPalavras);
        final Map<Integer, String> palavrasPorId = criarMapaPalavras(
                colecoesPalavras, idsPorColecao);
        final HashMap<Integer, Integer> mapaColecoes =
                setCombinacoes.getMapaListasPalavrasEmArray();

        CombinacaoPlanos.visitar(
                setCombinacoes.getListaDeSetCombinacoes(),
                colecoesPalavras,
                mapaColecoes,
                new CombinacaoPlanos.Visitante()
                {
                    @Override
                    public void aceitar(List<Integer> plano, int[] indicesPalavras)
                    {
                        int[] ids = new int[indicesPalavras.length];
                        for (int i = 0; i < indicesPalavras.length; i++)
                        {
                            int indiceColecao = mapaColecoes.get(plano.get(i));
                            ids[i] = idsPorColecao[indiceColecao][indicesPalavras[i]];
                        }
                        trie.insert(ids);
                    }
                });

        // A materialização ocorre somente ao final para que a trie seja a
        // estrutura responsável pela deduplicação das sequências de IDs.
        for (int[] vector : trie.getVectors())
        {
            StringBuilder combinacao = new StringBuilder();
            for (int id : vector)
            {
                combinacao.append(palavrasPorId.get(id));
            }
            listaPalavrasCombinadas.add(combinacao.toString());
        }
    }

    /**
     * Atribui IDs consecutivos e estáveis a cada posição das coleções.
     */
    private static int[][] criarIdsPorColecao(
            ArrayList<ArrayList<String>> colecoesPalavras)
    {
        int[][] ids = new int[colecoesPalavras.size()][];
        int proximoId = 0;
        for (int i = 0; i < colecoesPalavras.size(); i++)
        {
            ids[i] = new int[colecoesPalavras.get(i).size()];
            for (int j = 0; j < ids[i].length; j++)
            {
                ids[i][j] = proximoId++;
            }
        }
        return ids;
    }

    /**
     * Cria o mapa inverso usado para reconstruir os textos ao final.
     */
    private static Map<Integer, String> criarMapaPalavras(
            ArrayList<ArrayList<String>> colecoesPalavras,
            int[][] idsPorColecao)
    {
        Map<Integer, String> palavras = new HashMap<>();
        for (int i = 0; i < colecoesPalavras.size(); i++)
        {
            for (int j = 0; j < colecoesPalavras.get(i).size(); j++)
            {
                palavras.put(idsPorColecao[i][j], colecoesPalavras.get(i).get(j));
            }
        }
        return palavras;
    }
}
