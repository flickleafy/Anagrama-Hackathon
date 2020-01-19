/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package util.io;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link ConsoleEntrada}. */
public final class ConsoleEntradaUnitTest
{
    private ConsoleEntradaUnitTest()
    {
    }

    public static void executar()
    {
        ConsoleEntrada console = new ConsoleEntrada(new ByteArrayInputStream(
                "ação\nsegunda linha\n".getBytes(StandardCharsets.UTF_8)));
        TestSupport.igual("ação", console.lerConsoleUmaLinha(), "A leitura deve preservar UTF-8");
        TestSupport.igual("segunda linha", console.lerConsoleUmaLinha(), "Cada chamada deve consumir uma linha");
        console.destruir();
        TestSupport.lanca(
                IllegalStateException.class,
                new TestSupport.Acao()
                {
                    @Override
                    public void executar()
                    {
                        console.lerConsoleUmaLinha();
                    }
                },
                "O alias destruir deve fechar o leitor");

        TestSupport.lanca(
                IllegalArgumentException.class,
                new TestSupport.Acao()
                {
                    @Override
                    public void executar()
                    {
                        new ConsoleEntrada(null);
                    }
                },
                "Um fluxo nulo deve ser rejeitado");
    }
}
