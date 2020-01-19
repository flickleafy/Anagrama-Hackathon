/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.arvore;

import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link TrieNode}. */
public final class TrieNodeUnitTest
{
    private TrieNodeUnitTest()
    {
    }

    public static void executar()
    {
        TrieNode no = new TrieNode();
        TestSupport.verdadeiro(no.getChildren().isEmpty(), "Um nó novo não deve ter filhos");
        TestSupport.falso(no.isEndOfVector(), "Um nó novo não deve encerrar vetor");

        no.setEndOfVector(true);
        TestSupport.verdadeiro(no.isEndOfVector(), "O marcador de fim deve ser mutável");

        no.getChildren().put(7, new TrieNode());
        no.getChildren().put(3, new TrieNode());
        TestSupport.igual(
                Arrays.asList(7, 3),
                new ArrayList<>(no.getChildren().keySet()),
                "Os ramos devem preservar a ordem de inserção");
    }
}
