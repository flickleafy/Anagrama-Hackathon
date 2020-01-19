/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link Filtro}. */
public final class FiltroUnitTest
{
    private FiltroUnitTest()
    {
    }

    public static void executar()
    {
        final ArrayList<String> palavras = new ArrayList<>(Arrays.asList(
                "AB", "CAB", "AAA", "ABCD", "", null));
        ArrayList<String> retorno = Filtro.eliminarPalavrasEmLista(palavras, "AABC");

        TestSupport.verdadeiro(retorno == palavras, "O filtro deve devolver a mesma lista");
        TestSupport.igual(
                Arrays.asList("AB", "CAB"),
                palavras,
                "Somente palavras formáveis com as quantidades disponíveis devem permanecer");

        TestSupport.lanca(
                IllegalArgumentException.class,
                new TestSupport.Acao()
                {
                    @Override
                    public void executar()
                    {
                        Filtro.eliminarPalavrasEmLista(null, "ABC");
                    }
                },
                "Uma lista nula deve ser rejeitada");
    }
}
