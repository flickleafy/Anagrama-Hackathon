/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package util.io;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link LerArquivo}. */
public final class LerArquivoUnitTest
{
    private LerArquivoUnitTest()
    {
    }

    public static void executar() throws Exception
    {
        Path arquivo = Files.createTempFile("anagrama-ler-arquivo-", ".txt");
        try
        {
            Files.write(arquivo, Arrays.asList("ABC"), StandardCharsets.UTF_8);
            try (InputStream stream = new LerArquivo().lerArquivo(arquivo.toString()))
            {
                TestSupport.igual((int) 'A', stream.read(), "O arquivo existente deve ser aberto para leitura");
            }
        }
        finally
        {
            Files.deleteIfExists(arquivo);
        }

        PrintStream erroOriginal = System.err;
        ByteArrayOutputStream erro = new ByteArrayOutputStream();
        InputStream ausente;
        try
        {
            System.setErr(new PrintStream(erro, true, StandardCharsets.UTF_8.name()));
            ausente = new LerArquivo().lerArquivo(
                    arquivo.resolveSibling("arquivo-inexistente-anagrama.txt").toString());
        }
        finally
        {
            System.setErr(erroOriginal);
        }
        TestSupport.igual(null, ausente, "Um arquivo ausente deve produzir null");
        TestSupport.verdadeiro(
                erro.toString(StandardCharsets.UTF_8.name()).contains("Erro:"),
                "A falha de abertura deve ser registrada");
    }
}
