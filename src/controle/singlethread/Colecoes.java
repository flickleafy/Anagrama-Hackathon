/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
