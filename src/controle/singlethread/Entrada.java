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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.io.ConsoleEntrada;
import util.io.LerArquivo;

/**
 *
 * @author Enzo Erbano
 */

public class Entrada
{

    public static String lerEntradaConsole(ConsoleEntrada consoleManopla)
    {
        System.out.println("Digite uma palavra ou frase : ");
        String stringEntrada = "eugostodebanana";//consoleManopla.lerConsoleUmaLinha(); //"o rei de roma roeu a"
        stringEntrada = stringEntrada.toUpperCase();
        return stringEntrada;
    }

    public static ArrayList<String> lerArquivoConsole(ConsoleEntrada consoleManopla)
    {
        System.out.println("Digite o diret\u00f3rio e nome do dicionario : ");
        String diretorio =  "C:\\Users\\xxx\\Desktop\\Anagrama\\[dicionario]\\palavras.82eebac6.txt";//consoleManopla.lerConsoleUmaLinha();
        LerArquivo arquivo = new LerArquivo();
        ArrayList<String> listaString = new ArrayList<>();
        listaString = arquivo.lerArquivoLinhaPorLinha(diretorio);
        return listaString;
    }

    public static boolean validarString(String input)
    {
        String regex = "([A-Z ]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        boolean isMatched = matcher.matches();
        if (isMatched)
        {
            return false;
        }
        return true; // true para caractere indevido encontrado
    }
    
}
