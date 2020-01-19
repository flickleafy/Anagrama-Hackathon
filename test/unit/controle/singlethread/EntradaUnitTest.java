/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import testes.TestSupport;
import util.io.ConsoleEntrada;

/** Verifica isoladamente o contrato de {@link Entrada}. */
public final class EntradaUnitTest
{
    private EntradaUnitTest()
    {
    }

    public static void executar() throws Exception
    {
        PrintStream original = System.out;
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        try (ConsoleEntrada console = new ConsoleEntrada(new ByteArrayInputStream(
                "roma antiga\n".getBytes(StandardCharsets.UTF_8))))
        {
            System.setOut(new PrintStream(saida, true, StandardCharsets.UTF_8.name()));
            TestSupport.igual(
                    "ROMA ANTIGA",
                    Entrada.lerEntradaConsole(console),
                    "A entrada deve ser lida do console e convertida para maiúsculas");
        }
        finally
        {
            System.setOut(original);
        }

        TestSupport.falso(Entrada.validarString("ROMA ANTIGA"), "Letras e espaços devem ser aceitos");
        TestSupport.falso(Entrada.validarString("ROMA\tANTIGA"), "Todo espaço reconhecido deve ser aceito");
        TestSupport.verdadeiro(Entrada.validarString("ROMA-ANTIGA"), "Pontuação deve ser rejeitada");
        TestSupport.verdadeiro(Entrada.validarString("   "), "Uma entrada sem letras deve ser rejeitada");
        TestSupport.verdadeiro(Entrada.validarString(null), "Uma entrada nula deve ser rejeitada");
    }
}
