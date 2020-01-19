/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package anagrama;

import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link Anagrama}. */
public final class AnagramaUnitTest
{
    private AnagramaUnitTest()
    {
    }

    public static void executar()
    {
        TestSupport.verdadeiro(
                Anagrama.checarAnagrama("O REI", "REIO"),
                "Espaços devem ser ignorados na comparação");
        TestSupport.verdadeiro(
                Anagrama.checarAnagrama("roma", "AMOR"),
                "A comparação deve ignorar caixa");
        TestSupport.falso(
                Anagrama.checarAnagrama("AABB", "CCDD"),
                "Colisões do cálculo XOR devem ser rejeitadas");
        TestSupport.falso(
                Anagrama.checarAnagrama("AABC", "ABCC"),
                "A comparação deve respeitar multiplicidades");
        TestSupport.falso(
                Anagrama.checarAnagrama(null, "ABC"),
                "Valores nulos não são anagramas");
        TestSupport.falso(
                Anagrama.checarAnagrama("AÇÃO", "ACAO"),
                "Caracteres fora de A-Z devem ser rejeitados");

        TestSupport.verdadeiro(
                Anagrama.podeSerFormadaPor("CAB", "AABC"),
                "Uma palavra com letras disponíveis deve ser aceita");
        TestSupport.falso(
                Anagrama.podeSerFormadaPor("AAA", "AABC"),
                "O filtro de letras deve respeitar quantidades");
        TestSupport.falso(
                Anagrama.podeSerFormadaPor("", "ABC"),
                "Uma palavra vazia deve ser rejeitada");

        int[] contagem = Anagrama.contarLetras("A aB");
        TestSupport.igual(2, contagem[0], "A contagem da letra A deve ser exata");
        TestSupport.igual(1, contagem[1], "A contagem da letra B deve ser exata");
        TestSupport.igual(null, Anagrama.contarLetras("A-"), "Texto inválido não deve ser contado");
        TestSupport.igual("ROMAANTIGA", Anagrama.normalizar("Roma\tAntiga"), "A normalização deve remover espaços");
        TestSupport.igual(null, Anagrama.normalizar(null), "Entrada nula deve permanecer inválida");
    }
}
