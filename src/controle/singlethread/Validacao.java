/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import anagrama.Anagrama;
import java.util.ArrayList;
import java.util.List;

/**
 * Valida as combinações geradas em relação à expressão de entrada.
 *
 * @author Enzo Erbano
 */
public final class Validacao
{
    private Validacao()
    {
    }

    /**
     * Seleciona as combinações que são anagramas da entrada.
     *
     * @param listaPalavrasCombinadas candidatos que serão examinados
     * @param stringEntrada expressão de referência
     * @return nova lista contendo somente os anagramas, na ordem original
     */
    public static ArrayList<String> encontrarAnagramasEmListas(
            List<String> listaPalavrasCombinadas, String stringEntrada)
    {
        ArrayList<String> anagramas = new ArrayList<>();
        for (String palavra : listaPalavrasCombinadas)
        {
            if (Anagrama.checarAnagrama(stringEntrada, palavra))
            {
                anagramas.add(palavra);
            }
        }
        return anagramas;
    }

    /**
     * Conta os anagramas sem materializar uma lista intermediária.
     *
     * @param listaPalavrasCombinadas candidatos que serão examinados
     * @param stringEntrada expressão de referência
     * @return quantidade de candidatos que são anagramas
     */
    public static long contarAnagramasEmListas(
            List<String> listaPalavrasCombinadas, String stringEntrada)
    {
        long quantidade = 0L;
        for (String palavra : listaPalavrasCombinadas)
        {
            if (Anagrama.checarAnagrama(stringEntrada, palavra))
            {
                quantidade++;
            }
        }
        return quantidade;
    }

    /**
     * Imprime no console a quantidade de anagramas encontrados.
     *
     * @param listaPalavrasCombinadas candidatos que serão examinados
     * @param stringEntrada expressão de referência
     */
    public static void validarAnagramasEmListas(
            ArrayList<String> listaPalavrasCombinadas, String stringEntrada)
    {
        System.out.println(
                "Anagramas "
                + contarAnagramasEmListas(listaPalavrasCombinadas, stringEntrada));
    }
}
