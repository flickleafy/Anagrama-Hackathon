/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
