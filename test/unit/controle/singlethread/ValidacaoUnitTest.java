/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link Validacao}. */
public final class ValidacaoUnitTest
{
    private ValidacaoUnitTest()
    {
    }

    public static void executar() throws Exception
    {
        ArrayList<String> candidatos = new ArrayList<>(Arrays.asList(
                "ABCDEF", "FEDCBA", "ABCABC", "AABBCC"));
        TestSupport.igual(
                Arrays.asList("ABCDEF", "FEDCBA"),
                Validacao.encontrarAnagramasEmListas(candidatos, "ABCDEF"),
                "A seleção deve preservar somente anagramas e sua ordem");
        TestSupport.igual(
                2L,
                Validacao.contarAnagramasEmListas(candidatos, "ABCDEF"),
                "A contagem deve corresponder à seleção");

        PrintStream original = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try
        {
            System.setOut(new PrintStream(bytes, true, StandardCharsets.UTF_8.name()));
            Validacao.validarAnagramasEmListas(candidatos, "ABCDEF");
        }
        finally
        {
            System.setOut(original);
        }
        TestSupport.verdadeiro(
                bytes.toString(StandardCharsets.UTF_8.name()).contains("Anagramas 2"),
                "A validação deve imprimir a contagem calculada");
    }
}
