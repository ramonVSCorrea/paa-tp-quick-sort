package paa.sort.application;

import paa.sort.domain.SortingAlgorithm;
import paa.sort.domain.algorithms.RecursiveQuickSort;
import paa.sort.domain.algorithms.HybridQuickSort;
import paa.sort.domain.algorithms.ImprovedHybridQuickSort;
import paa.sort.domain.performance.PerformanceTester;
import paa.sort.domain.performance.PerformanceResult;
import paa.sort.domain.performance.SortingMetrics;
import paa.sort.domain.performance.ThresholdOptimizer;
import paa.sort.domain.testdata.DataType;
import paa.sort.domain.testdata.TestDataGenerator;
import paa.sort.infrastructure.export.ArrayExporter;

import java.util.ArrayList;
import java.util.List;

/**
 * Servico principal para executar o estudo comparativo dos algoritmos Quicksort
 */
public class QuickSortComparativeStudy {
    private final PerformanceTester performanceTester;
    private final ThresholdOptimizer thresholdOptimizer;
    private final ArrayExporter arrayExporter;
    private final TestDataGenerator testDataGenerator;

    // CONFIGURACAO: Numero de execucoes para obter medias confiaveis
    private static final int DEFAULT_MULTIPLE_EXECUTIONS = 5;
    private static final int DEFAULT_WARMUP_EXECUTIONS = 3;
    private static final int[] DEFAULT_TEST_SIZES = {100, 500, 1000, 2000, 5000, 10000};
    private static final int[] WORST_CASE_TEST_SIZES = {100, 200, 500, 1000};
    private static final int THRESHOLD_OPTIMIZATION_ARRAY_SIZE = 1000;
    private static final int THRESHOLD_OPTIMIZATION_ITERATIONS = 10;
    private static final long RANDOM_SEED = 42;

    public QuickSortComparativeStudy() {
        this.performanceTester = new PerformanceTester();
        this.thresholdOptimizer = new ThresholdOptimizer();
        this.arrayExporter = new ArrayExporter();
        this.testDataGenerator = new TestDataGenerator(RANDOM_SEED);
    }

