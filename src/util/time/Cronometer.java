/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package util.time;

import java.util.concurrent.TimeUnit;

/**
 * Mede intervalos sucessivos decorridos em milissegundos.
 *
 * @author Enzo Erbano
 */
public final class Cronometer
{
    private long ultimoInstante;
    private boolean iniciado;

    /** Inicia ou reinicia a medição a partir do instante atual. */
    public void start()
    {
        ultimoInstante = System.nanoTime();
        iniciado = true;
    }

    /**
     * Mede o intervalo desde o início ou desde a chamada anterior.
     *
     * @return intervalo decorrido, truncado para milissegundos inteiros
     * @throws IllegalStateException se {@link #start()} ainda não foi chamado
     */
    public long leap()
    {
        if (!iniciado)
        {
            throw new IllegalStateException("O cronômetro não foi iniciado");
        }

        long instanteAtual = System.nanoTime();
        long decorrido = instanteAtual - ultimoInstante;
        ultimoInstante = instanteAtual;
        return nanoTomilisec(decorrido);
    }

    /**
     * Converte nanossegundos para milissegundos inteiros.
     *
     * @param elapsedTime duração em nanossegundos
     * @return duração truncada em milissegundos
     */
    public static long nanoTomilisec(long elapsedTime)
    {
        return TimeUnit.NANOSECONDS.toMillis(elapsedTime);
    }
}
