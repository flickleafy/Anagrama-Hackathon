/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import java.util.Scanner;

/**
 *
 * @author Enzo Erbano
 */

public class ConsoleEntrada
{
    Scanner scanner;

    public ConsoleEntrada()
    {
        inicializar();
    }

    private void inicializar()
    {
        scanner = new Scanner(System.in);
    }

    public void destruir()
    {
        scanner.close();
    }
    
    public String lerConsoleUmaLinha()
    {
        String inputString = scanner.nextLine();
        return inputString;
    }
}
