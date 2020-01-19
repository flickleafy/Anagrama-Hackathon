/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link MultiValidacao}. */
public final class MultiValidacaoUnitTest
{
    private MultiValidacaoUnitTest()
    {
    }

    public static void executar()
    {
        MultiValidacao tarefa = new MultiValidacao(
                new ArrayList<>(Arrays.asList("ABC", "CBA", "ABB")),
                "ABC");
        TestSupport.igual(2L, tarefa.call(), "A tarefa deve contar somente os anagramas de sua partição");
    }
}
