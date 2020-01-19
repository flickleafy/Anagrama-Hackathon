/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package testes;

import integration.IntegrationTests;

/**
 * Ponto de entrada dos testes rotineiros sem dependências do projeto Ant.
 */
public final class AllTests
{
    private AllTests()
    {
    }

    public static void main(String[] args) throws Exception
    {
        int unitarios = UnitTests.executarTudo();
        int integracao = IntegrationTests.executarTudo();
        System.out.println(
                "OK - " + unitarios + " testes unitários e "
                + integracao + " testes de integração executados");
    }
}
