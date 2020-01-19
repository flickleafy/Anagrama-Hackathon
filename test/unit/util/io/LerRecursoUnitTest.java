/*
 * Copyright (C) 2021 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package util.io;

import controle.singlethread.Entrada;
import java.io.InputStream;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link LerRecurso}. */
public final class LerRecursoUnitTest
{
    private LerRecursoUnitTest()
    {
    }

    public static void executar() throws Exception
    {
        LerRecurso leitor = new LerRecurso();
        try (InputStream recurso = leitor.lerRecurso(Entrada.RECURSO_DICIONARIO))
        {
            TestSupport.verdadeiro(recurso != null, "O dicionário distribuído deve existir no classpath");
            TestSupport.verdadeiro(recurso.read() >= 0, "O recurso distribuído não deve estar vazio");
        }
        TestSupport.igual(null, leitor.lerRecurso(null), "Um nome nulo deve retornar null");
        TestSupport.igual(null, leitor.lerRecurso(""), "Um nome vazio deve retornar null");
        TestSupport.igual(
                null,
                leitor.lerRecurso("res/recurso-que-nao-existe.txt"),
                "Um recurso ausente deve retornar null");
    }
}
