/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.multithread;

import controle.singlethread.SetCombinacoes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Controla as alternativas com múltiplas threads dos estágios do fluxo.
 *
 * @author Enzo Erbano
 */
public final class ControleMultiThread
{
    private ControleMultiThread()
    {
    }

    /**
     * Filtra a lista em partições paralelas e recompõe sua ordem original.
     *
     * @param listaPalavras lista mutável que será substituída pelos resultados
     * @param stringEntrada letras disponíveis para cada palavra
     * @return a mesma instância recebida, depois do filtro
     */
    public static ArrayList<String> controlarMultiFiltroLista(
            ArrayList<String> listaPalavras, String stringEntrada)
    {
        if (listaPalavras.isEmpty())
        {
            return listaPalavras;
        }

        ArrayList<ArrayList<String>> partes = ListasParaThreads.dividirLista(
                listaPalavras,
                ExecucaoMultiThread.definirNumeroDeThreads(listaPalavras.size()));
        ArrayList<Callable<ArrayList<String>>> tarefas = new ArrayList<>();
        for (ArrayList<String> parte : partes)
        {
            tarefas.add(new MultiFiltro(parte, stringEntrada));
        }

        ArrayList<ArrayList<String>> resultados =
                ExecucaoMultiThread.executar(tarefas);
        listaPalavras.clear();
        for (ArrayList<String> resultado : resultados)
        {
            listaPalavras.addAll(resultado);
        }
        return listaPalavras;
    }

    /**
     * Conta anagramas em partições paralelas sem alterar os candidatos.
     *
     * @param listaPalavras candidatos que serão validados
     * @param stringEntrada expressão de referência
     * @return soma exata das contagens produzidas pelas partições
     */
    public static long contarMultiValidacaoLista(
            ArrayList<String> listaPalavras, String stringEntrada)
    {
        if (listaPalavras.isEmpty())
        {
            return 0L;
        }

        ArrayList<ArrayList<String>> partes = ListasParaThreads.dividirLista(
                listaPalavras,
                ExecucaoMultiThread.definirNumeroDeThreads(listaPalavras.size()));
        ArrayList<Callable<Long>> tarefas = new ArrayList<>();
        for (ArrayList<String> parte : partes)
        {
            tarefas.add(new MultiValidacao(parte, stringEntrada));
        }

        long total = 0L;
        for (Long subtotal : ExecucaoMultiThread.executar(tarefas))
        {
            total += subtotal;
        }
        return total;
    }

    /**
     * Imprime a contagem paralela de anagramas.
     *
     * @param listaPalavras candidatos que serão validados
     * @param stringEntrada expressão de referência
     * @return a lista original, sem modificações
     */
    public static ArrayList<String> controlarMultiValidacaoLista(
            ArrayList<String> listaPalavras, String stringEntrada)
    {
        System.out.println(
                "Anagramas "
                + contarMultiValidacaoLista(listaPalavras, stringEntrada));
        return listaPalavras;
    }

    /**
     * Distribui planos completos entre threads de trabalho e acrescenta os resultados ao
     * destino na ordem original dos planos.
     *
     * <p>Cada thread de trabalho usa uma lista de saída própria. A concatenação ocorre
     * somente depois da conclusão das tarefas, evitando escrita concorrente no
     * destino compartilhado.</p>
     *
     * @param listaPalavrasCombinadas destino que receberá as combinações
     * @param colecoesPalavras palavras agrupadas por comprimento
     * @param stringEntrada parâmetro preservado por compatibilidade com o fluxo
     * @param setCombinacoesManopla planos que serão particionados
     * @return a mesma lista de destino, acrescida dos resultados
     */
    public static ArrayList<String> controlarMultiCombinacoesLista(
            ArrayList<String> listaPalavrasCombinadas,
            ArrayList<ArrayList<String>> colecoesPalavras,
            String stringEntrada,
            SetCombinacoes setCombinacoesManopla)
    {
        ArrayList<ArrayList<Integer>> todosPlanos =
                setCombinacoesManopla.getListaDeSetCombinacoes();
        if (todosPlanos.isEmpty())
        {
            return listaPalavrasCombinadas;
        }

        ArrayList<ArrayList<ArrayList<Integer>>> partes =
                ListasParaThreads.dividirLista(
                        todosPlanos,
                        ExecucaoMultiThread.definirNumeroDeThreads(todosPlanos.size()));
        ArrayList<Callable<ArrayList<String>>> tarefas = new ArrayList<>();
        for (ArrayList<ArrayList<Integer>> parte : partes)
        {
            tarefas.add(new MultiCombinador(
                    colecoesPalavras,
                    parte,
                    setCombinacoesManopla.getMapaListasPalavrasEmArray()));
        }

        List<ArrayList<String>> resultados =
                ExecucaoMultiThread.executar(tarefas);
        for (ArrayList<String> resultado : resultados)
        {
            listaPalavrasCombinadas.addAll(resultado);
        }
        return listaPalavrasCombinadas;
    }
}
