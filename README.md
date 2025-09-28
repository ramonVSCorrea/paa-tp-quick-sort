# Estudo Comparativo de Algoritmos Quicksort

Este projeto implementa um estudo comparativo abrangente de três variações do algoritmo Quicksort, seguindo os princípios de arquitetura limpa (Clean Architecture) e clean code.

## 📋 Objetivo

O objetivo deste trabalho é realizar um estudo comparativo de diversas implementações do algoritmo Quicksort, incluindo:

1. **Quicksort Recursivo**: Implementação tradicional recursiva
2. **Quicksort Híbrido**: Versão que usa Insertion Sort para subarrays pequenos
3. **Quicksort Híbrido Melhorado**: Versão aprimorada com técnica mediana-de-três para escolha do pivô

## 🏗️ Arquitetura do Projeto

O projeto segue os princípios de Clean Architecture, organizando o código em camadas bem definidas:

```
src/main/java/paa/sort/
├── Main.java                          # Ponto de entrada da aplicação
├── application/                       # Camada de Aplicação
│   └── QuickSortComparativeStudy.java # Orquestração do estudo comparativo
├── domain/                           # Camada de Domínio
│   ├── SortingAlgorithm.java         # Interface dos algoritmos de ordenação
│   ├── algorithms/                   # Implementações dos algoritmos
│   │   ├── RecursiveQuickSort.java   # Quicksort recursivo tradicional
│   │   ├── HybridQuickSort.java      # Quicksort híbrido
│   │   ├── ImprovedHybridQuickSort.java # Quicksort híbrido melhorado
│   │   └── InsertionSort.java        # Insertion Sort auxiliar
│   ├── performance/                  # Medição de performance
│   │   ├── PerformanceResult.java    # Resultado de teste de performance
│   │   ├── PerformanceTester.java    # Executor de testes de performance
│   │   └── ThresholdOptimizer.java   # Otimizador de threshold empírico
│   └── testdata/                     # Geração de dados de teste
│       ├── DataType.java             # Tipos de dados de teste
│       └── TestDataGenerator.java    # Gerador de massas de teste
```

## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+ (opcional, para build com Maven)

### Opção 1: Compilação com Maven

Se você tiver o Maven instalado:

```bash
# Compile o projeto
mvn clean compile

# Execute o programa
mvn exec:java -Dexec.mainClass="paa.sort.Main"
```

### Opção 2: Compilação com javac (Recomendada)

Se não tiver Maven, use javac diretamente:

```bash
# Navegue para o diretório do projeto
cd C:\Users\1210499\Desktop\paa-tp-quick-sort

# Compile todos os arquivos Java
javac -d target/classes -sourcepath src/main/java src/main/java/paa/sort/Main.java src/main/java/paa/sort/**/*.java

# Execute o programa
java -cp target/classes paa.sort.Main
```

### Opção 3: Usando um IDE

1. Abra o projeto em sua IDE favorita (IntelliJ IDEA, Eclipse, VS Code)
2. Configure o JDK 17+
3. Execute a classe `paa.sort.Main`

## 📊 Como Funciona

### 1. Determinação Empírica do Threshold

O programa inicia determinando empiricamente o melhor valor de M (threshold) para o Quicksort híbrido:

- Testa valores de M entre 5 e 100
- Usa arrays de 1000 elementos aleatórios
- Executa 10 iterações por valor
- Seleciona o M com menor tempo médio de execução

```java
// Exemplo de saída
Determinando o melhor threshold (M) para o Quicksort hibrido...
Threshold M=5: 0.11 ms
Threshold M=10: 0.19 ms
...
Melhor threshold determinado: M=50 (0.06 ms)
```

### 2. Tipos de Dados Testados

O programa gera e testa cinco tipos diferentes de arrays:

| Tipo | Descrição | Propósito |
|------|-----------|-----------|
| **Aleatorio** | Elementos distribuídos aleatoriamente | Caso médio típico |
| **Ordenado** | Array já ordenado (0, 1, 2, ..., n-1) | Testa eficiência em dados ordenados |
| **Ordenado Inverso** | Array em ordem decrescente (n-1, n-2, ..., 0) | Testa comportamento em ordem inversa |
| **Muitos Duplicados** | 90% de elementos duplicados | Testa eficiência com muitas repetições |
| **Pior Caso** | Força explicitamente O(n²) | Demonstra limitações do Quicksort tradicional |

