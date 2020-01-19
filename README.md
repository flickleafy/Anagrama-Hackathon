# Anagrama-Hackathon

Desenvolvimento realizado durante a quarta etapa do Hackathon De Carreiras realizado pela Universidade Positivo. 

Local do evento: 
Universidade Positivo – Campus Ecoville - Prédio da Pós Graduação
R. Professor Pedro Viriato Parigot de Souza, 5300 - Campo Comprido, Curitiba – PR

Site: 
https://hack-ti.paniclobster.com/

# Detalhes sobre o funcionamento do algoritmo

## Introdução

A arquitetura da aplicação que desenvolvi tem a seguinte estruturação geral:

1.	Leitura de entrada
2.	Filtro de palavras do dicionário de entrada
3.	Divisão das palavras do dicionário em coleções
4.	Criação de set de combinações
5.	Execução de combinações de palavras
6.	Validação do anagrama

### Leitura de entrada

Esta etapa faz a leitura da palavra ou frase no console, converte a entrada para letras maiúsculas e valida seu conteúdo. Espaços são aceitos e ignorados durante a comparação, enquanto uma entrada vazia ou com caracteres diferentes de `A-Z` é rejeitada.

Em seguida é feita a leitura UTF-8 do dicionário informado. Quando o caminho digitado tem menos de cinco caracteres, a aplicação utiliza o dicionário distribuído em `res/palavras.82eebac6.txt`.

### Filtro de palavras

Remove do dicionário todas as palavras que não podem ser formadas com as letras da palavra/expressão introduzida pelo usuário no console. O filtro considera a quantidade disponível de cada letra, não apenas sua presença. Palavras vazias, nulas ou com caracteres inválidos também não seguem para as próximas etapas.

### Divisão em coleções

As palavras restantes, após a filtragem, são separadas em coleções, conforme a quantidade normalizada de caracteres de cada palavra.

Isso significa que palavras com 1 letra são adicionadas à coleção de tamanho 1, palavras com 2 letras são adicionadas à coleção de tamanho 2, e assim sucessivamente. As coleções são criadas dinamicamente e ordenadas pelo tamanho das palavras, sem um limite fixo de comprimento.

### Set de combinações

O módulo de set de combinações verifica quais coleções estão disponíveis, pelo tamanho das palavras. Ao mesmo tempo também verifica qual o tamanho da palavra/expressão introduzida no console.

Se o tamanho da palavra/expressão for 15 caracteres, então serão montados sets de combinações em que a soma total dê exatamente 15 caracteres. Tamanhos que não permitam alcançar o tamanho de referência são descartados durante a própria busca.

A título de exemplo, alguns dos sets possíveis são similares a esta lista, considerando apenas os tamanhos que estão disponíveis após a etapa de filtragem:

* combinações([1, 2, 3, 4, 5])=15
* combinações([1, 2, 4, 8])=15
* combinações([1, 2, 5, 7])=15
* combinações([1, 3, 4, 7])=15
* combinações([1, 3, 5, 6])=15
* combinações([1, 6, 8])=15
* combinações([2, 3, 4, 6])=15
* combinações([2, 5, 8])=15
* combinações([2, 6, 7])=15
* combinações([3, 4, 8])=15
* combinações([3, 5, 7])=15
* combinações([4, 5, 6])=15
* combinações([7, 8])=15
* combinações([3, 3, 3, 3, 3])=15
* combinações([5, 5, 5])=15

O mesmo tamanho pode ser usado mais de uma vez, permitindo combinações como `3+3` ou `5+5+5`. Os tamanhos são mantidos em ordem crescente para não gerar sets equivalentes apenas pela permutação. Cada instância conserva seu próprio estado e fornece cópias defensivas, por isso os planos podem ser reutilizados em chamadas consecutivas sem serem consumidos.

### Execução de combinações

Essa etapa faz o produto cartesiano das coleções conforme cada lista de referência passada pelo módulo Set de Combinações. A implementação padrão é a versão sequencial em `src/controle/singlethread`.

Quando um plano repete o mesmo tamanho, os índices das palavras são percorridos em ordem não decrescente. Isso permite reutilizar uma palavra quando necessário, mas evita gerar novamente uma permutação equivalente. A regra central de enumeração é compartilhada com as implementações alternativas para manter os resultados consistentes.

### Validação do anagrama

