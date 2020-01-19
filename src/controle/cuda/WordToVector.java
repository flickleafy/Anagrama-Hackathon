/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.cuda;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Atribui identificadores numéricos estáveis às palavras usadas pelo CUDA.
 *
 * @author Enzo Erbano
 */
public final class WordToVector
{
    private final Map<Integer, String> wordToNumberMapping = new LinkedHashMap<>();
    private final ArrayList<int[]> colecoesVetores = new ArrayList<>();

    /**
     * Numera as palavras na ordem das coleções e de seus elementos.
     *
     * @param colecoesPalavras palavras agrupadas por comprimento
     */
    public WordToVector(ArrayList<ArrayList<String>> colecoesPalavras)
    {
        int wordId = 0;
        for (ArrayList<String> colecao : colecoesPalavras)
        {
            int[] ids = new int[colecao.size()];
            for (int i = 0; i < colecao.size(); i++)
            {
                ids[i] = wordId;
                wordToNumberMapping.put(wordId, colecao.get(i));
                wordId++;
            }
            colecoesVetores.add(ids);
        }
    }

    /**
     * Retorna o mapa usado para reconstruir palavras a partir dos IDs.
     *
     * @return cópia independente que preserva a ordem de inserção
     */
    public Map<Integer, String> getWordToNumberMapping()
    {
        return new LinkedHashMap<>(wordToNumberMapping);
    }

    /**
     * Retorna as coleções vetorizadas sem expor os arrays internos.
     *
     * @return nova lista contendo uma cópia de cada vetor
     */
    public ArrayList<int[]> getColecoesVetores()
    {
        ArrayList<int[]> copia = new ArrayList<>();
        for (int[] colecao : colecoesVetores)
        {
            copia.add(colecao.clone());
        }
        return copia;
    }
}
