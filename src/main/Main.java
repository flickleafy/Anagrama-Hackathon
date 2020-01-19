/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package main;

import controle.singlethread.Colecoes;
import controle.singlethread.Combinador;
import controle.singlethread.Entrada;
import controle.singlethread.Filtro;
import controle.singlethread.SetCombinacoes;
import controle.singlethread.Validacao;
import java.util.ArrayList;
import util.io.ConsoleEntrada;
import util.time.Cronometer;

/**
 * Ponto de entrada do fluxo sequencial da aplicação.
 *
 * @author Enzo Erbano
 */
public final class Main
{
    private Main()
    {
    }

    /**
     * Executa leitura, filtro, planejamento, combinação e validação.
     *
     * @param args argumentos de linha de comando; não são utilizados
     */
    public static void main(String[] args)
    {
        try (ConsoleEntrada console = new ConsoleEntrada())
        {
            String entrada = Entrada.lerEntradaConsole(console);
            if (Entrada.validarString(entrada))
            {
                System.err.println("A entrada contém caracteres inválidos");
                return;
            }

            ArrayList<String> palavras = Entrada.lerArquivoConsole(console);
            Cronometer cronometro = new Cronometer();
            cronometro.start();

            Filtro.eliminarPalavrasEmLista(palavras, entrada);
            long tempoFiltro = cronometro.leap();

            ArrayList<ArrayList<String>> colecoes =
                    Colecoes.divisaoEmColecoes(palavras);
            SetCombinacoes planos =
                    SetCombinacoes.criarSetDeCombinacoes(entrada, colecoes);
            long tempoPlanos = cronometro.leap();

            ArrayList<String> combinacoes = new ArrayList<>();
            Combinador.combinadorPalavras(
                    combinacoes, colecoes, entrada, planos);
            long tempoCombinacao = cronometro.leap();

            System.out.println("Total de combinações " + combinacoes.size());
            Validacao.validarAnagramasEmListas(combinacoes, entrada);
            long tempoValidacao = cronometro.leap();

            System.out.println("\nTempo de filtro: " + tempoFiltro + " milissegundos");
            System.out.println("Tempo de criação de set: " + tempoPlanos + " milissegundos");
            System.out.println("Tempo de combinação: " + tempoCombinacao + " milissegundos");
            System.out.println("Tempo de validação: " + tempoValidacao + " milissegundos");
        }
    }
}
