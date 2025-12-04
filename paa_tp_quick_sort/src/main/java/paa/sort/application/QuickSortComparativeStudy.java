package paa.sort.application;

import paa.sort.domain.SortingAlgorithm;
import paa.sort.domain.algorithms.RecursiveQuickSort;
import paa.sort.domain.algorithms.HybridQuickSort;
import paa.sort.domain.algorithms.ImprovedHybridQuickSort;
import paa.sort.domain.performance.PerformanceResult;
import paa.sort.domain.performance.SortingMetrics;
import paa.sort.domain.performance.ThresholdOptimizer;
import paa.sort.domain.testdata.DataType;
import paa.sort.domain.testdata.TestDataGenerator;
import paa.sort.infrastructure.export.ArrayExporter;
import paa.sort.domain.exceptions.SortingException;
import paa.sort.domain.exceptions.StackOverflowException;
import paa.sort.domain.exceptions.ValidationException;
import paa.sort.infrastructure.logging.ExceptionLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Servico principal para executar o estudo comparativo dos algoritmos Quicksort
 */
public class QuickSortComparativeStudy {
    private final ThresholdOptimizer thresholdOptimizer;
    private final ArrayExporter arrayExporter;
    private final TestDataGenerator testDataGenerator;
    private final ExceptionLogger exceptionLogger;

    // CONFIGURACAO: Numero de execucoes para obter medias confiaveis
    private static final int MULTIPLE_EXECUTIONS = 5;
    private static final int WARMUP_EXECUTIONS = 3;

    public QuickSortComparativeStudy() {
        this.thresholdOptimizer = new ThresholdOptimizer();
        this.arrayExporter = new ArrayExporter();
        this.testDataGenerator = new TestDataGenerator(42);
        this.exceptionLogger = ExceptionLogger.getInstance();
    }

