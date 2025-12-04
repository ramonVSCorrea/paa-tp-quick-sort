# Estudo Comparativo de Algoritmos Quicksort

Este projeto implementa um estudo comparativo abrangente de três variações do algoritmo Quicksort, seguindo os princípios de arquitetura limpa (Clean Architecture) e clean code.

## 📋 Objetivo

O objetivo deste trabalho é realizar um estudo comparativo de diversas implementações do algoritmo Quicksort, incluindo:

1. **Quicksort Recursivo**: Implementação tradicional recursiva
2. **Quicksort Híbrido**: Versão que usa Insertion Sort para subarrays pequenos
3. **Quicksort Híbrido Melhorado**: Versão aprimorada com técnica mediana-de-três para escolha do pivô

## 🎯 Características da Medição de Performance

Execuções Múltiplas para Médias Confiáveis

O projeto foi desenvolvido para garantir resultados estatisticamente confiáveis:

#### Configuração de Execuções:
- **5 execuções** de cada algoritmo com as **mesmas massas de dados**
- **3 execuções de aquecimento** da JVM antes das medições reais
- **Cálculo de médias** automático para tempos, comparações e trocas
- **Validação de ordenação** em todas as execuções

#### Como Funciona:
```java
// Para cada algoritmo e tamanho de array:
1. Gera a massa de dados UMA vez
2. Executa 3 aquecimentos da JVM (descartados)
3. Executa 5 medições reais com a MESMA massa de dados
4. Calcula média aritmética dos resultados
5. Retorna resultado médio confiável
```

#### Benefícios desta Abordagem:
- ✅ **Eliminação de variações pontuais** de performance
- ✅ **Aquecimento da JVM** para medições mais precisas  
- ✅ **Comparação justa** - todos os algoritmos testam os mesmos dados
- ✅ **Resultados estatisticamente confiáveis**
- ✅ **Reproduzibilidade** com seed fixa (42)

#### Saída Melhorada:
```
Tamanho do array: 1000
Executando 5 vezes cada algoritmo...
  Quicksort Recursivo: 0.15 ms | Comp: 8543 | Trocas: 2104
  Quicksort Hibrido (M=50): 0.12 ms | Comp: 8234 | Trocas: 1987  
  Quicksort Hibrido Melhorado: 0.09 ms | Comp: 7891 | Trocas: 1823
  -> Melhor: Quicksort Hibrido Melhorado
```

*Cada valor mostrado é a **média de 5 execuções** com os mesmos dados.*

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

### 2. Exportação Automática de Arrays e Resultados

**FUNCIONALIDADE**: Para cada array testado, o programa automaticamente gera arquivos .txt organizados em uma estrutura hierárquica no diretório `arrays_testados/`:

#### Estrutura de Diretórios:
```
arrays_testados/
├── aleatorio/
│   ├── arrays_originais/
│   │   ├── array_original_random_100.txt
│   │   ├── array_original_random_500.txt
│   │   └── ... (outros tamanhos)
│   ├── arrays_ordenados/
│   │   ├── array_ordenado_Quicksort_Recursivo_random_100.txt
│   │   ├── array_ordenado_Quicksort_Hibrido_random_100.txt
│   │   └── ... (todos algoritmos e tamanhos)
│   └── resultados/
│       └── resultados_aleatorio.txt
├── ordenado/
│   ├── arrays_originais/
│   ├── arrays_ordenados/
│   └── resultados/
├── ordenado_inverso/
├── muitos_duplicados/
└── pior_caso/
```

#### Tipos de Arquivos Gerados:

1. **Arrays Originais** - `arrays_originais/array_original_[tipo]_[tamanho].txt`
   - Contém o array antes da ordenação
   - **TODOS os elementos são exibidos** (não há omissão)
   - Organizados por tipo de dados

2. **Arrays Ordenados** - `arrays_ordenados/array_ordenado_[algoritmo]_[tipo]_[tamanho].txt`
   - Contém o array após ordenação por cada algoritmo
   - **TODOS os elementos são exibidos** com verificação de ordenação
   - Um arquivo por algoritmo testado
   - Inclui verificação automática: "ORDENADO CORRETAMENTE"

3. **Resultados** - `resultados/resultados_[tipo].txt`
   - Performance consolidada por tipo de dados
   - Tempos em ms e nanosegundos
   - Status de sucesso/falha de cada teste

4. **Resumo Geral** - `resumo_geral.txt`
   - Visão consolidada de todos os testes no diretório raiz
   - Threshold ótimo determinado
   - Mapa da estrutura de arquivos gerada

#### Características da Exportação:

- **Organização Hierárquica**: Cada tipo de dados tem sua pasta separada
- **Sobreescrita Inteligente**: Cada execução substitui os arquivos anteriores
- **Exibição Completa**: Todos os elementos dos arrays são mostrados (não há "..." ou omissões)
- **Nomes Limpos**: Sem timestamps, nomes de arquivos consistentes
- **Estrutura Padronizada**: Fácil navegação e localização dos dados

#### Exemplo de Arquivo de Array Completo:
```
========================================
ARRAY ORIGINAL TESTADO
========================================
Tipo: Aleatorio
Tamanho: 100 elementos
Data/Hora: 28/09/2025 12:15:30
----------------------------------------
Elementos:
[0] = 45
[1] = 23
[2] = 78
[3] = 12
...
[98] = 34
[99] = 67

Total de elementos: 100
```

### 3. Tipos de Dados Testados

O programa gera e testa cinco tipos diferentes de arrays:

