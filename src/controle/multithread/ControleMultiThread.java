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

public class ControleMultiThread
{
    
    public static ArrayList<String> controlarMultiFiltroLista(ArrayList<String> listaPalavras, String stringEntrada)
    {
        int numeroDeThreads = ExecucaoMultiThread.definirNumeroDeThreads(listaPalavras.size());
        
        ArrayList<ArrayList> colecoesListas = new ArrayList<>();
        
        ListasParaThreads.dividirLista(listaPalavras, colecoesListas, numeroDeThreads);
        
        ExecucaoMultiThread.executarMultiFiltro(colecoesListas, stringEntrada, numeroDeThreads);
        
        ListasParaThreads.unirListas(listaPalavras, colecoesListas, numeroDeThreads);
        
        return listaPalavras;
    }

    public static ArrayList<String> controlarMultiValidacaoLista(ArrayList<String> listaPalavras, String stringEntrada)
    {
        int numeroDeThreads = ExecucaoMultiThread.definirNumeroDeThreads(listaPalavras.size());
        
        ArrayList<ArrayList> colecoesListas = new ArrayList<>();
        
        ListasParaThreads.dividirLista(listaPalavras, colecoesListas, numeroDeThreads);
        
        ExecucaoMultiThread.executarMultiValidacao(colecoesListas, stringEntrada, numeroDeThreads);
                        
        ListasParaThreads.unirListas(listaPalavras, colecoesListas, numeroDeThreads);
        
        return listaPalavras;
    }
    
    public static ArrayList<String> controlarMultiCombinacoesLista(ArrayList<String> listaPalavrasCombinadas, ArrayList<ArrayList> colecoesPalavras, String stringEntrada, SetCombinacoes setCombinacoesManopla)
    {
        int numeroDeThreads = ExecucaoMultiThread.definirNumeroDeThreads(setCombinacoesManopla.getListaDeSetCombinacoes().size());
        
        ArrayList<ArrayList> colecoesDeSets = new ArrayList<>();
        
        ListasParaThreads.dividirListaSetCombinacoes(setCombinacoesManopla.getListaDeSetCombinacoes(), colecoesDeSets, numeroDeThreads);
        
        ExecucaoMultiThread.executarMultiCombinacoes(listaPalavrasCombinadas, colecoesPalavras, stringEntrada, setCombinacoesManopla, colecoesDeSets, numeroDeThreads);
                        
        //ListasParaThreads.unirListas(listaPalavrasCombinadas, colecoesListas, numeroDeThreads);
        
        return listaPalavrasCombinadas;
    }
    
    
}
