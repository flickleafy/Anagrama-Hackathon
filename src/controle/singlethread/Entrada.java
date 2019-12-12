/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        String stringEntrada = consoleManopla.lerConsoleUmaLinha(); //"o rei de roma roeu a"
        stringEntrada = stringEntrada.toUpperCase();
        return stringEntrada;
    }

    public static ArrayList<String> lerArquivoConsole(ConsoleEntrada consoleManopla)
    {
        System.out.println("Digite o diret\u00f3rio e nome do dicionario : ");
        String diretorio =  consoleManopla.lerConsoleUmaLinha();
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
