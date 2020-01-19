/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.cuda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link WordToVector}. */
public final class WordToVectorUnitTest
{
    private WordToVectorUnitTest()
    {
    }

    public static void executar()
    {
        ArrayList<ArrayList<String>> colecoes = new ArrayList<>();
        colecoes.add(new ArrayList<>(Arrays.asList("A", "A")));
        colecoes.add(new ArrayList<>(Arrays.asList("AB", "BA")));
        WordToVector vetorizador = new WordToVector(colecoes);

        Map<Integer, String> palavras = vetorizador.getWordToNumberMapping();
        TestSupport.igual(4, palavras.size(), "Cada posição deve receber um ID, inclusive duplicatas textuais");
        TestSupport.igual("A", palavras.get(0), "A numeração deve começar na primeira palavra");
        TestSupport.igual("BA", palavras.get(3), "A numeração deve preservar a ordem global");
        TestSupport.igual(2, vetorizador.getColecoesVetores().size(), "A divisão em coleções deve ser preservada");

        palavras.clear();
        TestSupport.igual(4, vetorizador.getWordToNumberMapping().size(), "O mapa retornado deve ser defensivo");
        int[] copia = vetorizador.getColecoesVetores().get(0);
        copia[0] = 999;
        TestSupport.igual(0, vetorizador.getColecoesVetores().get(0)[0], "Cada vetor retornado deve ser clonado");
    }
}
