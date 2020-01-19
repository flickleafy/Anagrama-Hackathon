/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package performance;

import controle.arvore.ArvoreCombinador;
import controle.cuda.CudaCombinador;
import controle.multithread.ControleMultiThread;
import controle.singlethread.Colecoes;
import controle.singlethread.Combinador;
import controle.singlethread.SetCombinacoes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Mantém a mesma carga em memória para todos os combinadores medidos. */
final class CenarioDesempenho
{
    static final String ALVO = "ABCDEF";

    private final ArrayList<ArrayList<String>> colecoes;
    private final SetCombinacoes planos;

    CenarioDesempenho()
    {
        ArrayList<String> palavras = new ArrayList<>(Arrays.asList(
                "A", "B", "C", "D", "E",
                "AB", "BC", "CD", "DE", "EA",
                "ABC", "BCD", "CDE", "DEA", "EAB"));
        colecoes = Colecoes.divisaoEmColecoes(palavras);
        planos = SetCombinacoes.criarSetDeCombinacoes(ALVO, colecoes);
    }

    ArrayList<String> executarSingleThread()
    {
        ArrayList<String> resultado = new ArrayList<>();
        Combinador.combinadorPalavras(resultado, colecoes, ALVO, planos);
        return resultado;
    }

    ArrayList<String> executarMultiThread()
    {
        ArrayList<String> resultado = new ArrayList<>();
        ControleMultiThread.controlarMultiCombinacoesLista(
                resultado, colecoes, ALVO, planos);
        return resultado;
    }

    ArrayList<String> executarArvore()
    {
        ArrayList<String> resultado = new ArrayList<>();
        ArvoreCombinador.combinadorPalavras(resultado, colecoes, planos);
        return resultado;
    }

    ResultadoCuda executarCuda(boolean tentarCuda)
    {
        ArrayList<String> resultado = new ArrayList<>();
        CudaCombinador combinador = new CudaCombinador(
                resultado, colecoes, planos, tentarCuda);
        return new ResultadoCuda(resultado, combinador);
    }

    static List<String> ordenar(List<String> valores)
    {
        ArrayList<String> ordenados = new ArrayList<>(valores);
        Collections.sort(ordenados);
        return ordenados;
    }

    static final class ResultadoCuda
    {
        final ArrayList<String> combinacoes;
        final CudaCombinador combinador;

        ResultadoCuda(
                ArrayList<String> combinacoes,
                CudaCombinador combinador)
        {
            this.combinacoes = combinacoes;
            this.combinador = combinador;
        }
    }
}
