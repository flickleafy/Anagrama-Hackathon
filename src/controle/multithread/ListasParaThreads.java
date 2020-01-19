/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import java.util.ArrayList;
import java.util.List;

/**
 * Cria partições balanceadas e independentes para as threads de trabalho.
 *
 * @author Enzo Erbano
 */
final class ListasParaThreads
{
    private ListasParaThreads()
    {
    }

    /**
     * Divide uma lista em cópias contíguas com diferença máxima de um item.
     *
     * <p>A quantidade efetiva nunca ultrapassa o número de itens; por isso uma
     * entrada vazia não produz partições nem threads sem trabalho.</p>
     *
     * @param itens elementos que serão distribuídos
     * @param quantidadePartes limite de partições desejado
     * @param <T> tipo dos elementos
     * @return partições independentes na mesma ordem da lista original
     * @throws IllegalArgumentException se a lista for nula
     */
    static <T> ArrayList<ArrayList<T>> dividirLista(
            List<T> itens, int quantidadePartes)
    {
        ArrayList<ArrayList<T>> partes = new ArrayList<>();
        if (itens == null)
        {
            throw new IllegalArgumentException("A lista não pode ser nula");
        }
        if (itens.isEmpty())
        {
            return partes;
        }

        int numeroPartes = Math.max(1, Math.min(quantidadePartes, itens.size()));
        int tamanhoBase = itens.size() / numeroPartes;
        int restantes = itens.size() % numeroPartes;
        int inicio = 0;

        for (int i = 0; i < numeroPartes; i++)
        {
            int tamanho = tamanhoBase + (i < restantes ? 1 : 0);
            int fim = inicio + tamanho;
            partes.add(new ArrayList<>(itens.subList(inicio, fim)));
            inicio = fim;
        }
        return partes;
    }
}
