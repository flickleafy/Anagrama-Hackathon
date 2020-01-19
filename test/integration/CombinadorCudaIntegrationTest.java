/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package integration;

import controle.cuda.CudaCombinador;
import controle.singlethread.Colecoes;
import controle.singlethread.Combinador;
import controle.singlethread.SetCombinacoes;
import java.util.ArrayList;
import java.util.Arrays;
import testes.TestSupport;

/** Integra o adaptador CUDA, incluindo sua contingência, ao fluxo padrão. */
public final class CombinadorCudaIntegrationTest
{
    private CombinadorCudaIntegrationTest()
    {
    }

    public static void executar()
    {
        ArrayList<ArrayList<String>> colecoes = Colecoes.divisaoEmColecoes(
                new ArrayList<>(Arrays.asList(
                        "A", "B", "C", "AB", "BC", "ABC")));
        SetCombinacoes planos = SetCombinacoes.criarSetDeCombinacoes("ABC", colecoes);
        ArrayList<String> referencia = new ArrayList<>();
        Combinador.combinadorPalavras(referencia, colecoes, "ABC", planos);

        ArrayList<String> forcarCpu = new ArrayList<>();
        CudaCombinador cpu = new CudaCombinador(forcarCpu, colecoes, planos, false);
        TestSupport.igual(referencia, forcarCpu, "A contingência numérica deve ser equivalente");
        TestSupport.igual(CudaCombinador.Backend.CPU, cpu.getBackendUsado(), "O modo forçado deve usar CPU");

        ArrayList<String> automatico = new ArrayList<>();
        CudaCombinador tentativa = new CudaCombinador(
                automatico, colecoes, planos, true);
        TestSupport.igual(
                referencia,
                automatico,
                "CUDA ou sua contingência deve preservar todas as dimensões dos planos");
        if (tentativa.getBackendUsado() == CudaCombinador.Backend.CPU)
        {
            TestSupport.verdadeiro(
                    tentativa.getDetalheFalhaCuda() != null,
                    "Uma tentativa CUDA malsucedida deve explicar a contingência");
        }
    }
}
