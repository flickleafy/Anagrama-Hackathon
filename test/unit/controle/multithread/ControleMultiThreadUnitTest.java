/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import controle.singlethread.SetCombinacoes;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link ControleMultiThread}. */
public final class ControleMultiThreadUnitTest
{
    private ControleMultiThreadUnitTest()
    {
    }

    public static void executar() throws Exception
    {
        ArrayList<String> vazia = new ArrayList<>();
        TestSupport.verdadeiro(
                ControleMultiThread.controlarMultiFiltroLista(vazia, "ABC") == vazia,
                "O filtro vazio deve devolver a mesma instância");
        TestSupport.igual(
                0L,
                ControleMultiThread.contarMultiValidacaoLista(vazia, "ABC"),
                "A validação vazia deve retornar zero");

        ArrayList<String> destino = new ArrayList<>(Arrays.asList("INICIAL"));
        SetCombinacoes semPlanos = new SetCombinacoes(
                0,
                new ArrayList<ArrayList<String>>());
        TestSupport.verdadeiro(
                ControleMultiThread.controlarMultiCombinacoesLista(
                        destino,
                        new ArrayList<ArrayList<String>>(),
                        "",
                        semPlanos) == destino,
                "A combinação sem planos deve preservar o destino");
        TestSupport.igual(Arrays.asList("INICIAL"), destino, "Nenhum resultado deve ser acrescentado");

        PrintStream original = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try
        {
            System.setOut(new PrintStream(bytes, true, StandardCharsets.UTF_8.name()));
            TestSupport.verdadeiro(
                    ControleMultiThread.controlarMultiValidacaoLista(vazia, "ABC") == vazia,
                    "A validação impressa deve preservar a lista");
        }
        finally
        {
            System.setOut(original);
        }
        TestSupport.verdadeiro(
                bytes.toString(StandardCharsets.UTF_8.name()).contains("Anagramas 0"),
                "A validação vazia deve imprimir zero");
    }
}
