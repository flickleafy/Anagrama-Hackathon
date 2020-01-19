/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link Main}. */
public final class MainUnitTest
{
    private MainUnitTest()
    {
    }

    public static void executar() throws Exception
    {
        TestSupport.verdadeiro(
                Modifier.isStatic(Main.class.getMethod("main", String[].class).getModifiers()),
                "O ponto de entrada deve permanecer estático");

        InputStream entradaOriginal = System.in;
        PrintStream saidaOriginal = System.out;
        PrintStream erroOriginal = System.err;
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        ByteArrayOutputStream erro = new ByteArrayOutputStream();
        try
        {
            System.setIn(new ByteArrayInputStream(
                    "ROMA-ANTIGA\n".getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(saida, true, StandardCharsets.UTF_8.name()));
            System.setErr(new PrintStream(erro, true, StandardCharsets.UTF_8.name()));
            Main.main(new String[0]);
        }
        finally
        {
            System.setIn(entradaOriginal);
            System.setOut(saidaOriginal);
            System.setErr(erroOriginal);
        }

        TestSupport.verdadeiro(
                erro.toString(StandardCharsets.UTF_8.name())
                        .contains("caracteres inválidos"),
                "Uma entrada inválida deve encerrar antes de solicitar o dicionário");
        TestSupport.falso(
                saida.toString(StandardCharsets.UTF_8.name())
                        .contains("Digite o diretório"),
                "O dicionário não deve ser solicitado depois de uma entrada inválida");
    }
}
