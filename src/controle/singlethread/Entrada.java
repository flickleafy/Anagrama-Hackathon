/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import anagrama.Anagrama;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import util.io.ConsoleEntrada;
import util.io.LerArquivo;
import util.io.LerRecurso;
import util.io.StreamString;

/**
 * Lê e valida as entradas usadas pela aplicação.
 *
 * @author Enzo Erbano
 */
public final class Entrada
{
    /** Caminho do dicionário distribuído no classpath. */
    public static final String RECURSO_DICIONARIO = "res/palavras.82eebac6.txt";

    private Entrada()
    {
    }

    /**
     * Lê uma linha do console e a converte para maiúsculas.
     *
     * @param consoleManopla leitor de console que fornece a linha
     * @return texto digitado, preservando seus espaços
     */
    public static String lerEntradaConsole(ConsoleEntrada consoleManopla)
    {
        System.out.println("Digite uma palavra ou frase : ");
        return consoleManopla.lerConsoleUmaLinha().toUpperCase(Locale.ROOT);
    }

    /**
     * Lê o dicionário indicado no console.
     *
     * <p>Um caminho com menos de cinco caracteres seleciona o recurso
     * distribuído com a aplicação. Se o arquivo não puder ser aberto, retorna
     * uma lista vazia.</p>
     *
     * @param consoleManopla leitor de console que fornece o caminho
     * @return linhas UTF-8 do dicionário escolhido
     */
    public static ArrayList<String> lerArquivoConsole(ConsoleEntrada consoleManopla)
    {
        System.out.println("Digite o diretório e nome do dicionario : ");
        String diretorio = consoleManopla.lerConsoleUmaLinha();
        InputStream stream;

        if (diretorio.length() < 5)
        {
            stream = new LerRecurso().lerRecurso(RECURSO_DICIONARIO);
        }
        else
        {
            stream = new LerArquivo().lerArquivo(diretorio);
        }

        if (stream == null)
        {
            return new ArrayList<>();
        }
        return new StreamString().streamParaString(stream);
    }

    /**
     * Informa se uma expressão deve ser rejeitada.
     *
     * @param input expressão que será normalizada
     * @return {@code true} para entrada nula, sem letras ou com caracteres fora
     *         de {@code A-Z}; {@code false} para uma entrada válida
     */
    public static boolean validarString(String input)
    {
        String normalizado = Anagrama.normalizar(input);
        return normalizado == null || normalizado.isEmpty();
    }
}
