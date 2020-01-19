/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.arvore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Armazena vetores de inteiros na trie usada pelo combinador em árvore.
 */
public final class Trie
{
    private final TrieNode root = new TrieNode();

    /**
     * Insere um vetor; reinserir o mesmo conteúdo não cria uma duplicata.
     *
     * @param vector sequência que será armazenada
     * @throws IllegalArgumentException se o vetor for nulo
     */
    public void insert(int[] vector)
    {
        validarVetor(vector);
        TrieNode current = root;
        for (int numero : vector)
        {
            TrieNode proximo = current.getChildren().get(numero);
            if (proximo == null)
            {
                proximo = new TrieNode();
                current.getChildren().put(numero, proximo);
            }
            current = proximo;
        }
        current.setEndOfVector(true);
    }

    /**
     * Exclui um vetor completo sem remover prefixos compartilhados.
     *
     * @param vector sequência que será removida
     * @return {@code true} se o vetor existia e foi removido
     * @throws IllegalArgumentException se o vetor for nulo
     */
    public boolean delete(int[] vector)
    {
        validarVetor(vector);
        if (!find(vector))
        {
            return false;
        }
        remover(root, vector, 0);
        return true;
    }

    /**
     * Mantém o nome da consulta usado pela API original.
     *
     * @param vector sequência procurada
     * @return {@code true} somente para um vetor completo armazenado
     * @throws IllegalArgumentException se o vetor for nulo
     */
    public boolean containsNode(int[] vector)
    {
        return find(vector);
    }

    /**
     * Procura um vetor completo; um prefixo isolado não é suficiente.
     *
     * @param vector sequência procurada
     * @return {@code true} se o vetor foi inserido
     * @throws IllegalArgumentException se o vetor for nulo
     */
    public boolean find(int[] vector)
    {
        validarVetor(vector);
        TrieNode current = root;
        for (int numero : vector)
        {
            current = current.getChildren().get(numero);
            if (current == null)
            {
                return false;
            }
        }
        return current.isEndOfVector();
    }

    /**
     * Informa se nenhum vetor está armazenado.
     *
     * @return {@code true} quando a raiz não representa nem possui vetores
     */
    public boolean isEmpty()
    {
        return root.getChildren().isEmpty() && !root.isEndOfVector();
    }

    /**
     * Materializa cópias dos vetores seguindo a ordem dos ramos inseridos.
     *
     * @return nova lista com novos vetores, sem expor os nós internos
     */
    public List<int[]> getVectors()
    {
        ArrayList<int[]> vectors = new ArrayList<>();
        coletar(root, new ArrayList<Integer>(), vectors);
        return vectors;
    }

    /**
     * Remove recursivamente os nós que deixaram de participar de outro vetor.
     *
     * @return {@code true} quando o chamador também pode remover o nó atual
     */
    private boolean remover(TrieNode current, int[] vector, int index)
    {
        if (index == vector.length)
        {
            current.setEndOfVector(false);
            return current.getChildren().isEmpty();
        }

        int numero = vector[index];
        TrieNode child = current.getChildren().get(numero);
        boolean removerFilho = remover(child, vector, index + 1);
        if (removerFilho)
        {
            current.getChildren().remove(numero);
        }
        return current.getChildren().isEmpty() && !current.isEndOfVector();
    }

    /**
     * Percorre a trie em profundidade e copia cada caminho completo.
     */
    private void coletar(
            TrieNode node, ArrayList<Integer> caminho, List<int[]> destino)
    {
        if (node.isEndOfVector())
        {
            int[] vector = new int[caminho.size()];
            for (int i = 0; i < caminho.size(); i++)
            {
                vector[i] = caminho.get(i);
            }
            destino.add(vector);
        }

        for (Map.Entry<Integer, TrieNode> entry : node.getChildren().entrySet())
        {
            caminho.add(entry.getKey());
            coletar(entry.getValue(), caminho, destino);
            caminho.remove(caminho.size() - 1);
        }
    }

    private void validarVetor(int[] vector)
    {
        if (vector == null)
        {
            throw new IllegalArgumentException("O vetor não pode ser nulo");
        }
    }
}
