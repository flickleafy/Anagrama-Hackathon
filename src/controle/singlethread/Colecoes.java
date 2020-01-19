/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import anagrama.Anagrama;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Agrupa palavras do dicionário pelo comprimento normalizado.
 *
 * @author Enzo Erbano
 */
public final class Colecoes
{
    private Colecoes()
    {
    }

    /**
     * Divide as palavras válidas em coleções ordenadas por comprimento.
     *
     * <p>Entradas nulas, vazias ou com caracteres inválidos são ignoradas. A
     * lista recebida não é modificada.</p>
     *
     * @param listaPalavras palavras que serão agrupadas
     * @return novas coleções em ordem crescente de comprimento
     * @throws IllegalArgumentException se a lista for nula
     */
    public static ArrayList<ArrayList<String>> divisaoEmColecoes(
            ArrayList<String> listaPalavras)
    {
        if (listaPalavras == null)
        {
            throw new IllegalArgumentException("A lista de palavras não pode ser nula");
        }

        Map<Integer, ArrayList<String>> palavrasPorTamanho = new TreeMap<>();
        for (String palavra : listaPalavras)
        {
            String normalizada = Anagrama.normalizar(palavra);
            if (normalizada == null || normalizada.isEmpty())
            {
                continue;
            }

            ArrayList<String> colecao = palavrasPorTamanho.get(normalizada.length());
            if (colecao == null)
            {
                colecao = new ArrayList<>();
                palavrasPorTamanho.put(normalizada.length(), colecao);
            }
            colecao.add(palavra);
        }

        return new ArrayList<>(palavrasPorTamanho.values());
    }
}
