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


/*          switch (setCombinacoes.size())
            {
                case 1:
                    // 1 palavra
                    listaPalavrasCombinadas.addAll( combinadorColecaoUmaPalavra(colecoesPalavras, setCombinacoes, mapaListasPalavrasEmArray) );
                    break;
                case 2:
                    // 2 palavras
                    listaPalavrasCombinadas.addAll( combinadorColecaoDuasPalavras(colecoesPalavras, setCombinacoes, mapaListasPalavrasEmArray) );
                    break;
                case 3:
                    // 3 palavras
                    listaPalavrasCombinadas.addAll( combinadorColecaoTresPalavras(colecoesPalavras, setCombinacoes, mapaListasPalavrasEmArray) );
                    break;
                default:                    
                    break;
            }      


    private static ArrayList<String> combinadorColecaoUmaPalavra(ArrayList<ArrayList> colecoesPalavras, ArrayList<Integer> setCombinacoes, HashMap<Integer, Integer> mapaListasPalavrasEmArray)
    {
        ArrayList<String> listaPalavrasCombinadas = new ArrayList<>();
        
        int tamanhoPalavra1 = setCombinacoes.get(0);
        int posicaoLista1 = mapaListasPalavrasEmArray.get(tamanhoPalavra1);
        ArrayList<String> tmpListaPalavras1 = colecoesPalavras.get(posicaoLista1);
        
        for (int i = 0; i < tmpListaPalavras1.size(); i++)
        {
            listaPalavrasCombinadas.add(tmpListaPalavras1.get(i));
        }
        
        return listaPalavrasCombinadas;
    }

    private static ArrayList<String> combinadorColecaoDuasPalavras(ArrayList<ArrayList> colecoesPalavras, ArrayList<Integer> setCombinacoes, HashMap<Integer, Integer> mapaListasPalavrasEmArray)
    {
        ArrayList<String> listaPalavrasCombinadas = new ArrayList<>();
        int tamanhoPalavra1 = setCombinacoes.get(0);
        int tamanhoPalavra2 = setCombinacoes.get(1);
        int posicaoLista1 = mapaListasPalavrasEmArray.get(tamanhoPalavra1);
        int posicaoLista2 = mapaListasPalavrasEmArray.get(tamanhoPalavra2);
        ArrayList<String> tmpListaPalavras1 = colecoesPalavras.get(posicaoLista1);
        ArrayList<String> tmpListaPalavras2 = colecoesPalavras.get(posicaoLista2);
        for (int i = 0; i < tmpListaPalavras1.size(); i++)
        {
            for (int j = 0; j < tmpListaPalavras2.size(); j++)
            {
                String palavraCombinada = tmpListaPalavras1.get(i) + tmpListaPalavras2.get(j);
                listaPalavrasCombinadas.add(palavraCombinada);
            }
        }
        return listaPalavrasCombinadas;
    }
    
    private static ArrayList<String> combinadorColecaoTresPalavras(ArrayList<ArrayList> colecoesPalavras, ArrayList<Integer> setCombinacoes, HashMap<Integer, Integer> mapaListasPalavrasEmArray)
    {
        ArrayList<String> listaPalavrasCombinadas = new ArrayList<>();
        int tamanhoPalavra1 = setCombinacoes.get(0);
        int tamanhoPalavra2 = setCombinacoes.get(1);
        int tamanhoPalavra3 = setCombinacoes.get(2);
        int posicaoLista1 = mapaListasPalavrasEmArray.get(tamanhoPalavra1);
        int posicaoLista2 = mapaListasPalavrasEmArray.get(tamanhoPalavra2);
        int posicaoLista3 = mapaListasPalavrasEmArray.get(tamanhoPalavra3);
        ArrayList<String> tmpListaPalavras1 = colecoesPalavras.get(posicaoLista1);
        ArrayList<String> tmpListaPalavras2 = colecoesPalavras.get(posicaoLista2);
        ArrayList<String> tmpListaPalavras3 = colecoesPalavras.get(posicaoLista3);
        for (int i = 0; i < tmpListaPalavras1.size(); i++)
        {
            for (int j = 0; j < tmpListaPalavras2.size(); j++)
            {
                for (int k = 0; k < tmpListaPalavras3.size(); k++)
                {
                    String palavraCombinada = tmpListaPalavras1.get(i) + tmpListaPalavras2.get(j) + tmpListaPalavras3.get(k);
                    listaPalavrasCombinadas.add(palavraCombinada);
                }
            }
        }
        return listaPalavrasCombinadas;
    }*/