| Tipo | Descrição | Propósito |
|------|-----------|-----------|
| **Aleatorio** | Elementos distribuídos aleatoriamente | Caso médio típico |
| **Ordenado** | Array já ordenado (0, 1, 2, ..., n-1) | Testa eficiência em dados ordenados |
| **Ordenado Inverso** | Array em ordem decrescente (n-1, n-2, ..., 0) | Testa comportamento em ordem inversa |
| **Muitos Duplicados** | 90% de elementos duplicados | Testa eficiência com muitas repetições |
| **Pior Caso** | Força explicitamente O(n²) | Demonstra limitações do Quicksort tradicional |

### 4. Algoritmos Implementados

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

### 5. Testes de Performance

Para cada combinação algoritmo + tipo de dados + tamanho:

- **Aquecimento da JVM**: 3 execuções descartadas para otimizar a JVM
- **Execuções Múltiplas**: 5 execuções reais com as **mesmas massas de dados**
- **Medição Precisa**: Tempo em nanossegundos usando `System.nanoTime()`
- **Cálculo de Médias**: Média aritmética de tempos, comparações e trocas
- **Validação Rigorosa**: Verifica ordenação correta em todas as execuções
- **Detecção de Falhas**: Marca testes que falharam em qualquer execução

#### Processo de Medição Detalhado:
1. **Geração de Dados**: Array original criado uma única vez
2. **Aquecimento**: 3 execuções para otimizar a JVM (resultados descartados)
3. **Execuções Reais**: 5 execuções independentes com clones do array original
4. **Coleta de Métricas**: Tempo, comparações e trocas de cada execução
5. **Cálculo Estatístico**: Médias aritméticas dos resultados coletados
6. **Validação**: Verificação de ordenação correta em todas as execuções

## 📈 Interpretação dos Resultados

### ⭐ **IMPORTANTE**: Os Resultados São Médias Confiáveis

**Todos os valores apresentados são médias de 5 execuções independentes** com as mesmas massas de dados, garantindo:
- Eliminação de flutuações pontuais de performance
- Resultados estatisticamente representativos
- Comparações justas entre algoritmos

### Exemplo de Saída Melhorada:

```
--- Testando com dados: Ordenado ---
Configuracao: 5 execucoes por teste + 3 aquecimentos

Tamanho do array: 10000
Executando 5 vezes cada algoritmo...
  Quicksort Recursivo: 128.70 ms | Comp: 49995000 | Trocas: 0
  Quicksort Hibrido (M=50): 128.64 ms | Comp: 49994950 | Trocas: 0  
  Quicksort Hibrido Melhorado (M=50, Mediana-de-3): 0.16 ms | Comp: 19998 | Trocas: 0
  -> Melhor: Quicksort Hibrido Melhorado

*Cada valor é a média de 5 execuções com os mesmos dados*
```

### Padrões Esperados com Maior Confiabilidade:

1. **Arrays Pequenos (≤500)**: Diferenças mínimas entre algoritmos (médias estáveis)
2. **Arrays Médios (1000-2000)**: Híbrido começa a mostrar vantagens consistentes
3. **Arrays Grandes (≥5000)**: Híbrido Melhorado domina de forma reproduzível
4. **Dados ordenados** expõem dramaticamente as limitações do Quicksort tradicional
5. **Pior Caso**: Apenas o Melhorado mantém O(n log n) de forma consistente

## 🔬 Conceitos Técnicos Implementados

### Clean Architecture
- **Separação de responsabilidades**: Cada camada tem uma responsabilidade específica
- **Inversão de dependência**: Interfaces definem contratos
- **Testabilidade**: Código facilmente testável e mock-able

### ⭐ **APRIMORAMENTOS**: Otimizações de Performance e Confiabilidade
- **Execuções Múltiplas**: 5 execuções reais + 3 aquecimentos para resultados confiáveis
- **JVM Warmup**: Aquecimento específico para medições precisas
- **Seed fixa**: Reproduzibilidade absoluta dos testes (`Random(42)`)
- **Clonagem independente**: Cada execução usa clone do array original
- **Nano precisão**: Medição em nanossegundos com `System.nanoTime()`
- **Validação rigorosa**: Verificação de ordenação em todas as execuções
- **Cálculos estatísticos**: Médias automáticas de tempo, comparações e trocas

### Metodologia Científica
- **Controle de variáveis**: Mesmos dados para todos os algoritmos
- **Repetibilidade**: Múltiplas execuções para eliminar outliers
- **Validação**: Verificação automática da correção dos resultados
- **Precisão**: Medições em nanossegundos com aquecimento da JVM

## 🎯 Resultados Esperados

O estudo demonstra com **alta confiabilidade estatística** que:

1. **Quicksort Híbrido Melhorado** é consistentemente superior na maioria dos casos
2. **Mediana-de-três** resolve efetivamente o problema do pior caso
3. **Threshold empírico** (~50) oferece o melhor balance de forma reproduzível
4. **Dados ordenados** expõem dramaticamente as limitações do Quicksort tradicional
5. **Execuções múltiplas** eliminam variações e fornecem resultados confiáveis
6. **Clean Architecture** facilita manutenção, extensibilidade e validação

## 📝 Observações

- O programa usa codificação ASCII para evitar problemas de caracteres especiais
- Todos os tempos são médias de 5 execuções independentes com aquecimento da JVM
- Cada algoritmo é testado exatamente com as mesmas massas de dados
- A saída inclui validação rigorosa de correção (arrays realmente ordenados em todas as execuções)
- Stack overflow pode ocorrer com Quicksort tradicional em casos extremos
- Configuração facilmente ajustável de número de execuções (MULTIPLE_EXECUTIONS e WARMUP_EXECUTIONS)

---

**Desenvolvido como trabalho prático de Projeto e Análise de Algoritmos (PAA)**
**Implementação com execuções múltiplas para resultados estatisticamente confiáveis**
