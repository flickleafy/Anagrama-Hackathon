/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package util.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Abre um dicionário armazenado no sistema de arquivos.
 *
 * @author Enzo Erbano
 */
public final class LerArquivo
{
    /**
     * Abre um arquivo para leitura.
     *
     * <p>Em caso de falha, registra a mensagem no fluxo de erro e retorna
     * {@code null} para preservar o contrato da aplicação.</p>
     *
     * @param nomeArquivo caminho do arquivo desejado
     * @return fluxo aberto, ou {@code null} quando o arquivo não existe
     */
    public InputStream lerArquivo(String nomeArquivo)
    {
        try
        {
            return new FileInputStream(nomeArquivo);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Erro: " + e.getMessage());
            return null;
        }
    }
}
