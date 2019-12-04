/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

/**
 *
 * @author Enzo Erbano
 */

import java.io.*;
import java.lang.String;
import java.util.ArrayList;

import java.util.Vector;

public class LerArquivo
{
    public ArrayList<String> lerArquivoLinhaPorLinha(String filename)
    {
        ArrayList<String> listaString = new ArrayList<String>();
        
        try
        {   // Open the file that is the first
            // command line parameter
            FileInputStream arquivoInpStream = new FileInputStream(filename);

            // Pegar referencia de objeto de DataInputStream
            DataInputStream dadosInpStream = new DataInputStream(arquivoInpStream);

            BufferedReader buffReader = new BufferedReader(new InputStreamReader(dadosInpStream));

            String strLinha;

            //Ler arquivo linha por linha
            while ((strLinha = buffReader.readLine()) != null)
            {        
                listaString.add(strLinha);
            }
            //Fechar stream de entrada
            dadosInpStream.close();
        }
        catch (Exception e)
        {   //Pegar e imprimir exceção se aparecer
            System.err.println("Erro: " + e.getMessage());
        }
        return listaString;
    }
}