Essa etapa pega a palavra/expressão inicial e a lista de palavras combinadas e faz uma validação final. A comparação usa um vetor com a contagem exata das 26 letras, ignorando diferenças entre letras maiúsculas e minúsculas e os caracteres de espaço. Dessa forma, combinações com as mesmas letras, mas com quantidades diferentes, não são aceitas por engano.

Ao final, a quantidade de anagramas confirmados é impressa no console.

## Justificativa do Design

A estrutura da aplicação foi escolhida para ser assim, dividida em várias etapas com responsabilidades separadas, por vários motivos, incluindo:

1.	Cada módulo contém apenas métodos coerentes com o que aquele módulo pretende computar.
2.	É possível corrigir ou otimizar uma etapa preservando os contratos usados pelas demais etapas.
3.	A separação simplifica a lógica de cada módulo e facilita a gestão das alterações no código.
4.	O paralelismo e a vetorização exigem que a seção otimizada tenha poucas dependências e resultados bem definidos.
5.	As implementações compartilham a mesma normalização, o mesmo planejamento e a mesma regra canônica de combinação, evitando resultados diferentes para a mesma entrada.

## Tempo de execução do código

O tempo de execução não pode ser representado por um valor fixo. Ele varia conforme a palavra/frase, o conteúdo e o tamanho do dicionário, os sets de comprimentos possíveis, a quantidade de palavras em cada coleção e o equipamento utilizado.

A etapa de combinação continua materializando os resultados antes da validação. Por isso, entradas que produzem produtos cartesianos maiores aumentam tanto o tempo como o consumo de memória. A aplicação imprime separadamente os tempos de filtro, criação dos sets, combinação e validação para permitir a análise de cada execução.

### Testes de desempenho

Os testes em `test/performance` comparam as implementações single-thread, multithread, trie e a enumeração numérica da alternativa CUDA executada em CPU. Todas recebem a mesma carga determinística, e a equivalência dos resultados é verificada antes da medição.

O benchmark faz aquecimentos antes das iterações medidas e informa a média em nanossegundos por operação. Ele não impõe um tempo máximo, porque o resultado depende da máquina e da carga do sistema. Em entradas pequenas, o custo de criar threads ou inicializar um contexto CUDA pode ser maior que o trabalho de combinação; portanto, nenhuma aceleração fixa é garantida.

O desempenho CUDA nativo é medido somente pelo alvo específico que exige a biblioteca nativa, o driver e uma GPU compatível. Assim, uma contingência em CPU nunca é informada como se fosse uma medição da GPU.

## Considerações sobre multithreads

A versão multithread está implementada como alternativa para as etapas de filtro, combinação e validação. As listas e os sets são divididos em partições balanceadas e o número de threads é limitado pelo menor valor entre a carga de trabalho e os processadores disponíveis.

Cada tarefa escreve em uma saída própria, e os resultados são unidos na ordem original somente depois que todas as tarefas terminam. Isso evita escrita concorrente na lista final, preserva um resultado determinístico e propaga falhas sem retornar silenciosamente um resultado parcial.

O fluxo usado por `main.Main` continua sendo single-thread. A alternativa multithread pode aproveitar cargas maiores, mas ainda possui overhead de particionamento, criação das tarefas e sincronização. Por isso, sua vantagem deve ser medida para cada entrada, sem assumir que sempre será mais rápida.

## Implementações alternativas

As otimizações estão disponíveis como alternativas para a mesma etapa de combinação:

1.	`src/controle/multithread` executa partições independentes em um conjunto limitado de threads e recompõe os resultados de forma determinística.
2.	`src/controle/arvore` representa as sequências de identificadores em uma trie, compartilhando prefixos e evitando a duplicação da mesma sequência numérica antes de reconstruir as palavras.
3.	`src/controle/cuda` converte cada posição das coleções em um identificador numérico estável e usa um kernel CUDA para enumerar os índices do produto cartesiano em bases mistas.

A implementação CUDA processa a saída temporária em lotes limitados, libera contexto, módulo e memória do dispositivo ao final e só acrescenta o resultado ao destino depois de concluir a tentativa. Caso JCuda, o driver, a GPU ou o PTX não estejam disponíveis, a mesma enumeração é reiniciada automaticamente na CPU, sem duplicar uma saída parcial.

O projeto distribui o código-fonte do kernel e um PTX pré-compilado. O PTX é validado para ISA 6.4 e alvo `sm_75`, respeitando o limite do CUDA 10.1 usado pelas dependências JCuda 10.1.0. O `nvcc` só é chamado como contingência quando o PTX distribuído não pode ser localizado.

