/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
