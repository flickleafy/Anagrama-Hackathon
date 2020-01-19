/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.arvore;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Representa um nó interno da trie e preserva a ordem de inserção dos ramos.
 */
final class TrieNode
{
    private final Map<Integer, TrieNode> children = new LinkedHashMap<>();
    private boolean endOfVector;

    Map<Integer, TrieNode> getChildren()
    {
        return children;
    }

    boolean isEndOfVector()
    {
        return endOfVector;
    }

    void setEndOfVector(boolean endOfVector)
    {
        this.endOfVector = endOfVector;
    }
}
