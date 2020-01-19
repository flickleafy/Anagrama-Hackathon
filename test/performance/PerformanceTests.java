/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package performance;

/** Ponto de entrada da suíte de desempenho separada dos testes rotineiros. */
public final class PerformanceTests
{
    private PerformanceTests()
    {
    }

    public static void main(String[] args)
    {
        CombinadoresPerformanceTest.executar();
        boolean cudaExecutado = CudaNativoPerformanceTest.executarSeSolicitado();
        System.out.println(
                "OK - benchmarks de CPU executados; CUDA nativo "
                + (cudaExecutado ? "executado" : "ignorado"));
    }
}
