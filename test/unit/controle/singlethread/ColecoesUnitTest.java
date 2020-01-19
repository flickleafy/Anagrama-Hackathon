/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.singlethread;

import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link Colecoes}. */
public final class ColecoesUnitTest
{
    private ColecoesUnitTest()
    {
    }

    public static void executar()
    {
        String longa = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCD";
        ArrayList<String> origem = new ArrayList<>(
                Arrays.asList(longa, "ABC", "AB", "BA", "", null, "A-"));
        ArrayList<String> copia = new ArrayList<>(origem);

        ArrayList<ArrayList<String>> colecoes = Colecoes.divisaoEmColecoes(origem);

        TestSupport.igual(3, colecoes.size(), "Devem existir três comprimentos válidos");
        TestSupport.igual(Arrays.asList("AB", "BA"), colecoes.get(0), "A menor coleção deve vir primeiro");
        TestSupport.igual("ABC", colecoes.get(1).get(0), "A ordenação deve usar o comprimento");
        TestSupport.igual(longa, colecoes.get(2).get(0), "Palavras maiores que 25 letras devem ser aceitas");
        TestSupport.igual(copia, origem, "O agrupamento não deve modificar a origem");

        TestSupport.lanca(
                IllegalArgumentException.class,
                new TestSupport.Acao()
                {
                    @Override
                    public void executar()
                    {
                        Colecoes.divisaoEmColecoes(null);
                    }
                },
                "Uma lista nula deve ser rejeitada");
    }
}
