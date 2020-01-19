/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package integration;

import controle.singlethread.Entrada;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;
import util.io.ConsoleEntrada;

/** Integra a entrada de console com arquivos externos e recursos empacotados. */
public final class EntradaArquivoIntegrationTest
{
    private EntradaArquivoIntegrationTest()
    {
    }

    public static void executar() throws Exception
    {
        PrintStream original = System.out;
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        Path dicionario = Files.createTempFile("anagrama-integracao-", ".txt");
        try
        {
            System.setOut(new PrintStream(saida, true, StandardCharsets.UTF_8.name()));
            Files.write(
                    dicionario,
                    Arrays.asList("AÇÃO", "ROMA"),
                    StandardCharsets.UTF_8);
            try (ConsoleEntrada console = consoleComLinha(dicionario.toString()))
            {
                TestSupport.igual(
                        Arrays.asList("AÇÃO", "ROMA"),
                        Entrada.lerArquivoConsole(console),
                        "O caminho lido no console deve chegar ao leitor UTF-8");
            }

            try (ConsoleEntrada console = consoleComLinha(""))
            {
                ArrayList<String> recurso = Entrada.lerArquivoConsole(console);
                TestSupport.verdadeiro(
                        recurso.size() > 1000 && "A".equals(recurso.get(0)),
                        "Um caminho curto deve carregar o dicionário distribuído");
            }
        }
        finally
        {
            System.setOut(original);
            Files.deleteIfExists(dicionario);
        }
    }

    private static ConsoleEntrada consoleComLinha(String linha)
    {
        return new ConsoleEntrada(new ByteArrayInputStream(
                (linha + "\n").getBytes(StandardCharsets.UTF_8)));
    }
}
