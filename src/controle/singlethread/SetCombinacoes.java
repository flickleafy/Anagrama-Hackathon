/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.singlethread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Enzo Erbano 
 */

public class SetCombinacoes
{
    static ArrayList<ArrayList> listaDeSetCombinacoes = new ArrayList<>();
    static HashMap<Integer, Integer> mapaListasPalavrasEmArray = new HashMap<>();
    
    static void combinaRecursiva(ArrayList<Integer> numeros, int total, ArrayList<Integer> parcial)
    {
        int somatoria = 0;
        
        for (int x : parcial)
        {
            somatoria += x;
        }
        
        if (somatoria == total)
        {
            System.out.println("combinações(" + Arrays.toString(parcial.toArray()) + ")=" + total);
            listaDeSetCombinacoes.add(parcial);
        }
        if (somatoria >= total)
        {
            return;
        }
        
        for (int i = 0; i < numeros.size(); i++)
        {
            ArrayList<Integer> numerosRestantes = new ArrayList<>();
            int num = numeros.get(i);
            
            for (int j = i + 1; j < numeros.size(); j++)
            {
                numerosRestantes.add(numeros.get(j));
            }
            
            ArrayList<Integer> somaParcial = new ArrayList<>(parcial);
            somaParcial.add(num);
            combinaRecursiva(numerosRestantes, total, somaParcial);
        }
        
    }

    static void combina(ArrayList<Integer> numbers, int target)
    {
        combinaRecursiva(numbers, target, new ArrayList<>());        
    }

    public static SetCombinacoes criarSetDeCombinacoes(String stringEntrada, ArrayList<ArrayList> colecoesPalavras)
    {
        String stringTmp = stringEntrada.replaceAll("\\s+", "");
        int comprimento = stringTmp.length();
        SetCombinacoes setCombinacoesManopla = new SetCombinacoes(comprimento, colecoesPalavras);
        return setCombinacoesManopla;
    }

    public SetCombinacoes(int comprimentoExpressao, ArrayList<ArrayList> colecoesPalavras)
    {
        ArrayList<Integer> comprimentosDisponiveis = new ArrayList<>();
                
        for (int i=0;i < colecoesPalavras.size(); i++)
        {
            ArrayList<String> tmpStringArray = colecoesPalavras.get(i);
            int length = tmpStringArray.get(0).length();
            mapaListasPalavrasEmArray.put(length, i);
            comprimentosDisponiveis.add(length);
        }
        
        combina(comprimentosDisponiveis, comprimentoExpressao);
    }

    public ArrayList<ArrayList> getListaDeSetCombinacoes()
    {
        return listaDeSetCombinacoes;
    }

    public HashMap<Integer, Integer> getMapaListasPalavrasEmArray()
    {
        return mapaListasPalavrasEmArray;
    }
    
    
}