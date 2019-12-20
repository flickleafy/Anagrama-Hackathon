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