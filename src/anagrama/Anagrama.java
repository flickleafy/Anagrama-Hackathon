/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anagrama;

/**
 *
 * @author xxx
 */
public class Anagrama
{
    public static boolean checarAnagrama(String str1, String str2)  
    {  
        // Caso tenha tamanhos diferentes, retornar falso  
        /*if (str1.length() != str2.length())  
        {  
            return false;  
        }*/
        
        // Calcula valor de XOR
        int acumulador = 0;  

        for (int i = 0; i < str1.length(); i++)  
        {  
            acumulador = acumulador ^ (int) str1.charAt(i);  
            acumulador = acumulador ^ (int) str2.charAt(i);  
        }  

        return acumulador == 0;  
    }  

}
