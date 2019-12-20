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
import java.util.Objects;

/**
 *
 * @author Enzo Erbano 
 */

public class Colecoes
{
    // Separa uma lista de palavras simples em listas menores, 
    // cada lista com palavras com mesma quantidade de caracteres.
    
    public static ArrayList<ArrayList> divisaoEmColecoes(ArrayList<String> listaPalavras)
    {
        ArrayList<ArrayList> colecoesPalavras = new ArrayList<>();
        for (int i = 0; i < 25; i++)
        {
            ArrayList<String> list = new ArrayList<>();
            colecoesPalavras.add(list);
        }
        for (int i = 0; i < listaPalavras.size(); i++)
        {
            String palavra = listaPalavras.get(i);
            colecoesPalavras.get(palavra.length() - 1).add(palavra);
        }
        
        for (int i = 0; i < colecoesPalavras.size(); i++)
        {
            if (colecoesPalavras.get(i).isEmpty())
            {
                colecoesPalavras.set(i, null);
                //colecoesPalavras.set(i,tmpListaPalavras);
            }
        }
        colecoesPalavras.removeIf(Objects::isNull);
        return colecoesPalavras;
    }
    
}