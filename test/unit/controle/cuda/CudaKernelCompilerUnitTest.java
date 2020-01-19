/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
package controle.cuda;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import testes.TestSupport;

/** Verifica isoladamente o contrato de {@link CudaKernelCompiler}. */
public final class CudaKernelCompilerUnitTest
{
    private CudaKernelCompilerUnitTest()
    {
    }

    public static void executar() throws Exception
    {
        String primeiro = CudaKernelCompiler.prepararPtx();
        Path caminho = Paths.get(primeiro);
        TestSupport.verdadeiro(
                Files.isRegularFile(caminho),
                "O carregador deve fornecer um arquivo PTX existente");
        TestSupport.verdadeiro(
                Files.size(caminho) > 0L,
                "O arquivo PTX não deve estar vazio");
        String conteudo = new String(
                Files.readAllBytes(caminho), StandardCharsets.UTF_8);
        TestSupport.verdadeiro(
                conteudo.contains("release 10.1, V10.1.243"),
                "O PTX distribuído deve ser gerado pelo CUDA 10.1.243");
        TestSupport.verdadeiro(
                conteudo.contains(".version 6.4"),
                "O PTX distribuído deve usar a ISA 6.4");
        TestSupport.verdadeiro(
                conteudo.contains(".target sm_75"),
                "O PTX distribuído deve respeitar o teto sm_75");
        TestSupport.verdadeiro(
                conteudo.contains(".entry combinar_indices"),
                "O PTX distribuído deve exportar o kernel esperado");
        CudaKernelCompiler.validarCompatibilidadePtx(caminho);
        TestSupport.igual(
                primeiro,
                CudaKernelCompiler.prepararPtx(),
                "Chamadas consecutivas devem reutilizar o caminho em cache");

        verificarRejeicaoPtx(
                ".version 8.8\n.target sm_75\n.address_size 64\n",
                "PTX acima do limite 6.4 deve ser rejeitado");
        verificarRejeicaoPtx(
                ".version 6.4\n.target sm_90\n.address_size 64\n",
                "Arquitetura acima do limite sm_75 deve ser rejeitada");
        verificarRejeicaoPtx(
                ".target sm_75\n.address_size 64\n",
                "PTX sem versão deve ser rejeitado");
        verificarRejeicaoPtx(
                ".version 6.4\n.address_size 64\n",
                "PTX sem arquitetura deve ser rejeitado");
        verificarRejeicaoPtx(
                ".version 6.x\n.target sm_75\n.address_size 64\n",
                "Diretiva de versão inválida deve ser rejeitada");
        verificarRejeicaoPtx(
                ".version 6.4\n.target compute_75\n.address_size 64\n",
                "Diretiva de arquitetura inválida deve ser rejeitada");
    }

    /**
     * Cria um módulo temporário e confirma que o validador recusa suas diretivas.
     */
    private static void verificarRejeicaoPtx(
            String conteudo, String mensagem) throws Exception
    {
        final Path ptx = Files.createTempFile("anagrama-ptx-incompativel-", ".ptx");
        try
        {
            Files.write(ptx, conteudo.getBytes(StandardCharsets.UTF_8));
            TestSupport.lanca(
                    IOException.class,
                    new TestSupport.Acao()
                    {
                        @Override
                        public void executar() throws Exception
                        {
                            CudaKernelCompiler.validarCompatibilidadePtx(ptx);
                        }
                    },
                    mensagem);
        }
        finally
        {
            Files.deleteIfExists(ptx);
        }
    }
}
