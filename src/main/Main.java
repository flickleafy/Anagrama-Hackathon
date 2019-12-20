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

package main;

import controle.singlethread.Colecoes;
import controle.singlethread.Entrada;
import util.io.ConsoleEntrada;
import java.util.ArrayList;
import controle.multithread.ControleMultiThread;
import controle.singlethread.Combinador;
import controle.singlethread.Filtro;
import controle.singlethread.SetCombinacoes;
import controle.singlethread.Validacao;
import util.time.Timer;



/**
 *
 * @author Enzo Erbano
 */

public class Main
{
   
    public static void main(String[] args)
    {
        // Time
        long startTime;
        long elapsedTimeFiltro, elapsedTimeSetCombina, elapsedTimeCombina, elapsedTimeValida; 
    
        ConsoleEntrada consoleManopla = new ConsoleEntrada();
        
        // Leitura de entrada
        
        String stringEntrada = Entrada.lerEntradaConsole(consoleManopla); //single-thread

        if (Entrada.validarString(stringEntrada))
        {
            // termina execução por caractere indevido
            consoleManopla.destruir();
            return;
        }

        ArrayList<String> listaPalavras = Entrada.lerArquivoConsole(consoleManopla); //single-thread

        // Filtro de palavras
        
            startTime = System.nanoTime(); 
        listaPalavras = Filtro.eliminarPalavrasEmLista(listaPalavras, stringEntrada); //single-thread        
        //listaPalavras = ControleMultiThread.controlarMultiFiltroLista(listaPalavras, stringEntrada);  //multi-thread
            elapsedTimeFiltro = System.nanoTime() - startTime;
            
        // Divisão em coleçoes por tamanho de palavra
        
        ArrayList<ArrayList> colecoesPalavras = Colecoes.divisaoEmColecoes(listaPalavras); //single-thread
              
        // Criação de set de combinações
        
            startTime = System.nanoTime();
        SetCombinacoes setCombinacoesManopla = SetCombinacoes.criarSetDeCombinacoes(stringEntrada, colecoesPalavras); //single-thread
            elapsedTimeSetCombina = System.nanoTime() - startTime;
            
        // Execução de combinações
                
        ArrayList<String> listaPalavrasCombinadas = new ArrayList<>();        
        
            startTime = System.nanoTime();
        //Combinador.combinadorPalavras(listaPalavrasCombinadas, colecoesPalavras, stringEntrada, setCombinacoesManopla); //single-thread        
        ControleMultiThread.controlarMultiCombinacoesLista(listaPalavrasCombinadas,colecoesPalavras, stringEntrada, setCombinacoesManopla);  //multi-thread
            elapsedTimeCombina    = System.nanoTime() - startTime;
                
        // Validação de anagramas encontrados
        
            startTime = System.nanoTime();
        Validacao.validarAnagramasEmListas(listaPalavrasCombinadas, stringEntrada); //single-thread
        //ControleMultiThread.controlarMultiValidacaoLista(listaPalavrasCombinadas, stringEntrada);  //multi-thread
            elapsedTimeValida = System.nanoTime() - startTime;
        
            
        // Medidas de desempenho
        
        System.out.println("\nTempo de filtro: " + Timer.nanoTomilisec (elapsedTimeFiltro) + " milisegundos" );
        System.out.println("Tempo de criação de set: " + Timer.nanoTomilisec (elapsedTimeSetCombina) + " milisegundos" );
        System.out.println("Tempo de combinação: " + Timer.nanoTomilisec (elapsedTimeCombina) + " milisegundos" );
        System.out.println("Tempo de validação: " + Timer.nanoTomilisec (elapsedTimeValida) + " milisegundos" );
            
        // Destroi console
        
        consoleManopla.destruir();
    }

}
