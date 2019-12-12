/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
