/*
 * Copyright (C) 2021 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package util.io;

import java.io.InputStream;

/**
 * Abre recursos disponíveis no classpath da aplicação.
 *
 * @author Enzo Erbano
 */
public final class LerRecurso
{
    /**
     * Localiza um recurso pelo carregador de classes.
     *
     * @param nomeArquivo caminho relativo dentro do classpath
     * @return fluxo aberto, ou {@code null} para nome vazio ou recurso ausente
     */
    public InputStream lerRecurso(String nomeArquivo)
    {
        if (nomeArquivo == null || nomeArquivo.isEmpty())
        {
            return null;
        }
        return LerRecurso.class.getClassLoader().getResourceAsStream(nomeArquivo);
    }
}