    /**
     * Executa o estudo comparativo completo
     */
    public void executeCompleteStudy() throws SortingException, ValidationException {
        try {
            validateStudyConfiguration();

            System.out.println("=== ESTUDO COMPARATIVO DE ALGORITMOS QUICKSORT ===");
            System.out.println("Configuracao: " + MULTIPLE_EXECUTIONS + " execucoes por teste + " + WARMUP_EXECUTIONS
                    + " aquecimentos");
            System.out.println("Gerando arquivos .txt organizados por tipo de dados...");
            System.out.println();

            List<PerformanceResult> allResults = new ArrayList<>();

            // 1. Determina o threshold otimo
            int optimalThreshold = determineOptimalThreshold();
            exceptionLogger.logInfo("Threshold ótimo determinado: " + optimalThreshold, "Otimização inicial");

            System.out.println();
            System.out.println("=== COMPARACAO DOS ALGORITMOS ===");
            System.out.println();

            // 2. Cria as implementacoes dos algoritmos
            List<SortingAlgorithm> algorithms = createAlgorithms(optimalThreshold);

            // 3. Define os tamanhos de teste
            int[] testSizes = { 100, 500, 1000, 2000, 5000, 10000 };

            // 4. Define os tipos de dados para teste
            DataType[] dataTypes = {
                    DataType.RANDOM,
                    DataType.SORTED,
                    DataType.REVERSE_SORTED,
                    DataType.MANY_DUPLICATES,
                    DataType.WORST_CASE
            };

            // 5. Executa os testes comparativos
            for (DataType dataType : dataTypes) {
                System.out.println("--- Testando com dados: " + dataType.getDescription() + " ---");
                System.out.println();

                List<PerformanceResult> typeResults = new ArrayList<>();

                for (int size : testSizes) {
                    System.out.println("Tamanho do array: " + size);
                    System.out.println("Executando " + MULTIPLE_EXECUTIONS + " vezes cada algoritmo...");

                    // Gera o array original e salva
                    int[] originalArray = testDataGenerator.generateData(dataType, size);
                    arrayExporter.saveOriginalArray(dataType, size, originalArray);

                    List<PerformanceResult> results = new ArrayList<>();

                    for (SortingAlgorithm algorithm : algorithms) {
                        // MUDANCA PRINCIPAL: Executa multiplas vezes e calcula medias
                        PerformanceResult result = testAlgorithmMultipleTimesAndSaveArrays(
                                algorithm, dataType, size, originalArray, MULTIPLE_EXECUTIONS);
                        results.add(result);
                        allResults.add(result);
                        typeResults.add(result);

                        System.out.printf("  %s: %.2f ms | Comp: %d | Trocas: %d%n",
                                result.getAlgorithmName(), result.getExecutionTimeMillis(),
                                result.getComparisons(), result.getSwaps());
                    }

                    // Encontra o melhor resultado
                    PerformanceResult fastest = results.stream()
                            .min((r1, r2) -> Long.compare(r1.getExecutionTimeNanos(), r2.getExecutionTimeNanos()))
                            .orElse(null);

                    if (fastest != null) {
                        System.out.printf("  -> Melhor: %s%n", fastest.getAlgorithmName());
                    }

                    System.out.println();
                }

                // Salva resultados por tipo de dados
                arrayExporter.saveTestResults(typeResults, dataType.getDescription(), dataType);
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

        } catch (ValidationException e) {
            exceptionLogger.logValidationError(e, "Estudo completo interrompido");
            System.err.println("ERRO DE VALIDAÇÃO: " + e.getMessage());
            throw e;
        } catch (SortingException e) {
            exceptionLogger.logSortingException(e, "Estudo completo interrompido");
            System.err.println("ERRO CRÍTICO: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            exceptionLogger.logGenericError("STUDY_EXECUTION_ERROR",
                    "Erro inesperado durante execução do estudo: " + e.getMessage(),
                    e,
                    "Estudo completo");
            System.err.println("ERRO INESPERADO: " + e.getMessage());
            throw new RuntimeException("Falha na execução do estudo comparativo", e);
        } finally {
            // Cleanup resources
            exceptionLogger.logInfo("Estudo comparativo finalizado", "Cleanup");
        }
    }

    /**
     * NOVO METODO: Executa teste multiplas vezes e calcula medias confiaveis
     */
    private PerformanceResult testAlgorithmMultipleTimesAndSaveArrays(
            SortingAlgorithm algorithm, DataType dataType, int size,
            int[] originalArray, int iterations) throws SortingException, ValidationException {

        // Lista para armazenar resultados de todas as execucoes
        List<Long> executionTimes = new ArrayList<>();
        List<Long> comparisonCounts = new ArrayList<>();
        List<Long> swapCounts = new ArrayList<>();
        boolean allSuccessful = true;
        int[] finalSortedArray = null;

        // Aquecimento da JVM
        for (int i = 0; i < WARMUP_EXECUTIONS; i++) {
            try {
                algorithm.sort(originalArray.clone());
            } catch (StackOverflowError e) {
                // Stack overflow durante aquecimento é um sinal de problema
                exceptionLogger.logStackOverflow(
                        new StackOverflowException(algorithm.getName(), dataType.getDescription(), size, 0, 0),
                        "Aquecimento JVM");
                throw new StackOverflowException(algorithm.getName(), dataType.getDescription(), size, 0, 0);
            } catch (Exception e) {
                // Outras exceções durante aquecimento são logadas mas não interrompem
                exceptionLogger.logGenericError("WARMUP_ERROR",
                        "Erro durante aquecimento: " + e.getMessage(),
                        e,
                        "Algoritmo: " + algorithm.getName() + ", Tipo: " + dataType.getDescription() + ", Tamanho: "
                                + size);
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
                    long endTime = System.nanoTime();
                    long executionTime = endTime - startTime;
                    throw new SortingException(
                            "Resultado da ordenação está incorreto",
                            algorithm.getName(),
                            dataType.getDescription(),
                            size,
                            executionTime);
                }

                // Salva o array da primeira execucao bem-sucedida
                if (successful && finalSortedArray == null) {
                    finalSortedArray = sortedArray;
                }

            } catch (StackOverflowError e) {
                successful = false;
                throw new StackOverflowException(
                        algorithm.getName(),
                        dataType.getDescription(),
                        size,
                        calculateRecursionDepth(size),
                        0);
            } catch (SortingException e) {
                successful = false;
                exceptionLogger.logSortingException(e, "Execução " + (i + 1) + "/" + iterations);
                throw e;
            } catch (Exception e) {
                successful = false;
                long endTime = System.nanoTime();
                long executionTime = endTime - startTime;
                SortingException sortingException = new SortingException(
                        "Erro inesperado durante ordenação: " + e.getMessage(),
                        algorithm.getName(),
                        dataType.getDescription(),
                        size,
                        executionTime,
                        e);
                exceptionLogger.logSortingException(sortingException, "Execução " + (i + 1) + "/" + iterations);
                throw sortingException;
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
                averageSwaps);
    }

    /**
     * Verifica se um array esta ordenado
     */
    private boolean isArraySorted(int[] array) {
        if (array == null || array.length <= 1) {
            return true;
        }

        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
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
    private void executeWorstCaseAnalysis(List<SortingAlgorithm> algorithms, List<PerformanceResult> allResults)
            throws ValidationException {
        System.out.println("=== ANALISE DO PIOR CASO ===");
        System.out.println();

        int[] worstCaseSizes = { 100, 200, 500, 1000 };

        System.out.println("Testando com arrays que forcam o pior caso do Quicksort:");
        System.out.println("Executando " + MULTIPLE_EXECUTIONS + " vezes cada teste...");
        System.out.println();

        List<PerformanceResult> worstCaseResults = new ArrayList<>();

        for (int size : worstCaseSizes) {
            System.out.println("Tamanho: " + size);

            // Gera array do pior caso
            int[] worstCaseArray = testDataGenerator.generateData(DataType.WORST_CASE, size);
            arrayExporter.saveOriginalArray(DataType.WORST_CASE, size, worstCaseArray);

            for (SortingAlgorithm algorithm : algorithms) {
                try {
                    // MUDANCA: Usa multiplas execucoes tambem no pior caso
                    PerformanceResult result = testAlgorithmMultipleTimesAndSaveArrays(
                            algorithm, DataType.WORST_CASE, size, worstCaseArray, MULTIPLE_EXECUTIONS);
                    worstCaseResults.add(result);
                    allResults.add(result);

                    System.out.printf("  %s: %.2f ms | Comp: %d | Trocas: %d (Sucesso: %s)%n",
                            result.getAlgorithmName(),
                            result.getExecutionTimeMillis(),
                            result.getComparisons(),
                            result.getSwaps(),
                            result.isSuccessful() ? "Sim" : "Nao");

                } catch (StackOverflowError e) {
                    System.out.printf("  %s: STACK OVERFLOW ERROR%n", algorithm.getName());
                    exceptionLogger.logStackOverflow(
                            new StackOverflowException(algorithm.getName(), "WORST_CASE", size, 0, 0),
                            "Análise do pior caso");
                } catch (SortingException e) {
                    System.out.printf("  %s: ERRO - %s%n", algorithm.getName(), e.getMessage());
                    exceptionLogger.logSortingException(e, "Análise do pior caso");
                }
            }
            System.out.println();
        }

        // Salva resultados da analise do pior caso
        arrayExporter.saveTestResults(worstCaseResults, "Analise_Pior_Caso", DataType.WORST_CASE);
    }

    /**
     * Valida a configuração do estudo antes da execução
     */
    private void validateStudyConfiguration() throws ValidationException {
        if (MULTIPLE_EXECUTIONS <= 0) {
            throw new ValidationException("Número de execuções deve ser positivo", "MULTIPLE_EXECUTIONS",
                    MULTIPLE_EXECUTIONS);
        }

        if (WARMUP_EXECUTIONS < 0) {
            throw new ValidationException("Número de aquecimentos não pode ser negativo", "WARMUP_EXECUTIONS",
                    WARMUP_EXECUTIONS);
        }

        exceptionLogger.logInfo("Configuração do estudo validada com sucesso", "Validação inicial");
    }

    /**
     * Determina o threshold ótimo com tratamento de erro robusto
     */
    private int determineOptimalThreshold() throws SortingException {
        try {
            ThresholdOptimizer.OptimizationResult optimization = thresholdOptimizer.findOptimalThreshold(1000, 10);
            int optimalThreshold = optimization.getOptimalThreshold();

            if (optimalThreshold <= 0) {
                throw new ValidationException("Threshold ótimo inválido", "optimalThreshold", optimalThreshold);
            }

            return optimalThreshold;
        } catch (Exception e) {
            exceptionLogger.logGenericError("THRESHOLD_OPTIMIZATION_ERROR",
                    "Erro ao determinar threshold ótimo: " + e.getMessage(),
                    e,
                    "Otimização de threshold");

            // Usa valor padrão em caso de erro
            int defaultThreshold = 10;
            exceptionLogger.logWarning("Usando threshold padrão: " + defaultThreshold, "Fallback de threshold");
            return defaultThreshold;
        }
    }

    /**
     * Calcula a profundidade de recursão estimada para arrays de um tamanho
     */
    private int calculateRecursionDepth(int arraySize) {
        // Estimativa: log2(n) é a profundidade média esperada
        return (int) Math.ceil(Math.log(arraySize) / Math.log(2));
    }
}