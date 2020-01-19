/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import controle.singlethread.Filtro;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Filtra uma partição independente do dicionário.
 *
 * @author Enzo Erbano
 */
final class MultiFiltro implements Callable<ArrayList<String>>
{
    private final ArrayList<String> listaPalavras;
    private final String stringEntrada;

    MultiFiltro(ArrayList<String> listaPalavras, String stringEntrada)
    {
        this.listaPalavras = listaPalavras;
        this.stringEntrada = stringEntrada;
    }

    @Override
    public ArrayList<String> call()
    {
        return Filtro.eliminarPalavrasEmLista(listaPalavras, stringEntrada);
    }
}
