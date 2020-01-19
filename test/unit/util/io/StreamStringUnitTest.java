/*
 * Copyright (C) 2021 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package util.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link StreamString}. */
public final class StreamStringUnitTest
{
    private StreamStringUnitTest()
    {
    }

    public static void executar()
    {
        StreamString leitor = new StreamString();
        TestSupport.igual(
                Arrays.asList("AÇÃO", "ROMA"),
                leitor.streamParaString(new ByteArrayInputStream(
                        "AÇÃO\nROMA\n".getBytes(StandardCharsets.UTF_8))),
                "As linhas devem ser decodificadas em UTF-8");
        TestSupport.igual(
                Collections.emptyList(),
                leitor.streamParaString(new ByteArrayInputStream(new byte[0])),
                "Um fluxo vazio deve produzir uma lista vazia");

        TestSupport.lanca(
                IllegalArgumentException.class,
                new TestSupport.Acao()
                {
                    @Override
                    public void executar()
                    {
                        new StreamString().streamParaString(null);
                    }
                },
                "Um fluxo nulo deve ser rejeitado");

        final FluxoFalho fluxoFalho = new FluxoFalho();
        TestSupport.lanca(
                IllegalStateException.class,
                new TestSupport.Acao()
                {
                    @Override
                    public void executar()
                    {
                        new StreamString().streamParaString(fluxoFalho);
                    }
                },
                "Falhas de leitura devem ser propagadas pelo contrato da classe");
        TestSupport.verdadeiro(fluxoFalho.fechado, "O fluxo deve ser fechado mesmo depois de uma falha");
    }

    private static final class FluxoFalho extends InputStream
    {
        private boolean fechado;

        @Override
        public int read() throws IOException
        {
            throw new IOException("falha controlada");
        }

        @Override
        public void close()
        {
            fechado = true;
        }
    }
}
