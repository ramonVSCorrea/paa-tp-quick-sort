package paa.sort.domain.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import paa.sort.domain.SortingAlgorithm;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o algoritmo ImprovedHybridQuickSort
 */
@DisplayName("Testes do Quicksort Hibrido Melhorado")
class ImprovedHybridQuickSortTest extends SortingAlgorithmTestBase {

    @Override
    protected SortingAlgorithm createAlgorithm() {
        return new ImprovedHybridQuickSort(10); // Threshold padrao de 10
    }

    @Test
    @DisplayName("Teste com threshold pequeno (5)")
    void testComThresholdPequeno() {
        SortingAlgorithm algorithm = new ImprovedHybridQuickSort(5);
        int[] array = { 5, 2, 8, 1, 9, 3, 7, 4, 6, 10 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        int[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        assertArrayEquals(expected, result, "Array deve estar ordenado com threshold pequeno");
    }

    @Test
    @DisplayName("Teste com threshold grande (50)")
    void testComThresholdGrande() {
        SortingAlgorithm algorithm = new ImprovedHybridQuickSort(50);
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
        SortingAlgorithm algorithm = new ImprovedHybridQuickSort(arraySize);
        int[] array = { 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        for (int i = 0; i < result.length - 1; i++) {
            assertTrue(result[i] <= result[i + 1], "Array deve estar ordenado mesmo quando threshold = tamanho");
        }
    }

    @Test
    @DisplayName("Teste de mediana-de-tres com array de 3 elementos")
    void testMedianaDeTresComTresElementos() {
        SortingAlgorithm algorithm = new ImprovedHybridQuickSort(1); // Threshold pequeno
        int[] array = { 3, 1, 2 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        int[] expected = { 1, 2, 3 };
        assertArrayEquals(expected, result, "Array de 3 elementos deve estar ordenado");
    }

    @Test
    @DisplayName("Teste de mediana-de-tres com caso pior do quicksort tradicional")
    void testMedianaDeTresComPiorCaso() {
        // Array ordenado seria pior caso para quicksort tradicional
        // Mas mediana-de-tres deve lidar melhor
        SortingAlgorithm algorithm = new ImprovedHybridQuickSort(5);
        int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        for (int i = 0; i < result.length - 1; i++) {
            assertTrue(result[i] <= result[i + 1], "Array deve estar ordenado mesmo no pior caso tradicional");
        }
    }
}

