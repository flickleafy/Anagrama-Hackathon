/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.multithread;

import java.util.ArrayList;
import java.util.HashMap;
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
        HashMap<Character, Integer> mapaCaracterePalavraRef = mapearPalavra(stringEntrada);

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
        HashMap<Character, Integer> mapaCaracterePalavraRef = mapearPalavra(stringEntrada);

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

    private HashMap<Character, Integer> mapearPalavra(String stringEntrada)
    {
        // Cria HashMap contendo caractere como chave e ocorrencias como valor
        HashMap<Character, Integer> mapaCaractere = new HashMap<Character, Integer>();
        // Converte string em vetor de caractere
        char[] strVetor = stringEntrada.toCharArray();
        // mapear caracteres em vetor
        for (char charactere : strVetor)
        {
            if (mapaCaractere.containsKey(charactere))
            {
                // Se presente em Hashmap incrementa 1
                //mapaCaractere.put(charactere, mapaCaractere.get(charactere) + 1);
            }
            else
            {
                // Não presente, adiciona caractere com contagem 1
                mapaCaractere.put(charactere, 1);
            }
        }
        return mapaCaractere;
    }

    private boolean checarCaracteresEmMapaRef(String palavra, HashMap<Character, Integer> mapaCaracterePalavraRef)
    {
        char[] strVetor = palavra.toCharArray();
        // mapear caracteres em array
        int contem = 0;
        int naocontem = 0;
        // checar se palavra da coleção contém todas as letras que a referencia
        for (char charactere : strVetor)
        {
            if (mapaCaracterePalavraRef.containsKey(charactere))
            {
                contem++;
            }
            else
            {
                //naocontem++;
                return false;
            }
        }
        /*if (naocontem > 0)
        {
            return false;
        }*/
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