Essas versões são acessadas por suas classes de controle; elas não substituem automaticamente o fluxo sequencial da CLI.

## Compilação e execução

O projeto usa Java 8, a estrutura de projeto do NetBeans e o Apache Ant. Para gerar o arquivo `dist/Anagrama.jar` e executar a aplicação:

```sh
ant clean jar
ant run
```

Durante a execução são solicitadas a palavra/frase e a localização do dicionário. Basta deixar a localização vazia, ou informar uma entrada com menos de cinco caracteres, para usar o dicionário distribuído com a aplicação.

### Execução em Docker

O projeto também fornece duas imagens `linux/amd64` com todas as ferramentas necessárias, sem alterar a instalação Java da máquina:

* `anagrama:java8-ant1.10.7` usa Ubuntu 18.04.3, OpenJDK 8u232-b09, Apache Ant 1.10.7 e os artefatos JCuda 10.1.0 distribuídos no projeto.
* `anagrama:cuda10.1-java8-ant1.10.7` acrescenta CUDA 10.1.243, CUDA Runtime 10.1.243, GCC/G++ 7.3.0, glibc 2.27 e cabeçalhos Linux 4.15.0-20. O kernel CUDA é compilado durante a construção para confirmar PTX 6.4 e alvo `sm_75`.

A imagem Java é identificada por digest. O arquivo do Ant, os pacotes CUDA e os JARs JCuda são conferidos por checksum; as bibliotecas do compilador CUDA usam versões exatas do repositório-base do Ubuntu. A imagem não executa como `root` depois de pronta.

O script de apoio aceita tanto o Compose integrado ao Docker como o executável `docker-compose`. Para construir, conferir, testar e executar a variante CPU:

```sh
./docker/anagrama.sh cpu build
./docker/anagrama.sh cpu verify
./docker/anagrama.sh cpu test
./docker/anagrama.sh cpu run
```

Para a variante CUDA:

```sh
./docker/anagrama.sh cuda build
./docker/anagrama.sh cuda verify
./docker/anagrama.sh cuda test
./docker/anagrama.sh cuda run
```

A construção CUDA não exige uma GPU, pois o `nvcc` recompila o kernel sem executá-lo. As ações `verify`, `test` e `run` exigem o runtime `nvidia` registrado no Docker; o teste executa todas as suítes e acrescenta o teste CUDA nativo, que falha se houver contingência em CPU. O comando `cuda run` mantém a CLI sequencial definida por `main.Main`; a GPU é exercitada pela implementação alternativa e pelo alvo de teste próprio.

Os arquivos Compose usam o formato 2.4 e identificam explicitamente o runtime `nvidia` exigido pela variante CUDA.

O contêiner fixa o espaço de usuário, mas compartilha o kernel e o daemon Docker do host. O ambiente-alvo utiliza Linux 4.15 a 5.4, Docker 18.06 a 19.03 e Docker Compose 1.21 a 1.25. Por padrão, diferenças nessas versões produzem um aviso e a execução continua. Para exigir exatamente o ambiente-alvo:

```sh
ANAGRAMA_EXIGIR_AMBIENTE_ALVO=1 ./docker/anagrama.sh cpu verify
```

Para executar todas as camadas com as versões definidas, use um host ou máquina virtual dentro desses limites. No modo CUDA, também é necessário um driver NVIDIA compatível com CUDA 10.1; a imagem declara o mínimo 418.39 e valida a disponibilidade real da GPU no teste nativo.

## Testes automatizados

Os testes ficam integralmente dentro da pasta `test` e não dependem de um framework externo:

* `test/unit` espelha os pacotes de `src` e contém uma classe de teste para cada uma das 26 classes Java da aplicação.
* `test/integration` contém 6 testes que exercitam o fluxo single-thread, a alternativa multithread, os combinadores trie e CUDA, a leitura de arquivos e a CLI completa.
* `test/performance` contém os cenários e medições dos quatro combinadores, além da medição CUDA nativa opcional.
* `test/testes` contém os executores das suítes e os recursos de asserção compartilhados.

Os alvos Ant permitem executar cada categoria separadamente:

```sh
ant test-unit
ant test-integration
ant test
ant test-performance
ant test-performance-cuda
ant test-all
```

`ant test` executa os testes unitários e de integração. `ant test-all` acrescenta os testes de desempenho independentes de GPU. O teste CUDA nativo permanece separado em `ant test-performance-cuda` e falha quando o backend CUDA não é realmente usado, evitando medir a contingência em CPU como se fosse GPU.
