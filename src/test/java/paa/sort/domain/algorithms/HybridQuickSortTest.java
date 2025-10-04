package paa.sort.domain.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import paa.sort.domain.SortingAlgorithm;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o algoritmo HybridQuickSort
 */
@DisplayName("Testes do Quicksort Hibrido")
class HybridQuickSortTest extends SortingAlgorithmTestBase {

    @Override
    protected SortingAlgorithm createAlgorithm() {
        return new HybridQuickSort(10); // Threshold padrao de 10
    }

    @Test
    @DisplayName("Teste com threshold pequeno (5)")
    void testComThresholdPequeno() {
        SortingAlgorithm algorithm = new HybridQuickSort(5);
        int[] array = { 5, 2, 8, 1, 9, 3, 7, 4, 6, 10 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        int[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        assertArrayEquals(expected, result, "Array deve estar ordenado com threshold pequeno");
    }

    @Test
    @DisplayName("Teste com threshold grande (50)")
    void testComThresholdGrande() {
        SortingAlgorithm algorithm = new HybridQuickSort(50);
        int[] array = new int[100];
        for (int i = 0; i < 100; i++) {
            array[i] = 100 - i;
        }

        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        for (int i = 0; i < result.length - 1; i++) {
            assertTrue(result[i] <= result[i + 1], "Array deve estar ordenado com threshold grande");
        }
    }

    @Test
    @DisplayName("Teste com threshold igual ao tamanho do array")
    void testComThresholdIgualTamanhoArray() {
        int arraySize = 20;
        SortingAlgorithm algorithm = new HybridQuickSort(arraySize);
        int[] array = { 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        for (int i = 0; i < result.length - 1; i++) {
            assertTrue(result[i] <= result[i + 1], "Array deve estar ordenado mesmo quando threshold = tamanho");
        }
    }
}

