/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import anagrama.Anagrama;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Filtra palavras que não podem ser formadas pelas letras da entrada.
 *
 * @author Enzo Erbano
 */
public final class Filtro
{
    private Filtro()
    {
    }

    /**
     * Remove da própria lista as palavras incompatíveis com a entrada.
     *
     * @param listaPalavras lista mutável que será filtrada
     * @param stringEntrada letras disponíveis para formar cada palavra
     * @return a mesma instância recebida, depois das remoções
     * @throws IllegalArgumentException se a lista for nula
     */
    public static ArrayList<String> eliminarPalavrasEmLista(
            ArrayList<String> listaPalavras, String stringEntrada)
    {
        if (listaPalavras == null)
        {
            throw new IllegalArgumentException("A lista de palavras não pode ser nula");
        }

        Iterator<String> iterator = listaPalavras.iterator();
        while (iterator.hasNext())
        {
            if (!Anagrama.podeSerFormadaPor(iterator.next(), stringEntrada))
            {
                iterator.remove();
            }
        }
        return listaPalavras;
    }
}
