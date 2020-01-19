/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import controle.singlethread.Validacao;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Conta anagramas válidos em uma partição independente de candidatos.
 *
 * @author Enzo Erbano
 */
final class MultiValidacao implements Callable<Long>
{
    private final ArrayList<String> listaPalavras;
    private final String stringEntrada;

    MultiValidacao(ArrayList<String> listaPalavras, String stringEntrada)
    {
        this.listaPalavras = listaPalavras;
        this.stringEntrada = stringEntrada;
    }

    @Override
    public Long call()
    {
        return Validacao.contarAnagramasEmListas(listaPalavras, stringEntrada);
    }
}
