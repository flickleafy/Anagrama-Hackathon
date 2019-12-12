/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        System.out.println("Total de combinações " + listaPalavrasCombinadas.size());
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