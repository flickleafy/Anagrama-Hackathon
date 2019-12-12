/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.multithread;

import anagrama.Anagrama;
import java.util.ArrayList;

/**
 *
 * @author Enzo Erbano
 */

public class MultiValidacao implements Runnable
{
    private ArrayList<String> listaPalavras;
    private String stringEntrada;
    
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
    

    public MultiValidacao (ArrayList<String> listaPalavras, String stringEntrada)
    {
        this.listaPalavras = listaPalavras;
        this.stringEntrada = stringEntrada;
    }
        
    @Override
    public void run()
    {
        validarAnagramasEmListas(listaPalavras,stringEntrada);
    }
    
}
