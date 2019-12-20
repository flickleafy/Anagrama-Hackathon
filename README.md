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
Esta etapa faz simplesmente a leitura da entrada no console e a leitura do dicionário de entrada.

### Filtro de palavras
Remove do dicionário todas as palavras que não tenham letras relacionadas a palavra/expressão introduzida pelo usuário no console.

### Divisão em coleções
As palavras restantes, após a filtragem, são separadas em coleções, conforme a quantidade de caracteres de cada palavra.
Isso significa que, palavras com 1 letra, são adicionadas diretamente na lista numero 1, palavras com 2 letras, são adicionadas na lista numero 2, e assim sucessivamente.

### Set de combinações
O módulo de set de combinações verifica quais coleções estão disponíveis, pelo tamanho das palavras. Ao mesmo tempo também verifica qual o tamanho da palavra/expressão introduzida no console.

Se o tamanho da palavra/expressão for 15 caracteres, então serão montados sets de combinações em que a combinação total de caracteres dê exatamente 15 caracteres. Isso significa por exemplo que palavras que tenham tamanho que não permita que a soma total de caracteres dê o tamanho de referência, são Descartadas categoricamente.

A título de exemplo, o que esse módulo faz é similar a essa lista (usando como referência os 15 caracteres citados acima, e também considerando apenas as palavras que estão disponíveis após a etapa de filtragem):

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

### Execução de combinações
Essa etapa apenas faz as combinações das palavras conforme a lista de referencia passada pelo módulo Set de Combinações.

### Validação do anagrama
Essa etapa pega a palavra/expressão inicial e pega a lista de palavras combinadas, e faz uma validação final, para garantir que as palavras são anagramas de fato, e se confirmado, somente os anagramas são impressos no console.

## Justificativa do Design
A estrutura da aplicação foi escolhida para ser assim, dividida em várias etapas modulares e independentes por vários motivos, incluindo:
1.	Cada módulo contém apenas métodos coerentes com o que aquele módulo pretende computar.
2.	Por serem independentes, é possível otimizar cada módulo de acordo com necessidade, sem precisar alterar código de outros módulos. Se existir algum bug, a mesma afirmação feita anteriormente é válida.
3.	Por simplicidade e elegância na lógica de cada módulo, sendo muito mais fácil de fazer a gestão de alterações nos códigos de cada módulo. 
4.	Otimizações como o multithreads (paralelismo), exige que a seção que será executada paralelamente tenha a menor quantidade de dependências possíveis. Outro tipo de otimização, como a vetorização, funciona em cima do mesmo princípio.

## Tempo de execução do código
O código atual tem tempo de execução de aproximadamente 10 segundos, para uma palavra/frase de entrada de 15 letras (esse tempo pode variar muito dependendo da entrada, podendo chegar a 50 e poucos segundos), e um dicionário de 24.853 palavras. Entradas com menos letras levam em torno de 1 a 2 segundos, variando de caso a caso, de acordo com o tipo de entrada.

### Testes de desempenho
![Imagem Teste](https://github.com/eerbano/Anagrama-Hackathon/blob/master/Temporiza%C3%A7%C3%A3o.png)

No teste de desempenho da última versão que fiz (19-12-19) depois de analisar o benchmark, é fácil notar que o problema de desempenho se concentra todo no método que combina palavras. Para otimizar mais o que pode ser feito é converter palavras em números, e processar o vetor usando CUDA. 

Algumas pesquisas feitas mostram experimentos de combinações usando uma GPU atual, onde o processamento no CPU é 10 segundos, na GPU leva em torno de 600 milisegundos.

## Considerações sobre multithreads
Apesar que a versão atual tem alguns códigos que fazem uso de multithreads para paralelizar a execução dos módulos o máximo possível entre núcleos de processamento disponíveis no computador em que a aplicação será executada, o modelo Fork-Join atual, e as estruturas de dados usadas aparentemente não são adequadas para tirar o máximo de proveito do paralelismo.

Foi percebido que existe uma boa quantidade de “overhead”, que está ocasionando perda de desempenho. Não tive tempo suficiente para encontrar outro modelo de paralelismo que não apresentasse overhead para a situação atual do código. Apesar disso, existe boas possibilidades de o código poder ser bastante otimizado para aproveitar o máximo de paralelismo.

## Para fazer
Depois dos últimos testes (19-12-19), vou fazer as seguintes otimizações na próxima versão:
1.	Conversão de lista de palavras para vetor de números
2.	Processamento da combinação do vetor de números em CUDA
