/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package util.time;

import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link Cronometer}. */
public final class CronometerUnitTest
{
    private CronometerUnitTest()
    {
    }

    public static void executar()
    {
        TestSupport.igual(1L, Cronometer.nanoTomilisec(1_000_000L), "Um milhão de nanos equivale a um milissegundo");
        TestSupport.igual(0L, Cronometer.nanoTomilisec(999_999L), "A conversão deve truncar frações de milissegundo");

        final Cronometer cronometro = new Cronometer();
        TestSupport.lanca(
                IllegalStateException.class,
                new TestSupport.Acao()
                {
                    @Override
                    public void executar()
                    {
                        cronometro.leap();
                    }
                },
                "leap deve exigir uma chamada anterior a start");
        cronometro.start();
        TestSupport.verdadeiro(cronometro.leap() >= 0L, "O primeiro intervalo não pode ser negativo");
        TestSupport.verdadeiro(cronometro.leap() >= 0L, "Intervalos sucessivos não podem ser negativos");
    }
}
