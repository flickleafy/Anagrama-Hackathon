/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package testes;

import anagrama.AnagramaUnitTest;
import controle.CombinacaoPlanosUnitTest;
import controle.arvore.ArvoreCombinadorUnitTest;
import controle.arvore.TrieNodeUnitTest;
import controle.arvore.TrieUnitTest;
import controle.cuda.CudaCombinadorUnitTest;
import controle.cuda.CudaKernelCompilerUnitTest;
import controle.cuda.WordToVectorUnitTest;
import controle.multithread.ControleMultiThreadUnitTest;
import controle.multithread.ExecucaoMultiThreadUnitTest;
import controle.multithread.ListasParaThreadsUnitTest;
import controle.multithread.MultiCombinadorUnitTest;
import controle.multithread.MultiFiltroUnitTest;
import controle.multithread.MultiValidacaoUnitTest;
import controle.singlethread.ColecoesUnitTest;
import controle.singlethread.CombinadorUnitTest;
import controle.singlethread.EntradaUnitTest;
import controle.singlethread.FiltroUnitTest;
import controle.singlethread.SetCombinacoesUnitTest;
import controle.singlethread.ValidacaoUnitTest;
import main.MainUnitTest;
import util.io.ConsoleEntradaUnitTest;
import util.io.LerArquivoUnitTest;
import util.io.LerRecursoUnitTest;
import util.io.StreamStringUnitTest;
import util.time.CronometerUnitTest;

/** Ponto de entrada da suíte unitária espelhada em relação a {@code src/}. */
public final class UnitTests
{
    private UnitTests()
    {
    }

    public static int executarTudo() throws Exception
    {
        int executados = 0;
        AnagramaUnitTest.executar();
        executados++;
        CombinacaoPlanosUnitTest.executar();
        executados++;

        ArvoreCombinadorUnitTest.executar();
        executados++;
        TrieUnitTest.executar();
        executados++;
        TrieNodeUnitTest.executar();
        executados++;

        WordToVectorUnitTest.executar();
        executados++;
        CudaKernelCompilerUnitTest.executar();
        executados++;
        CudaCombinadorUnitTest.executar();
        executados++;

        ListasParaThreadsUnitTest.executar();
        executados++;
        ExecucaoMultiThreadUnitTest.executar();
        executados++;
        MultiFiltroUnitTest.executar();
        executados++;
        MultiCombinadorUnitTest.executar();
        executados++;
        MultiValidacaoUnitTest.executar();
        executados++;
        ControleMultiThreadUnitTest.executar();
        executados++;

        ColecoesUnitTest.executar();
        executados++;
        FiltroUnitTest.executar();
        executados++;
        SetCombinacoesUnitTest.executar();
        executados++;
        CombinadorUnitTest.executar();
        executados++;
        ValidacaoUnitTest.executar();
        executados++;
        EntradaUnitTest.executar();
        executados++;

        MainUnitTest.executar();
        executados++;
        ConsoleEntradaUnitTest.executar();
        executados++;
        LerArquivoUnitTest.executar();
        executados++;
        LerRecursoUnitTest.executar();
        executados++;
        StreamStringUnitTest.executar();
        executados++;
        CronometerUnitTest.executar();
        executados++;
        return executados;
    }

    public static void main(String[] args) throws Exception
    {
        int executados = executarTudo();
        System.out.println("OK - " + executados + " testes unitários executados");
    }
}
