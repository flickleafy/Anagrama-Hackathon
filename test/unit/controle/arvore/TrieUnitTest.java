/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.arvore;

import java.util.Arrays;
import java.util.List;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link Trie}. */
public final class TrieUnitTest
{
    private TrieUnitTest()
    {
    }

    public static void executar()
    {
        Trie trie = new Trie();
        TestSupport.verdadeiro(trie.isEmpty(), "Uma trie nova deve estar vazia");

        int[] primeiro = {1, 2, 3};
        int[] segundo = {1, 2, 4};
        trie.insert(primeiro);
        trie.insert(segundo);
        trie.insert(primeiro);

        TestSupport.verdadeiro(trie.find(primeiro), "O primeiro vetor deve ser encontrado");
        TestSupport.verdadeiro(trie.containsNode(segundo), "O alias de consulta deve encontrar o segundo vetor");
        TestSupport.falso(trie.find(new int[]{1, 2}), "Um prefixo não é um vetor completo");
        TestSupport.igual(2, trie.getVectors().size(), "Uma reinserção não deve duplicar o vetor");

        List<int[]> copia = trie.getVectors();
        copia.get(0)[0] = 99;
        TestSupport.verdadeiro(trie.find(primeiro), "Vetores retornados devem ser cópias defensivas");

        TestSupport.verdadeiro(trie.delete(primeiro), "A exclusão existente deve retornar true");
        TestSupport.falso(trie.find(primeiro), "O vetor excluído não pode permanecer");
        TestSupport.verdadeiro(trie.find(segundo), "Um ramo compartilhado deve ser preservado");
        TestSupport.falso(trie.delete(primeiro), "Excluir novamente deve retornar false");
        TestSupport.verdadeiro(trie.delete(segundo), "O último vetor deve ser excluído");
        TestSupport.verdadeiro(trie.isEmpty(), "A trie deve voltar a ficar vazia");

        trie.insert(new int[0]);
        TestSupport.verdadeiro(trie.find(new int[0]), "O vetor vazio deve ser representável");
        TestSupport.verdadeiro(
                Arrays.equals(new int[0], trie.getVectors().get(0)),
                "O vetor vazio deve ser materializado corretamente");
        TestSupport.verdadeiro(trie.delete(new int[0]), "O vetor vazio deve poder ser excluído");

        TestSupport.lanca(
                IllegalArgumentException.class,
                new TestSupport.Acao()
                {
                    @Override
                    public void executar()
                    {
                        new Trie().insert(null);
                    }
                },
                "Um vetor nulo deve ser rejeitado");
    }
}