    /**
     * Executa o estudo comparativo completo
     */
    public void executeCompleteStudy() {
        System.out.println("=== ESTUDO COMPARATIVO DE ALGORITMOS QUICKSORT ===");
        System.out.println("Configuracao: " + DEFAULT_MULTIPLE_EXECUTIONS + " execucoes por teste + " + DEFAULT_WARMUP_EXECUTIONS + " aquecimentos");
        System.out.println("Gerando arquivos .txt organizados por tipo de dados...");
        System.out.println();

        List<PerformanceResult> allResults = new ArrayList<>();

        // 1. Determina o threshold otimo
        ThresholdOptimizer.OptimizationResult optimization =
            thresholdOptimizer.findOptimalThreshold(THRESHOLD_OPTIMIZATION_ARRAY_SIZE, THRESHOLD_OPTIMIZATION_ITERATIONS);
        int optimalThreshold = optimization.getOptimalThreshold();

        System.out.println();
        System.out.println("=== COMPARACAO DOS ALGORITMOS ===");
        System.out.println();

        // 2. Cria as implementacoes dos algoritmos
        List<SortingAlgorithm> algorithms = createAlgorithms(optimalThreshold);

        // 3. Define os tamanhos de teste
        int[] testSizes = DEFAULT_TEST_SIZES;

        // 4. Define os tipos de dados para teste
        DataType[] dataTypes = {
            DataType.RANDOM,
            DataType.SORTED,
            DataType.REVERSE_SORTED,
            DataType.MANY_DUPLICATES,
            DataType.WORST_CASE
        };

        // 5. Executa os testes comparativos
        for (DataType currentDataType : dataTypes) {
            System.out.println("--- Testando com dados: " + currentDataType.getDescription() + " ---");
            System.out.println();

            List<PerformanceResult> resultsForCurrentDataType = new ArrayList<>();

            for (int arraySize : testSizes) {
                System.out.println("Tamanho do array: " + arraySize);
                System.out.println("Executando " + DEFAULT_MULTIPLE_EXECUTIONS + " vezes cada algoritmo...");

                // Gera o array original e salva
                int[] originalUnsortedArray = testDataGenerator.generateData(currentDataType, arraySize);
                arrayExporter.saveOriginalArray(currentDataType, arraySize, originalUnsortedArray);

                List<PerformanceResult> resultsForCurrentSize = new ArrayList<>();

                for (SortingAlgorithm algorithmUnderTest : algorithms) {
                    // MUDANCA PRINCIPAL: Executa multiplas vezes e calcula medias
                    PerformanceResult result = testAlgorithmMultipleTimesAndSaveArrays(
                        algorithmUnderTest, currentDataType, arraySize, originalUnsortedArray, DEFAULT_MULTIPLE_EXECUTIONS
                    );
                    resultsForCurrentSize.add(result);
                    allResults.add(result);
                    resultsForCurrentDataType.add(result);

                    System.out.printf("  %s: %.2f ms | Comp: %d | Trocas: %d%n",
                        result.getAlgorithmName(), result.getExecutionTimeMillis(),
                        result.getComparisons(), result.getSwaps());
                }

                // Encontra o melhor resultado
                PerformanceResult fastestAlgorithmResult = resultsForCurrentSize.stream()
                        .min((r1, r2) -> Long.compare(r1.getExecutionTimeNanos(), r2.getExecutionTimeNanos()))
                        .orElse(null);

                if (fastestAlgorithmResult != null) {
                    System.out.printf("  -> Melhor: %s%n", fastestAlgorithmResult.getAlgorithmName());
                }

                System.out.println();
            }

            // Salva resultados por tipo de dados
            arrayExporter.saveTestResults(resultsForCurrentDataType, currentDataType.getDescription(), currentDataType);
            System.out.println();
        }

        // 6. Executa teste especifico do pior caso
        executeWorstCaseAnalysis(algorithms, allResults);

        // 7. Salva resumo geral
        arrayExporter.saveGeneralSummary(allResults, optimalThreshold);

        System.out.println();
        System.out.println("=== ARQUIVOS GERADOS ===");
        System.out.println("Estrutura organizada criada em 'arrays_testados/':");
        System.out.println("- Cada tipo de dados tem sua pasta separada");
        System.out.println("- Arrays originais em /arrays_originais/");
        System.out.println("- Arrays ordenados em /arrays_ordenados/");
        System.out.println("- Resultados em /resultados/");
        System.out.println("- Todos os elementos dos arrays sao exibidos");
        System.out.println();
        System.out.println("Estudo comparativo concluido com sucesso!");
    }

    /**
     * NOVO METODO: Executa teste multiplas vezes e calcula medias confiaveis
     */
    private PerformanceResult testAlgorithmMultipleTimesAndSaveArrays(
            SortingAlgorithm algorithm, DataType dataType, int size,
            int[] originalArray, int iterations) {

        // Lista para armazenar resultados de todas as execucoes
        List<Long> executionTimes = new ArrayList<>();
        List<Long> comparisonCounts = new ArrayList<>();
        List<Long> swapCounts = new ArrayList<>();
        boolean allSuccessful = true;
        int[] finalSortedArray = null;

        // Aquecimento da JVM
        for (int i = 0; i < DEFAULT_WARMUP_EXECUTIONS; i++) {
            try {
                algorithm.sort(originalArray.clone());
            } catch (Exception e) {
                // Ignora excecoes durante aquecimento
            }
        }

        // Executa o algoritmo multiplas vezes com as MESMAS massas de dados
        for (int i = 0; i < iterations; i++) {
            SortingMetrics metrics = new SortingMetrics();
            int[] testData = originalArray.clone(); // MESMA massa de dados

            long startTime = System.nanoTime();
            boolean successful = true;
            int[] sortedArray = null;

            try {
                sortedArray = algorithm.sort(testData, metrics);

                // Verifica se o resultado esta ordenado
                if (!isArraySorted(sortedArray)) {
                    successful = false;
                }

                // Salva o array da primeira execucao bem-sucedida
                if (successful && finalSortedArray == null) {
                    finalSortedArray = sortedArray;
                }

            } catch (Exception e) {
                successful = false;
            }

            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;

            // Coleta metricas desta execucao
            executionTimes.add(executionTime);
            comparisonCounts.add(metrics.getComparisons());
            swapCounts.add(metrics.getSwaps());

            if (!successful) {
                allSuccessful = false;
            }
        }

        // Calcula medias
        long averageTime = executionTimes.stream().mapToLong(Long::longValue).sum() / iterations;
        long averageComparisons = comparisonCounts.stream().mapToLong(Long::longValue).sum() / iterations;
        long averageSwaps = swapCounts.stream().mapToLong(Long::longValue).sum() / iterations;

        // Salva o array ordenado (da primeira execucao bem-sucedida)
        if (finalSortedArray != null) {
            arrayExporter.saveSortedArray(algorithm.getName(), dataType, size, finalSortedArray);
        }

        return new PerformanceResult(
            algorithm.getName(),
            dataType.getDescription(),
            size,
            averageTime,
            allSuccessful,
            averageComparisons,
            averageSwaps
        );
    }

