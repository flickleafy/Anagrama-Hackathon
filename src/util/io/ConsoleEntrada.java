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
