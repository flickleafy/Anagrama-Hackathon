/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package integration;

/** Ponto de entrada da suíte de integração. */
public final class IntegrationTests
{
    private IntegrationTests()
    {
    }

    public static int executarTudo() throws Exception
    {
        int executados = 0;
        FluxoSingleThreadIntegrationTest.executar();
        executados++;
        EntradaArquivoIntegrationTest.executar();
        executados++;
        FluxoMultithreadIntegrationTest.executar();
        executados++;
        CombinadorArvoreIntegrationTest.executar();
        executados++;
        CombinadorCudaIntegrationTest.executar();
        executados++;
        AplicacaoCliIntegrationTest.executar();
        executados++;
        return executados;
    }

    public static void main(String[] args) throws Exception
    {
        int executados = executarTudo();
        System.out.println("OK - " + executados + " testes de integração executados");
    }
}