    /**
     * Verifica se um array esta ordenado
     */
    private boolean isArraySorted(int[] array) {
        if (array == null || array.length <= 1) {
            return true;
        }

        for (int currentIndex = 1; currentIndex < array.length; currentIndex++) {
            if (array[currentIndex] < array[currentIndex - 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Cria as instancias dos algoritmos para comparacao
     */
    private List<SortingAlgorithm> createAlgorithms(int optimalThreshold) {
        List<SortingAlgorithm> algorithms = new ArrayList<>();

        algorithms.add(new RecursiveQuickSort());
        algorithms.add(new HybridQuickSort(optimalThreshold));
        algorithms.add(new ImprovedHybridQuickSort(optimalThreshold));

        return algorithms;
    }

    /**
     * Executa analise especifica do pior caso
     */
    private void executeWorstCaseAnalysis(List<SortingAlgorithm> algorithms, List<PerformanceResult> allResults) {
        System.out.println("=== ANALISE DO PIOR CASO ===");
        System.out.println();

        int[] worstCaseArraySizes = WORST_CASE_TEST_SIZES;

        System.out.println("Testando com arrays que forcam o pior caso do Quicksort:");
        System.out.println("Executando " + DEFAULT_MULTIPLE_EXECUTIONS + " vezes cada teste...");
        System.out.println();

        List<PerformanceResult> worstCaseResults = new ArrayList<>();

        for (int arraySize : worstCaseArraySizes) {
            System.out.println("Tamanho: " + arraySize);

            // Gera array do pior caso
            int[] worstCaseArray = testDataGenerator.generateData(DataType.WORST_CASE, arraySize);
            arrayExporter.saveOriginalArray(DataType.WORST_CASE, arraySize, worstCaseArray);

            for (SortingAlgorithm algorithmUnderTest : algorithms) {
                try {
                    // MUDANCA: Usa multiplas execucoes tambem no pior caso
                    PerformanceResult result = testAlgorithmMultipleTimesAndSaveArrays(
                        algorithmUnderTest, DataType.WORST_CASE, arraySize, worstCaseArray, DEFAULT_MULTIPLE_EXECUTIONS
                    );
                    worstCaseResults.add(result);
                    allResults.add(result);

                    System.out.printf("  %s: %.2f ms | Comp: %d | Trocas: %d (Sucesso: %s)%n",
                        result.getAlgorithmName(),
                        result.getExecutionTimeMillis(),
                        result.getComparisons(),
                        result.getSwaps(),
                        result.isSuccessful() ? "Sim" : "Nao"
                    );

                } catch (StackOverflowError e) {
                    System.out.printf("  %s: STACK OVERFLOW ERROR%n", algorithmUnderTest.getName());
                } catch (Exception e) {
                    System.out.printf("  %s: ERRO - %s%n", algorithmUnderTest.getName(), e.getMessage());
                }
            }
            System.out.println();
        }

        // Salva resultados da analise do pior caso
        arrayExporter.saveTestResults(worstCaseResults, "Analise_Pior_Caso", DataType.WORST_CASE);
    }
}
