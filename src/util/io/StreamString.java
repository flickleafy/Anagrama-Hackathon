/*
 * Copyright (C) 2021 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Converte um fluxo UTF-8 em uma lista de linhas.
 *
 * @author Enzo Erbano
 */
public final class StreamString
{
    /**
     * Consome todas as linhas e fecha o fluxo recebido.
     *
     * @param inputStream fluxo UTF-8 de origem
     * @return linhas na mesma ordem em que foram lidas
     * @throws IllegalArgumentException se o fluxo for nulo
     * @throws IllegalStateException se ocorrer uma falha de leitura
     */
    public ArrayList<String> streamParaString(InputStream inputStream)
    {
        if (inputStream == null)
        {
            throw new IllegalArgumentException("O stream de entrada não pode ser nulo");
        }

        ArrayList<String> listaString = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)))
        {
            String linha;
            while ((linha = reader.readLine()) != null)
            {
                listaString.add(linha);
            }
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Não foi possível ler o stream", e);
        }
        return listaString;
    }
}
