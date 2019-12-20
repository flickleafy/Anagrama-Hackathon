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
