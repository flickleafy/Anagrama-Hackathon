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

import controle.singlethread.SetCombinacoes;
import java.util.ArrayList;

/**
 *
 * @author Enzo Erbano 
 */

public class ExecucaoMultiThread
{

    static void executarMultiFiltro(ArrayList<ArrayList> colecoesListas, String stringEntrada, int numeroDeThreads)
    {
        ArrayList<Thread> threadsArray = new ArrayList<>();
        for (int i = 0; i < numeroDeThreads; i++)
        {
            Thread thread = new Thread(new MultiFiltro(colecoesListas.get(i), stringEntrada));
            threadsArray.add(thread);
            thread.start();
        }
        juntarThreads(threadsArray);
    }
    
    static void executarMultiCombinacoes(ArrayList<String> listaPalavrasCombinadas,ArrayList<ArrayList> colecoesListas, String stringEntrada,SetCombinacoes setCombinacoesManopla,ArrayList<ArrayList> colecoesDeSets ,int numeroDeThreads)
    {
        ArrayList<ArrayList> listasPalavrasPorThread = new ArrayList<>();
        ArrayList<Thread> threadsArray = new ArrayList<>();
        
        for (int i = 0; i < numeroDeThreads; i++)
        {
            ArrayList<String> listaPalavrasComb = new ArrayList<>();
            Thread thread = new Thread(new MultiCombinador(listaPalavrasComb,colecoesListas, stringEntrada,setCombinacoesManopla, colecoesDeSets.get(i)));
            threadsArray.add(thread);
            thread.start();
            listasPalavrasPorThread.add(listaPalavrasComb);
        }
        
        juntarThreads(threadsArray);
        
        for (int i = 0; i < numeroDeThreads; i++)
        {
            listaPalavrasCombinadas.addAll(listasPalavrasPorThread.get(i));
        }
        System.out.println("\nTotal de combinações " + listaPalavrasCombinadas.size());
    }    
    
    static void executarMultiValidacao(ArrayList<ArrayList> colecoesListas, String stringEntrada, int numeroDeThreads)
    {
        ArrayList<Thread> threadsArray = new ArrayList<>();
        for (int i = 0; i < numeroDeThreads; i++)
        {
            Thread thread = new Thread(new MultiValidacao(colecoesListas.get(i), stringEntrada));
            threadsArray.add(thread);
            thread.start();
        }
        juntarThreads(threadsArray);
    }

    

    
    // métodos de controle de Threads
    private static void juntarThreads(ArrayList<Thread> threadsArray)
    {
        // todas as threads vão executar em paralelo, enquanto o thread principal espera
        for (Thread thread : threadsArray)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
            }
        }
    }
    
    public static int definirNumeroDeThreads(int tamanhoArrayList)
    {
        int numeroDeThreads = Runtime.getRuntime().availableProcessors();
        if (tamanhoArrayList < numeroDeThreads*2)
        {
            numeroDeThreads = tamanhoArrayList;
        }
        //else if (tamanhoArrayList < numeroDeThreads*2 )
        //{
            //numeroDeThreads = tamanhoArrayList/2;
        //}
        return numeroDeThreads;
    }

    

    
}