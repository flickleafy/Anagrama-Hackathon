/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.time;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Enzo Erbano
 */
public class Timer
{

    public static long nanoTomilisec(long elapsedTime)
    {
        return TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
    }
    
}
