/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controle.singlethread.Colecoes;
import controle.singlethread.Entrada;
import util.io.ConsoleEntrada;
import java.util.ArrayList;
import controle.multithread.ControleMultiThread;
import controle.singlethread.Filtro;
import controle.singlethread.SetCombinacoes;
import controle.singlethread.Validacao;

/**
 *
 * @author Enzo Erbano
 */

public class Main
{

    public static void main(String[] args)
    {
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
        
        listaPalavras = Filtro.eliminarPalavrasEmLista(listaPalavras, stringEntrada); //single-thread        
        //listaPalavras = ControleMultiThread.controlarMultiFiltroLista(listaPalavras, stringEntrada);  //multi-thread

        // Divisão em coleçoes por tamanho de palavra
        
        ArrayList<ArrayList> colecoesPalavras = Colecoes.divisaoEmColecoes(listaPalavras); //single-thread
        
        // Criação de set de combinações
        
        SetCombinacoes setCombinacoesManopla = SetCombinacoes.criarSetDeCombinacoes(stringEntrada, colecoesPalavras); //single-thread
        
        // Execução de combinações
        
        ArrayList<String> listaPalavrasCombinadas = new ArrayList<>();
        
        //Combinador.combinadorPalavras(listaPalavrasCombinadas, colecoesPalavras, stringEntrada, setCombinacoesManopla); //single-thread        
        ControleMultiThread.controlarMultiCombinacoesLista(listaPalavrasCombinadas,colecoesPalavras, stringEntrada, setCombinacoesManopla);  //multi-thread

        // Validação de anagramas encontrados
        
        Validacao.validarAnagramasEmListas(listaPalavrasCombinadas, stringEntrada); //single-thread
        //ControleMultiThread.controlarMultiValidacaoLista(listaPalavrasCombinadas, stringEntrada);  //multi-thread

        // Destroi console
        
        consoleManopla.destruir();
    }

}
