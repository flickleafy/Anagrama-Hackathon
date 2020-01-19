/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package util.io;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Leitor de linhas do console com codificação UTF-8.
 *
 * @author Enzo Erbano
 */
public final class ConsoleEntrada implements AutoCloseable
{
    private final Scanner scanner;

    /** Cria um leitor associado à entrada padrão do processo. */
    public ConsoleEntrada()
    {
        this(System.in);
    }

    /**
     * Cria um leitor sobre um fluxo fornecido pelo chamador.
     *
     * @param inputStream fluxo que será consumido e fechado junto com o leitor
     * @throws IllegalArgumentException se o fluxo for nulo
     */
    public ConsoleEntrada(InputStream inputStream)
    {
        if (inputStream == null)
        {
            throw new IllegalArgumentException("O stream de entrada não pode ser nulo");
        }
        scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
    }

    /**
     * Lê a próxima linha completa.
     *
     * @return linha sem o separador de fim de linha
     */
    public String lerConsoleUmaLinha()
    {
        return scanner.nextLine();
    }

    /** Fecha o leitor, mantendo compatibilidade com a API original. */
    public void destruir()
    {
        close();
    }

    @Override
    public void close()
    {
        scanner.close();
    }
}
