# 🎒 Problema da Mochila 0/1 (Knapsack Problem)

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

> **PAA-TP-II**: Estudo completo e análise comparativa de algoritmos para resolução do clássico Problema da Mochila 0/1.

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Problema da Mochila 0/1](#-problema-da-mochila-01)
- [Algoritmos Implementados](#-algoritmos-implementados)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Como Usar](#-como-usar)
- [Análise de Desempenho](#-análise-de-desempenho)
- [Resultados](#-resultados)
- [Tecnologias](#-tecnologias)

---

## 🎯 Sobre o Projeto

Este projeto implementa e compara diferentes abordagens algorítmicas para resolver o **Problema da Mochila 0/1**, um dos problemas clássicos da Ciência da Computação e Otimização Combinatória.

### Objetivos

- ✅ Implementar algoritmos exatos e aproximados
- ✅ Analisar complexidade de tempo e espaço
- ✅ Comparar desempenho prático em diferentes cenários
- ✅ Gerar relatórios detalhados em formato CSV
- ✅ Fornecer insights sobre trade-offs entre otimalidade e eficiência

---

## 🔍 Problema da Mochila 0/1

### Definição

Dado um conjunto de **n** itens, cada um com:
- Um **peso** (w<sub>i</sub>)
- Um **valor** (v<sub>i</sub>)

E uma mochila com capacidade máxima **L**, encontre o subconjunto de itens que:
- Maximize o valor total
- Respeite a restrição de capacidade
- Cada item pode ser incluído **no máximo uma vez** (0 ou 1)

### Formulação Matemática

```
Maximizar: Σ(vi × xi)
Sujeito a: Σ(wi × xi) ≤ L
Onde: xi ∈ {0, 1} para todo i
```

### Complexidade

O problema da mochila 0/1 é **NP-Completo**, o que significa:
- Não existe algoritmo conhecido que resolva em tempo polinomial
- Algoritmos exatos têm complexidade exponencial ou pseudo-polinomial
- Algoritmos aproximados são necessários para instâncias grandes

---

## 🧮 Algoritmos Implementados

### 1. **Programação Dinâmica - Espaço Linear** (`KnapsackDPLinear`)

**Algoritmo Exato** ✅ Solução Ótima Garantida

```java
// Localização: domain/algorithms/KnapsackDPLinear.java
```

**Características:**
- ⏱️ **Tempo**: O(n × L)
- 💾 **Espaço**: O(L)
- 🎯 **Solução**: Ótima (retorna apenas o valor máximo)
- 📊 **Uso**: Quando a capacidade L é moderada e só precisamos do valor

**Vantagens:**
- ✅ Garante solução ótima
- ✅ Uso eficiente de memória
- ✅ Escalável para L moderado

**Desvantagens:**
- ❌ Não reconstrói quais itens foram selecionados
- ❌ Pseudo-polinomial (depende de L)

---

### 2. **Programação Dinâmica - Matriz Completa** (`KnapsackPD`)

**Algoritmo Exato** ✅ Solução Ótima Garantida

```java
// Localização: domain/algorithms/KnapsackPD.java
```

**Características:**
- ⏱️ **Tempo**: O(n × L)
- 💾 **Espaço**: O(n × L)
- 🎯 **Solução**: Ótima (com reconstrução dos itens selecionados)
- 📊 **Uso**: Quando precisamos saber quais itens foram escolhidos

**Vantagens:**
- ✅ Garante solução ótima
- ✅ Reconstrói os itens selecionados
- ✅ Permite análise detalhada da solução

**Desvantagens:**
- ❌ Alto uso de memória O(n × L)
- ❌ Limitado por restrições de memória para L muito grande

---

### 3. **Algoritmo Guloso** (`KnapsackGreedy`)

**Algoritmo Aproximado** ⚠️ Sem Garantia de Otimalidade

```java
// Localização: domain/algorithms/KnapsackGreedy.java
```

**Estratégia:**
1. Calcula a razão valor/peso (densidade) de cada item
2. Ordena itens por densidade (decrescente)
3. Seleciona itens até atingir a capacidade

**Características:**
- ⏱️ **Tempo**: O(n log n)
- 💾 **Espaço**: O(n)
- 🎯 **Solução**: Aproximada (tipicamente 90-99% do ótimo)
- 📊 **Uso**: Problemas grandes onde otimalidade não é crítica

**Vantagens:**
- ✅ MUITO rápido
- ✅ Baixo uso de memória
- ✅ Escalável para n > 1.000.000
- ✅ Boas soluções na prática
- ✅ Simples de implementar

**Desvantagens:**
- ❌ Não garante solução ótima
- ❌ Pior caso pode ser arbitrariamente ruim

---

## 📁 Estrutura do Projeto

```
paa_tp_ii/
│
├── src/
│   ├── main/java/paa/knapsack/
│   │   ├── Main.java                          # 🚀 Ponto de entrada
│   │   │
│   │   ├── application/
│   │   │   └── KnapsackStudyStarter.java      # 🎮 Orquestrador de experimentos
│   │   │
│   │   ├── domain/
│   │   │   ├── Item.java                      # 📦 Modelo de item
│   │   │   ├── KnapsackInstance.java          # 🎒 Instância do problema
│   │   │   ├── KnapsackResult.java            # 📊 Resultado da solução
│   │   │   │
│   │   │   ├── algorithms/                    # 🧮 Algoritmos
│   │   │   │   ├── KnapsackDPLinear.java      # DP Espaço Linear
│   │   │   │   ├── KnapsackPD.java            # DP Matriz Completa
│   │   │   │   ├── KnapsackGreedy.java        # Algoritmo Guloso
│   │   │   │   └── KnapsackGreedyReport.java  # Relatório do Guloso
│   │   │   │
│   │   │   ├── performance/
│   │   │   │   └── ExperimentRunner.java      # ⚡ Executor de benchmarks
│   │   │   │
│   │   │   └── testdata/
│   │   │       └── KnapsackInstanceGenerator.java  # 🎲 Gerador de dados
│   │   │
│   │   └── infrastructure/
│   │       └── export/
│   │           └── CSVExporter.java           # 📄 Exportador de resultados
│   │
│   └── test/java/paa/knapsack/
│       └── KnapsackSolverTest.java            # 🧪 Testes unitários
│
├── results/                                    # 📈 Resultados CSV
├── bin/                                        # 🔨 Arquivos compilados
├── pom.xml                                     # 📦 Configuração Maven
├── run.bat                                     # ⚙️ Script de execução
└── README.md                                   # 📖 Este arquivo
```

### Arquitetura

O projeto segue princípios de **Domain-Driven Design (DDD)**:

- **`domain/`**: Lógica de negócio e algoritmos (núcleo do sistema)
- **`application/`**: Casos de uso e orquestração
- **`infrastructure/`**: Detalhes técnicos (I/O, persistência)

---

## 🚀 Como Usar

### Pré-requisitos

- ☕ **Java 17+** instalado
- 📦 **Maven 3.8+** (opcional, para build com Maven)
- 💻 Sistema operacional: Windows, Linux ou macOS

### Opção 1: Usando `run.bat` (Windows - Recomendado)

#### 1️⃣ Compilar e Executar

```batch
run.bat
```

Compila o projeto e executa os estudos completos.

#### 2️⃣ Executar sem Recompilar

```batch
run.bat runonly
```

Executa sem recompilar (útil para execuções rápidas).

#### 3️⃣ Limpar Arquivos

```batch
run.bat clean
```

Remove diretórios `bin/` e `results/`.

---

### Opção 2: Usando Maven

#### Compilar

```bash
mvn clean compile
```

#### Executar

```bash
mvn exec:java
```

#### Gerar JAR Executável

```bash
mvn clean package
java -jar target/paa-tp-ii-1.0-SNAPSHOT.jar
```

#### Executar Testes

```bash
mvn test
```

---

### Opção 3: Compilação Manual

#### Compilar

```bash
mkdir bin
javac -d bin -encoding UTF-8 src/main/java/paa/knapsack/**/*.java
```

#### Executar

```bash
java -cp bin paa.knapsack.Main
```

---

## 📊 Análise de Desempenho

O projeto executa dois estudos principais:

### 1. **Estudo Completo** (`executeCompleteStudy`)

Testa os algoritmos com diferentes tamanhos de entrada:

- **Tamanhos**: 10, 20, 30, 50, 70, 100, 200, 300, 400, 500, 750, 1000, 1500, 2000, 3000, 5000, 7500, 10000
- **Algoritmos**: DP Linear-Space, DP Matriz-Completa, Greedy
- **Métricas**:
  - ⏱️ Tempo de execução (nanossegundos)
  - 💾 Uso de memória
  - 🎯 Valor ótimo encontrado
  - 📈 Qualidade da solução aproximada

### 2. **Estudo Customizado** (`executeCustomStudy`)

Cenário de "capacidade ajustada" (tight capacity):

- **Objetivo**: Testar algoritmos em cenários mais realistas
- **Características**:
  - Capacidade = ~30% do peso total médio
  - Pesos variados (1 a 50)
  - Valores variados (10 a 200)
  - 10 instâncias por tamanho para análise estatística

---

## 📈 Resultados

Os resultados são exportados automaticamente em formato CSV no diretório `results/`:

### Arquivos Gerados

1. **`knapsack_results_complete_study_[timestamp].csv`**
   - Resultados detalhados de cada execução
   - Colunas: n, L, algoritmo, tempo, valor, itens_selecionados, etc.

2. **`knapsack_summary_complete_study_[timestamp].csv`**
   - Sumário agregado por tamanho
   - Estatísticas: média, mediana, desvio padrão

3. **`knapsack_results_custom_study_[timestamp].csv`**
   - Resultados do estudo customizado

4. **`knapsack_summary_custom_study_[timestamp].csv`**
   - Sumário do estudo customizado

### Exemplo de Saída no Console

```
================================================================================
  ESTUDO COMPLETO DO PROBLEMA DA MOCHILA (0/1)
  Algoritmos: DP Linear-Space, DP Matriz-Completa, Greedy (Aproximado)
================================================================================

[KnapsackStudyStarter] Executando bateria de testes com 18 tamanhos distintos...

n=10, L=500 | DP-Linear: 1.2ms | DP-Matriz: 1.5ms | Greedy: 0.3ms
n=100, L=5000 | DP-Linear: 45ms | DP-Matriz: 120ms | Greedy: 2ms
...

================================================================================
  RESUMO DOS RESULTADOS
================================================================================
...

[KnapsackStudyStarter] Análise completada!
[KnapsackStudyStarter] Resultados em: results
```

---

## 🛠️ Tecnologias

- **Linguagem**: Java 17+
- **Build**: Maven 3.8+
- **Testes**: JUnit 4.13.2
- **Formato de Saída**: CSV
- **Padrões de Projeto**: DDD, Strategy, Factory

---

## 📚 Referências

- Cormen, T. H., et al. (2009). *Introduction to Algorithms* (3rd ed.). MIT Press.
- Kellerer, H., Pferschy, U., & Pisinger, D. (2004). *Knapsack Problems*. Springer.
- Vazirani, V. V. (2001). *Approximation Algorithms*. Springer.

---

## 👤 Autor

**Projeto Acadêmico - PAA (Projeto e Análise de Algoritmos)**

---

## 📝 Licença

Este projeto é parte de um trabalho acadêmico e está disponível para fins educacionais
*   `bin/`: Diretório de saída para os arquivos `.class` compilados.
*   `results/`: Diretório de saída para os arquivos CSV gerados pelos experimentos.
*   `pom.xml`: Arquivo de configuração Maven.
*   `run.bat`: Script principal para compilar, executar e limpar o projeto.
*   `README.md`: Este arquivo.
