/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.cuda;

import controle.singlethread.SetCombinacoes;
import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link CudaCombinador}. */
public final class CudaCombinadorUnitTest
{
    private CudaCombinadorUnitTest()
    {
    }

    public static void executar()
    {
        ArrayList<ArrayList<String>> colecoes = new ArrayList<>();
        colecoes.add(new ArrayList<>(Arrays.asList("A", "B")));
        colecoes.add(new ArrayList<>(Arrays.asList("AB", "BA")));
        SetCombinacoes planos = new SetCombinacoes(2, colecoes);

        ArrayList<String> resultado = new ArrayList<>(Arrays.asList("INICIAL"));
        CudaCombinador combinador = new CudaCombinador(
                resultado, colecoes, planos, false);
        TestSupport.igual(
                Arrays.asList("INICIAL", "AA", "AB", "BB", "AB", "BA"),
                resultado,
                "O modo CPU deve materializar a enumeração canônica completa");
        TestSupport.igual(
                CudaCombinador.Backend.CPU,
                combinador.getBackendUsado(),
                "Desabilitar CUDA deve forçar a CPU");
        TestSupport.igual(
                null,
                combinador.getDetalheFalhaCuda(),
                "O modo forçado em CPU não deve registrar uma falha CUDA");

        ArrayList<String> vazio = new ArrayList<>();
        new CudaCombinador(
                vazio,
                new ArrayList<ArrayList<String>>(),
                new SetCombinacoes(0, new ArrayList<ArrayList<String>>()),
                false);
        TestSupport.verdadeiro(vazio.isEmpty(), "A ausência de planos deve produzir uma saída vazia");
    }
}
