package paa.sort.application;

import paa.sort.domain.SortingAlgorithm;
import paa.sort.domain.algorithms.RecursiveQuickSort;
import paa.sort.domain.algorithms.HybridQuickSort;
import paa.sort.domain.algorithms.ImprovedHybridQuickSort;
import paa.sort.domain.performance.PerformanceTester;
import paa.sort.domain.performance.PerformanceResult;
import paa.sort.domain.performance.ThresholdOptimizer;
import paa.sort.domain.testdata.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Servico principal para executar o estudo comparativo dos algoritmos Quicksort
 */
public class QuickSortComparativeStudy {
    private final PerformanceTester performanceTester;
    private final ThresholdOptimizer thresholdOptimizer;

    public QuickSortComparativeStudy() {
        this.performanceTester = new PerformanceTester();
        this.thresholdOptimizer = new ThresholdOptimizer();
    }

    /**
     * Executa o estudo comparativo completo
     */
    public void executeCompleteStudy() {
        System.out.println("=== ESTUDO COMPARATIVO DE ALGORITMOS QUICKSORT ===");
        System.out.println();

        // 1. Determina o threshold otimo
        ThresholdOptimizer.OptimizationResult optimization =
            thresholdOptimizer.findOptimalThreshold(1000, 10);
        int optimalThreshold = optimization.getOptimalThreshold();

        System.out.println();
        System.out.println("=== COMPARACAO DOS ALGORITMOS ===");
        System.out.println();

        // 2. Cria as implementacoes dos algoritmos
        List<SortingAlgorithm> algorithms = createAlgorithms(optimalThreshold);

        // 3. Define os tamanhos de teste
        int[] testSizes = {100, 500, 1000, 2000, 5000, 10000};

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

            for (int size : testSizes) {
                System.out.println("Tamanho do array: " + size);

                List<PerformanceResult> results = new ArrayList<>();

                for (SortingAlgorithm algorithm : algorithms) {
                    PerformanceResult result = performanceTester.testAlgorithmMultipleTimes(
                        algorithm, dataType, size, 5
                    );
                    results.add(result);

                    System.out.printf("  %s: %.2f ms%n",
                        result.getAlgorithmName(), result.getExecutionTimeMillis());
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

            System.out.println();
        }

        // 6. Executa teste especifico do pior caso
        executeWorstCaseAnalysis(algorithms);
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
    private void executeWorstCaseAnalysis(List<SortingAlgorithm> algorithms) {
        System.out.println("=== ANALISE DO PIOR CASO ===");
        System.out.println();

        int[] worstCaseSizes = {100, 200, 500, 1000};

        System.out.println("Testando com arrays que forcam o pior caso do Quicksort:");
        System.out.println();

        for (int size : worstCaseSizes) {
            System.out.println("Tamanho: " + size);

            for (SortingAlgorithm algorithm : algorithms) {
                try {
                    PerformanceResult result = performanceTester.testAlgorithm(
                        algorithm, DataType.WORST_CASE, size
                    );

                    System.out.printf("  %s: %.2f ms (Sucesso: %s)%n",
                        result.getAlgorithmName(),
                        result.getExecutionTimeMillis(),
                        result.isSuccessful() ? "Sim" : "Nao"
                    );

                } catch (StackOverflowError e) {
                    System.out.printf("  %s: STACK OVERFLOW ERROR%n", algorithm.getName());
                } catch (Exception e) {
                    System.out.printf("  %s: ERRO - %s%n", algorithm.getName(), e.getMessage());
                }
            }
            System.out.println();
        }
    }
}
