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
 * @author xxx
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
