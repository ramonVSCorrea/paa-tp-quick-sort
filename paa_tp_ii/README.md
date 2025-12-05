# Solucionadores do Problema da Mochila (0/1)

Este projeto implementa e compara diversos algoritmos para resolver o clássico Problema da Mochila (Knapsack Problem) 0/1. O objetivo é encontrar a combinação de itens que maximiza o valor total dentro de uma capacidade de peso limitada, onde cada item pode ser incluído apenas uma vez (0 ou 1).

## Algoritmos Implementados

Os seguintes algoritmos estão implementados para análise e comparação de desempenho:

*   **Força Bruta (Brute Force):** Explora todas as combinações possíveis de itens para encontrar a solução ótima. (Complexidade exponencial)
*   **Programação Dinâmica com Espaço Linear (DP Linear-Space):** Uma abordagem de programação dinâmica que encontra a solução ótima com complexidade de tempo `O(nL)` e complexidade de espaço `O(L)`, onde `n` é o número de itens e `L` é a capacidade da mochila.
*   **Guloso (Greedy by Ratio):** Um algoritmo heurístico que seleciona itens com base na sua relação valor/peso, do maior para o menor. Não garante a solução ótima, mas é rápido.
*   **FPTAS (Fully Polynomial-Time Approximation Scheme):** Um esquema de aproximação que pode encontrar uma solução próxima da ótima dentro de um fator de erro controlável `epsilon` em tempo polinomial.

## Como Compilar e Executar

O projeto pode ser compilado e executado utilizando o script `run.bat` no diretório raiz do projeto.

### Uso do `run.bat`

*   **Compilar e Executar:**
    ```bash
    run.bat
    ```
    Este comando compila todos os arquivos Java do projeto (`src/main/java`) para o diretório `bin/` e, em seguida, executa a classe principal `paa.knapsack.Main`.

*   **Executar (sem recompilar):**
    ```bash
    run.bat runonly
    ```
    Este comando executa a aplicação diretamente, assumindo que os arquivos já foram compilados e estão no diretório `bin/`. Utilize isso para reexecuções rápidas após a primeira compilação.

*   **Limpar arquivos compilados e resultados:**
    ```bash
    run.bat clean
    ```
    Este comando remove o diretório `bin/` (contendo os arquivos `.class` compilados) e o diretório `results/` (contendo os arquivos CSV gerados pelos experimentos).

### Estrutura do Projeto

*   `src/main/java/`: Contém o código fonte Java principal.
    *   `paa/knapsack/`: Pacote raiz para o projeto da mochila.
        *   `application/`: Contém a lógica de inicialização e orquestração do estudo (`KnapsackStudyStarter.java`).
        *   `domain/`: Contém as classes de domínio (Item, KnapsackInstance, KnapsackResult) e os algoritmos de solução (`algorithms/KnapsackSolver.java`, `performance/ExperimentRunner.java`, `testdata/KnapsackInstanceGenerator.java`).
        *   `infrastructure/`: Contém a lógica de exportação de resultados (`export/CSVExporter.java`).
*   `bin/`: Diretório de saída para os arquivos `.class` compilados.
*   `results/`: Diretório de saída para os arquivos CSV gerados pelos experimentos.
*   `pom.xml`: Arquivo de configuração Maven.
*   `run.bat`: Script principal para compilar, executar e limpar o projeto.
*   `README.md`: Este arquivo.