### 3. Algoritmos Implementados

#### Quicksort Recursivo (Tradicional)
```java
// Implementação clássica com particionamento simples
// Pivot: último elemento do subarray
// Complexidade: O(n log n) médio, O(n²) pior caso
```

#### Quicksort Híbrido
```java
// Otimização: usa Insertion Sort para subarrays pequenos
// Threshold: determinado empiricamente (tipicamente ~50)
// Melhoria: evita overhead da recursão em subarrays pequenos
```

#### Quicksort Híbrido Melhorado
```java
// Todas as otimizações do híbrido +
// Mediana-de-três para escolha do pivô
// Benefício: evita o pior caso O(n²) em dados ordenados
```

### 4. Testes de Performance

Para cada combinação algoritmo + tipo de dados + tamanho:

- **Aquecimento**: 5 execuções para aquecer a JVM
- **Medição**: Tempo em nanossegundos usando `System.nanoTime()`
- **Validação**: Verifica se o resultado está ordenado
- **Média**: Calcula média de 5 execuções para maior precisão

### 5. Tamanhos de Teste

O programa testa arrays de tamanhos variados:
- 100, 500, 1000, 2000, 5000, 10000 elementos

### 6. Análise do Pior Caso

Uma seção especial força explicitamente o pior caso do Quicksort:
- Arrays ordenados com pivô sempre sendo o último elemento
- Demonstra quando o Quicksort degrada para O(n²)
- Mostra como a mediana-de-três resolve este problema

## 📈 Interpretação dos Resultados

### Exemplo de Saída Típica:

```
--- Testando com dados: Ordenado ---
Tamanho do array: 10000
  Quicksort Recursivo: 128.70 ms
  Quicksort Hibrido (M=50): 128.64 ms  
  Quicksort Hibrido Melhorado (M=50, Mediana-de-3): 0.16 ms
  -> Melhor: Quicksort Hibrido Melhorado
```

### Padrões Esperados:

1. **Arrays Pequenos (≤500)**: Diferenças mínimas entre algoritmos
2. **Arrays Médios (1000-2000)**: Híbrido começa a mostrar vantagens
3. **Arrays Grandes (≥5000)**: Híbrido Melhorado domina consistentemente
4. **Dados Ordenados**: Melhorado supera drasticamente os outros (128ms vs 0.16ms)
5. **Pior Caso**: Só o Melhorado mantém O(n log n)

## 🔬 Conceitos Técnicos Implementados

### Clean Architecture
- **Separação de responsabilidades**: Cada camada tem uma responsabilidade específica
- **Inversão de dependência**: Interfaces definem contratos
- **Testabilidade**: Código facilmente testável e mock-able

### Otimizações de Performance
- **JVM Warmup**: Aquecimento para medições precisas
- **Seed fixa**: Reproduzibilidade dos testes (`Random(42)`)
- **Clonagem de arrays**: Testes independentes
- **Nano precisão**: Medição em nanossegundos

### Algoritmos
- **Particionamento Lomuto**: Esquema de partição usado
- **Mediana-de-três**: Técnica para escolha otimizada do pivô
- **Insertion Sort**: Para subarrays pequenos (M ≤ threshold)
- **Análise empírica**: Determinação científica do threshold ótimo

## 🎯 Resultados Esperados

O estudo deve demonstrar que:

1. **Quicksort Híbrido Melhorado** é superior na maioria dos casos
2. **Mediana-de-três** resolve o problema do pior caso
3. **Threshold empírico** (~50) oferece melhor balance
4. **Dados ordenados** expõem as limitações do Quicksort tradicional
5. **Clean Architecture** facilita manutenção e extensibilidade

## 📝 Observações

- O programa usa codificação ASCII para evitar problemas de caracteres especiais
- Todos os tempos são calculados como média de múltiplas execuções
- A saída inclui validação de correção (arrays realmente ordenados)
- Stack overflow pode ocorrer com Quicksort tradicional em casos extremos

---

**Desenvolvido como trabalho prático de Projeto e Análise de Algoritmos (PAA)**
