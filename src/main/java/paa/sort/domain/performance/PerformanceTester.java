package paa.sort.domain.performance;

import paa.sort.domain.SortingAlgorithm;
import paa.sort.domain.testdata.DataType;
import paa.sort.domain.testdata.TestDataGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsavel por executar testes de performance dos algoritmos
 */
public class PerformanceTester {
    private final TestDataGenerator dataGenerator;

    public PerformanceTester() {
        this.dataGenerator = new TestDataGenerator(42); // Seed fixo para reproduzibilidade
    }

    /**
     * Executa um teste de performance para um algoritmo especifico
     */
    public PerformanceResult testAlgorithm(SortingAlgorithm algorithm, DataType dataType, int arraySize) {
        int[] testData = dataGenerator.generateData(dataType, arraySize);

        // Aquece a JVM
        warmUp(algorithm, testData);

        long startTime = System.nanoTime();
        boolean successful = true;

        try {
            int[] result = algorithm.sort(testData);
            // Verifica se o resultado esta ordenado
            if (!isSorted(result)) {
                successful = false;
            }
        } catch (Exception e) {
            successful = false;
        }

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        return new PerformanceResult(
            algorithm.getName(),
            dataType.getDescription(),
            arraySize,
            executionTime,
            successful
        );
    }

    /**
     * Executa multiplos testes e retorna a media dos tempos
     */
    public PerformanceResult testAlgorithmMultipleTimes(SortingAlgorithm algorithm, DataType dataType,
                                                       int arraySize, int iterations) {
        List<PerformanceResult> results = new ArrayList<>();

        for (int i = 0; i < iterations; i++) {
            results.add(testAlgorithm(algorithm, dataType, arraySize));
        }

        // Calcula a media dos tempos de execucao
        long averageTime = results.stream()
                .mapToLong(PerformanceResult::getExecutionTimeNanos)
                .sum() / iterations;

        boolean allSuccessful = results.stream().allMatch(PerformanceResult::isSuccessful);

        return new PerformanceResult(
            algorithm.getName(),
            dataType.getDescription(),
            arraySize,
            averageTime,
            allSuccessful
        );
    }

    /**
     * Aquece a JVM executando o algoritmo algumas vezes
     */
    private void warmUp(SortingAlgorithm algorithm, int[] testData) {
        for (int i = 0; i < 5; i++) {
            try {
                algorithm.sort(testData.clone());
            } catch (Exception e) {
                // Ignora excecoes durante o aquecimento
            }
        }
    }

    /**
     * Verifica se um array esta ordenado
     */
    private boolean isSorted(int[] array) {
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
}
