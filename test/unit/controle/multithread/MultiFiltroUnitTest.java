/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link MultiFiltro}. */
public final class MultiFiltroUnitTest
{
    private MultiFiltroUnitTest()
    {
    }

    public static void executar()
    {
        ArrayList<String> parte = new ArrayList<>(Arrays.asList("AB", "AAA", "CAB"));
        ArrayList<String> resultado = new MultiFiltro(parte, "AABC").call();
        TestSupport.verdadeiro(resultado == parte, "A tarefa deve devolver sua própria partição");
        TestSupport.igual(Arrays.asList("AB", "CAB"), resultado, "A tarefa deve aplicar o filtro sequencial");
    }
}
