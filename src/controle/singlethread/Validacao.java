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

import anagrama.Anagrama;
import java.util.ArrayList;

/**
 *
 * @author Enzo Erbano 
 */

public class Validacao
{

    public static void validarAnagramasEmColecoes(ArrayList<ArrayList> colecoesPalavrasCombinadas, String stringEntrada)
    {        
        String stringTmp = stringEntrada.replaceAll("\\s+", "");
        for (int i = 0; i < colecoesPalavrasCombinadas.size(); i++)
        {
            ArrayList<String> tmpListaPalavras = colecoesPalavrasCombinadas.get(i);
            for (int j = 0; j < tmpListaPalavras.size(); j++)
            {
                String palavra = tmpListaPalavras.get(j);
                if (Anagrama.checarAnagrama(stringTmp, palavra))
                {
                    System.out.println("A palavra \u00e9 um anagrama : " + palavra);
                }
            }
        }
    }

    /*public static void validarAnagramasEmListas(ArrayList<String> listaPalavrasCombinadas, String stringEntrada)
    {
        String stringTmp = stringEntrada.replaceAll("\\s+", "");        
        ArrayList<String> arrayAnagramas = new ArrayList<>();
        
        for (int j = 0; j < listaPalavrasCombinadas.size(); j++)
        {
            String palavra = listaPalavrasCombinadas.get(j);
            if (Anagrama.checarAnagrama(stringTmp, palavra))
            {
                arrayAnagramas.add(palavra+"\n");                        
                //System.out.println("A palavra \u00e9 um anagrama : " + palavra);
            }
        }        
        System.out.println("Anagramas "+ arrayAnagramas.size());
        System.out.println(arrayAnagramas.toString());
    }*/
    
    public static void validarAnagramasEmListas(ArrayList<String> listaPalavrasCombinadas, String stringEntrada)
    {
        String stringTmp = stringEntrada.replaceAll("\\s+", "");
        long c= 0;
        for (int j = 0; j < listaPalavrasCombinadas.size(); j++)
        {
            String palavra = listaPalavrasCombinadas.get(j);
            if (Anagrama.checarAnagrama(stringTmp, palavra))
            {
                c++;
                //System.out.println("A palavra \u00e9 um anagrama : " + palavra);
            }
        }
        System.out.println("Anagramas "+c);
    }
    
}
