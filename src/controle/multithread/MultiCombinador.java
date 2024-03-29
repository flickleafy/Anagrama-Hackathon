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
import java.util.HashMap;

/**
 *
 * @author Enzo Erbano 
 */

public class MultiCombinador implements Runnable
{
    private ArrayList<String> listaPalavrasCombinadas;
    private ArrayList<ArrayList> colecoesPalavras;
    private String stringEntrada;
    private SetCombinacoes setCombinacoesManopla;
    private ArrayList<ArrayList> colecoesDeSets;
    
    public static void combinadorPalavras(ArrayList<String> listaPalavrasCombinadas, ArrayList<ArrayList> colecoesPalavras, String stringEntrada, SetCombinacoes setCombinacoesManopla, ArrayList<ArrayList> colecoesDeSets)
    {
        ArrayList<ArrayList> listaDeSetCombinacoes = colecoesDeSets;//setCombinacoesManopla.getListaDeSetCombinacoes();        
        HashMap<Integer, Integer> mapaListasPalavrasEmArray = setCombinacoesManopla.getMapaListasPalavrasEmArray();
        
        //ArrayList<String> listaPalavrasCombinadas = new ArrayList<>();
        
        for (int i = 0; i < listaDeSetCombinacoes.size(); i++)
        {
            ArrayList<Integer> setCombinacoes = listaDeSetCombinacoes.get(i);
            
            listaPalavrasCombinadas.addAll( combinadorRecursivo(colecoesPalavras, setCombinacoes, mapaListasPalavrasEmArray) );
            
        }
        
        //return listaPalavrasCombinadas;
    }

    
    private static ArrayList<String> combinadorRecursivo(ArrayList<ArrayList> colecoesPalavras, ArrayList<Integer> setCombinacoes, HashMap<Integer, Integer> mapaListasPalavrasEmArray) 
    {
        ArrayList<String> listaPalavrasCombinadas = new ArrayList<>();
        
        int tamanhoPalavra = setCombinacoes.get(0);
        int posicaoRealLista = mapaListasPalavrasEmArray.get(tamanhoPalavra);
        ArrayList<String> tmpListaPalavras1 = colecoesPalavras.get(posicaoRealLista);

        if(setCombinacoes.size() > 1)
        {            
            setCombinacoes.remove(0);
            
            ArrayList<String> tmpListaPalavras2 = combinadorRecursivo(colecoesPalavras,setCombinacoes,mapaListasPalavrasEmArray);
            for (int i = 0; i < tmpListaPalavras1.size(); i++)
            {
                for (int j = 0; j < tmpListaPalavras2.size(); j++)
                {
                    listaPalavrasCombinadas.add(tmpListaPalavras1.get(i) + tmpListaPalavras2.get(j) );
                }
            }            
        }
        else
        {
            return tmpListaPalavras1;         
        }
        return listaPalavrasCombinadas;
    }
    
    MultiCombinador(ArrayList<String> listaPalavrasCombinadas, ArrayList<ArrayList> colecoesPalavras, String stringEntrada,SetCombinacoes setCombinacoesManopla,ArrayList<ArrayList> colecoesDeSets)
    {
        this.colecoesPalavras = colecoesPalavras;
        this.stringEntrada = stringEntrada;
        this.setCombinacoesManopla = setCombinacoesManopla;
        this.listaPalavrasCombinadas = listaPalavrasCombinadas;
        this.colecoesDeSets = colecoesDeSets;
    }

    @Override
    public void run()
    {
        combinadorPalavras(listaPalavrasCombinadas, colecoesPalavras, stringEntrada, setCombinacoesManopla,colecoesDeSets);
    }
    
}
