/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.singlethread;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Enzo Erbano 
 */

public class Combinador
{

    public static void combinadorPalavras(ArrayList<String> listaPalavrasCombinadas,ArrayList<ArrayList> colecoesPalavras, String stringEntrada, SetCombinacoes setCombinacoesManopla)
    {
        ArrayList<ArrayList> listaDeSetCombinacoes = setCombinacoesManopla.getListaDeSetCombinacoes();        
        HashMap<Integer, Integer> mapaListasPalavrasEmArray = setCombinacoesManopla.getMapaListasPalavrasEmArray();
        
        //ArrayList<String> listaPalavrasCombinadas = new ArrayList<>();
        
        for (int i = 0; i < listaDeSetCombinacoes.size(); i++)
        {
            ArrayList<Integer> setCombinacoes = listaDeSetCombinacoes.get(i);
            
            listaPalavrasCombinadas.addAll( combinadorRecursivo(colecoesPalavras, setCombinacoes, mapaListasPalavrasEmArray) );
            
        }
        System.out.println("Total de combinações " + listaPalavrasCombinadas.size());
        
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
    
}
