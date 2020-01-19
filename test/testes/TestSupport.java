/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package testes;

import java.util.Objects;

public final class TestSupport
{
    public interface Acao
    {
        void executar() throws Exception;
    }

    private TestSupport()
    {
    }

    public static void verdadeiro(boolean condicao, String mensagem)
    {
        if (!condicao)
        {
            throw new AssertionError(mensagem);
        }
    }

    public static void falso(boolean condicao, String mensagem)
    {
        verdadeiro(!condicao, mensagem);
    }

    public static void igual(Object esperado, Object atual, String mensagem)
    {
        if (!Objects.equals(esperado, atual))
        {
            throw new AssertionError(
                    mensagem + " - esperado: " + esperado + ", atual: " + atual);
        }
    }

    public static void lanca(
            Class<? extends Throwable> tipo,
            Acao acao,
            String mensagem)
    {
        try
        {
            acao.executar();
        }
        catch (Throwable erro)
        {
            if (tipo.isInstance(erro))
            {
                return;
            }
            throw new AssertionError(mensagem + " - exceção inesperada: " + erro, erro);
        }
        throw new AssertionError(mensagem + " - nenhuma exceção foi lançada");
    }
}
