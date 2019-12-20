/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 *  
 * You are free to:
 * 
 * Share - copy and redistribute the material in any medium or format
 * Adapt - remix, transform, and build upon the material
 * 
 * Under the following terms:
 * 
 * Attribution - You must give appropriate credit, provide a link to the license, and indicate if
 * changes were made. You may do so in any reasonable manner, but not in any way that
 * suggests the licensor endorses you or your use.
 * NonCommercial - You may not use the material for commercial purposes.
 * ShareAlike - If you remix, transform, or build upon the material, you must distribute your
 * contributions under the same license as the original.
 * No additional restrictions - You may not apply legal terms or technological measures that
 * legally restrict others from doing anything the license permits.
 * 
 */

package controle.multithread;

import java.util.ArrayList;

/**
 *
 * @author Enzo Erbano 
 */

public class ListasParaThreads
{

    static void unirListas(ArrayList<String> listaPalavras, ArrayList<ArrayList> colecoesListas, int numeroDeThreads)
    {
        listaPalavras.clear();
        for (int i = 0; i < numeroDeThreads; i++)
        {
            listaPalavras.addAll(colecoesListas.get(i));
        }
    }

    // métodos de divisão de listas em pedaços iguais a quantidade de Threads
    static void dividirLista(ArrayList<String> listaPalavras, ArrayList<ArrayList> colecoesListas, int numeroDeThreads)
    {
        int pedaco = listaPalavras.size() / numeroDeThreads;
        int indiceinicial = 0;
        int indicefinal = pedaco;
        for (int i = 0; i < numeroDeThreads; i++)
        {
            ArrayList<String> tmp;
            if (i == numeroDeThreads - 1)
            {
                tmp = new ArrayList<>(listaPalavras.subList(indiceinicial, listaPalavras.size()));
            }
            else
            {
                tmp = new ArrayList<>(listaPalavras.subList(indiceinicial, indicefinal));
            }
            indiceinicial = indicefinal;
            indicefinal = indicefinal + pedaco;
            colecoesListas.add(tmp);
        }
    }
    
    
    static void dividirListaSetCombinacoes(ArrayList<ArrayList> listaSetCombinacoes, ArrayList<ArrayList> colecoesDeSets, int numeroDeThreads)
    {
        int pedaco = listaSetCombinacoes.size() / numeroDeThreads;
        int indiceinicial = 0;
        int indicefinal = pedaco;
        for (int i = 0; i < numeroDeThreads; i++)
        {
            ArrayList<ArrayList> tmp;
            if (i == numeroDeThreads - 1)
            {
                tmp = new ArrayList<>(listaSetCombinacoes.subList(indiceinicial, listaSetCombinacoes.size()));
            }
            else
            {
                tmp = new ArrayList<>(listaSetCombinacoes.subList(indiceinicial, indicefinal));
            }
            indiceinicial = indicefinal;
            indicefinal = indicefinal + pedaco;
            colecoesDeSets.add(tmp);
        }
    }
    
    /*static void unirColecoes(ArrayList<ArrayList> listasPalavras, ArrayList<ArrayList> colecoesListas, int numeroDeThreads)
    {
        listasPalavras.clear();
        for (int i = 0; i < numeroDeThreads; i++)
        {
            listasPalavras.addAll(colecoesListas.get(i));
        }
    }*/
}
