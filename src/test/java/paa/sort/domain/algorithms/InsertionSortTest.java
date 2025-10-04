package paa.sort.domain.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import paa.sort.domain.performance.SortingMetrics;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o algoritmo InsertionSort (usado internamente pelos hibridos)
 */
@DisplayName("Testes do Insertion Sort")
class InsertionSortTest {

    @Test
    @DisplayName("Teste basico de ordenacao")
    void testOrdenacaoBasica() {
        int[] array = { 5, 2, 8, 1, 9 };
        SortingMetrics metrics = new SortingMetrics();

        InsertionSort.sort(array, 0, array.length - 1, metrics);

        int[] expected = { 1, 2, 5, 8, 9 };
        assertArrayEquals(expected, array, "Array deve estar ordenado");
    }

    @Test
    @DisplayName("Teste com subarray")
    void testComSubarray() {
        int[] array = { 10, 5, 2, 8, 1, 9, 20 };
        SortingMetrics metrics = new SortingMetrics();

        // Ordena apenas do indice 1 ao 5
        InsertionSort.sort(array, 1, 5, metrics);

        // O primeiro e ultimo elemento nao devem ser afetados
        assertEquals(10, array[0], "Primeiro elemento nao deve mudar");
        assertEquals(20, array[6], "Ultimo elemento nao deve mudar");

        // Elementos do meio devem estar ordenados
        for (int i = 1; i < 5; i++) {
            assertTrue(array[i] <= array[i + 1], "Subarray deve estar ordenado");
        }
    }

    @Test
    @DisplayName("Teste com array ja ordenado")
    void testArrayJaOrdenado() {
        int[] array = { 1, 2, 3, 4, 5 };
        SortingMetrics metrics = new SortingMetrics();

        InsertionSort.sort(array, 0, array.length - 1, metrics);

        int[] expected = { 1, 2, 3, 4, 5 };
        assertArrayEquals(expected, array, "Array deve permanecer ordenado");
    }

    @Test
    @DisplayName("Teste com array de um elemento")
    void testArrayUmElemento() {
        int[] array = { 42 };
        SortingMetrics metrics = new SortingMetrics();

        InsertionSort.sort(array, 0, 0, metrics);

        assertEquals(42, array[0], "Elemento unico deve permanecer igual");
    }

    @Test
    @DisplayName("Teste com array de dois elementos")
    void testArrayDoisElementos() {
        int[] array = { 2, 1 };
        SortingMetrics metrics = new SortingMetrics();

        InsertionSort.sort(array, 0, 1, metrics);

        int[] expected = { 1, 2 };
        assertArrayEquals(expected, array, "Array de 2 elementos deve estar ordenado");
    }

    @Test
    @DisplayName("Teste com elementos negativos")
    void testElementosNegativos() {
        int[] array = { -5, 3, -1, 8, -10 };
        SortingMetrics metrics = new SortingMetrics();

        InsertionSort.sort(array, 0, array.length - 1, metrics);

        int[] expected = { -10, -5, -1, 3, 8 };
        assertArrayEquals(expected, array, "Array com negativos deve estar ordenado");
    }

    @Test
    @DisplayName("Teste com duplicatas")
    void testComDuplicatas() {
        int[] array = { 5, 2, 5, 1, 2 };
        SortingMetrics metrics = new SortingMetrics();

        InsertionSort.sort(array, 0, array.length - 1, metrics);

        int[] expected = { 1, 2, 2, 5, 5 };
        assertArrayEquals(expected, array, "Array com duplicatas deve estar ordenado");
    }
}

