/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package integration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import main.Main;
import testes.TestSupport;

/** Exercita uma fatia completa da CLI com um dicionário temporário. */
public final class AplicacaoCliIntegrationTest
{
    private AplicacaoCliIntegrationTest()
    {
    }

    public static void executar() throws Exception
    {
        Path dicionario = Files.createTempFile("anagrama-cli-", ".txt");
        InputStream entradaOriginal = System.in;
        PrintStream saidaOriginal = System.out;
        PrintStream erroOriginal = System.err;
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        ByteArrayOutputStream erro = new ByteArrayOutputStream();
        try
        {
            Files.write(
                    dicionario,
                    Arrays.asList("A", "B", "AB", "BA"),
                    StandardCharsets.UTF_8);
            String entrada = "AB\n" + dicionario + "\n";
            System.setIn(new ByteArrayInputStream(
                    entrada.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(saida, true, StandardCharsets.UTF_8.name()));
            System.setErr(new PrintStream(erro, true, StandardCharsets.UTF_8.name()));
            Main.main(new String[0]);
        }
        finally
        {
            System.setIn(entradaOriginal);
            System.setOut(saidaOriginal);
            System.setErr(erroOriginal);
            Files.deleteIfExists(dicionario);
        }

        String texto = saida.toString(StandardCharsets.UTF_8.name());
        TestSupport.verdadeiro(
                texto.contains("Total de combinações 5"),
                "A CLI deve executar o pipeline completo sobre o arquivo informado");
        TestSupport.verdadeiro(
                texto.contains("Anagramas 3"),
                "A CLI deve imprimir a contagem final correta");
        TestSupport.igual("", erro.toString(StandardCharsets.UTF_8.name()), "A execução válida não deve escrever erros");
    }
}
