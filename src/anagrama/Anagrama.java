/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package anagrama;

import java.util.Arrays;
import java.util.Locale;

/**
 * Centraliza a normalização e a contagem de letras usadas pelas
 * implementações de anagramas.
 *
 * @author Enzo Erbano
 */
public final class Anagrama
{
    private static final int QUANTIDADE_LETRAS = 26;

    private Anagrama()
    {
    }

    /**
     * Verifica se dois textos possuem exatamente as mesmas letras.
     *
     * <p>A comparação ignora caixa e caracteres de espaço. Valores nulos ou
     * com caracteres fora do intervalo de {@code A} a {@code Z} não são
     * considerados anagramas.</p>
     *
     * @param str1 primeiro texto da comparação
     * @param str2 segundo texto da comparação
     * @return {@code true} quando as contagens de todas as letras coincidem
     */
    public static boolean checarAnagrama(String str1, String str2)
    {
        int[] contagem1 = contarLetras(str1);
        int[] contagem2 = contarLetras(str2);
        return contagem1 != null && contagem2 != null
                && Arrays.equals(contagem1, contagem2);
    }

    /**
     * Verifica se uma palavra pode ser montada com as letras da referência.
     *
     * @param palavra palavra candidata; deve conter ao menos uma letra válida
     * @param referencia conjunto de letras disponíveis
     * @return {@code true} quando cada letra ocorre no máximo a quantidade
     *         disponível na referência
     */
    public static boolean podeSerFormadaPor(String palavra, String referencia)
    {
        String palavraNormalizada = normalizar(palavra);
        String referenciaNormalizada = normalizar(referencia);

        if (palavraNormalizada == null || referenciaNormalizada == null
                || palavraNormalizada.isEmpty())
        {
            return false;
        }

        int[] contagemPalavra = contarNormalizado(palavraNormalizada);
        int[] contagemReferencia = contarNormalizado(referenciaNormalizada);

        for (int i = 0; i < QUANTIDADE_LETRAS; i++)
        {
            if (contagemPalavra[i] > contagemReferencia[i])
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Conta as ocorrências das letras depois de normalizar um texto.
     *
     * @param valor texto que será normalizado antes da contagem
     * @return novo vetor de 26 posições, de {@code A} a {@code Z}, ou
     *         {@code null} quando o texto for nulo ou inválido
     */
    public static int[] contarLetras(String valor)
    {
        String normalizado = normalizar(valor);
        if (normalizado == null)
        {
            return null;
        }

        return contarNormalizado(normalizado);
    }

    /**
     * Conta um valor que já satisfaz o contrato interno de normalização.
     */
    private static int[] contarNormalizado(String normalizado)
    {
        int[] contagem = new int[QUANTIDADE_LETRAS];
        for (int i = 0; i < normalizado.length(); i++)
        {
            contagem[normalizado.charAt(i) - 'A']++;
        }
        return contagem;
    }

    /**
     * Converte um texto para maiúsculas e remove caracteres de espaço.
     *
     * @param valor texto de entrada
     * @return texto contendo somente {@code A-Z}, ou {@code null} se a entrada
     *         for nula ou contiver outro caractere
     */
    public static String normalizar(String valor)
    {
        if (valor == null)
        {
            return null;
        }

        String maiusculo = valor.toUpperCase(Locale.ROOT);
        StringBuilder normalizado = new StringBuilder(maiusculo.length());
        for (int i = 0; i < maiusculo.length(); i++)
        {
            char caractere = maiusculo.charAt(i);
            if (Character.isWhitespace(caractere))
            {
                continue;
            }
            if (caractere < 'A' || caractere > 'Z')
            {
                return null;
            }
            normalizado.append(caractere);
        }
        return normalizado.toString();
    }
}
