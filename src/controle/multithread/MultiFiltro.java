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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

/**
 *
 * @author Enzo Erbano 
 */

public class MultiFiltro implements Runnable
{
    private ArrayList<String> listaPalavras;
    private String stringEntrada;
    
    private ArrayList<String> eliminarPalavrasEmLista(ArrayList<String> listaPalavras, String stringEntrada)
    {
        HashSet<Character> mapaCaracterePalavraRef = mapearPalavra(stringEntrada);

        for (int j = 0; j < listaPalavras.size(); j++)
        {
            String palavra = listaPalavras.get(j);
            if (!checarCaracteresEmMapaRef(palavra, mapaCaracterePalavraRef))
            {
                listaPalavras.set(j, null);
            }
        }
        listaPalavras.removeIf(Objects::isNull);

        return listaPalavras;
    }

    private ArrayList<ArrayList> eliminarPalavrasEmColecoes(ArrayList<ArrayList> colecoesPalavras, String stringEntrada)
    {
        HashSet<Character> mapaCaracterePalavraRef = mapearPalavra(stringEntrada);

        for (int i = 0; i < colecoesPalavras.size(); i++)
        {
            ArrayList<String> tmpListaPalavras = colecoesPalavras.get(i);
            for (int j = 0; j < tmpListaPalavras.size(); j++)
            {
                String palavra = tmpListaPalavras.get(j);
                if (!checarCaracteresEmMapaRef(palavra, mapaCaracterePalavraRef))
                {
                    tmpListaPalavras.set(j, null);
                }
            }
            tmpListaPalavras.removeIf(Objects::isNull);
            if (tmpListaPalavras.isEmpty())
            {
                colecoesPalavras.set(i, null);
                //colecoesPalavras.set(i,tmpListaPalavras);
            }
        }
        colecoesPalavras.removeIf(Objects::isNull);
        return colecoesPalavras;
    }
    
    private HashSet<Character> mapearPalavra(String stringEntrada)
    {
        // Cria HashMap contendo caractere como chave e ocorrencias como valor
        HashSet<Character> mapaCaractere = new HashSet<>();
        // Converte string em vetor de caractere
        char[] strVetor = stringEntrada.toCharArray();
        // mapear caracteres em vetor
        for (char charactere : strVetor)
        {
            mapaCaractere.add(charactere);
        }
        return mapaCaractere;
    }

    private boolean checarCaracteresEmMapaRef(String palavra, HashSet<Character> mapaCaracterePalavraRef)
    {
        char[] strVetor = palavra.toCharArray();
        // mapear caracteres em array
        // checar se palavra da coleção contém todas as letras que a referencia
        for (char charactere : strVetor)
        {
            if (!mapaCaracterePalavraRef.contains(charactere))
            {
                return false;
            }
        }
        return true;
    }

    public MultiFiltro (ArrayList<String> listaPalavras, String stringEntrada)
    {
        this.listaPalavras = listaPalavras;
        this.stringEntrada = stringEntrada;
    }
        
    @Override
    public void run()
    {
        eliminarPalavrasEmLista(listaPalavras,stringEntrada);
    }

}
