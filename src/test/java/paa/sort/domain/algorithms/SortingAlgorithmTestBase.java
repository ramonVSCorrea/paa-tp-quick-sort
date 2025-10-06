package paa.sort.domain.algorithms;

import org.junit.jupiter.api.Test;
import paa.sort.domain.SortingAlgorithm;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe base abstrata para testes de algoritmos de ordenacao.
 * Contem todos os casos de teste comuns que cada implementacao deve passar.
 */
abstract class SortingAlgorithmTestBase {

    /**
     * Metodo abstrato que cada classe de teste concreta deve implementar
     * para fornecer a instancia do algoritmo a ser testado
     */
    protected abstract SortingAlgorithm createAlgorithm();

    /**
     * Verifica se um array esta ordenado
     */
    private boolean isArraySorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se dois arrays contem os mesmos elementos (mesmo que em ordem
     * diferente)
     */
    private boolean haveSameElements(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        int[] sorted1 = Arrays.copyOf(array1, array1.length);
        int[] sorted2 = Arrays.copyOf(array2, array2.length);
        Arrays.sort(sorted1);
        Arrays.sort(sorted2);
        return Arrays.equals(sorted1, sorted2);
    }

    @Test
    void testArrayVazio() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = {};
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertEquals(0, result.length, "Array vazio deve permanecer vazio");
    }

    @Test
    void testArrayComUmElemento() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { 42 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertEquals(1, result.length, "Array deve ter 1 elemento");
        assertEquals(42, result[0], "O elemento deve permanecer o mesmo");
    }

    @Test
    void testArrayJaOrdenado() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        int[] original = array.clone();
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertTrue(isArraySorted(result), "Array deve estar ordenado");
        assertTrue(haveSameElements(original, result), "Array deve conter os mesmos elementos");
        assertArrayEquals(original, result, "Array ja ordenado deve permanecer igual");
    }

    @Test
    void testArrayOrdenadoInversamente() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        int[] original = array.clone();
        int[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertTrue(isArraySorted(result), "Array deve estar ordenado");
        assertTrue(haveSameElements(original, result), "Array deve conter os mesmos elementos");
        assertArrayEquals(expected, result, "Array deve estar ordenado corretamente");
    }

    @Test
    void testArrayComTodosElementosIguais() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { 5, 5, 5, 5, 5, 5, 5, 5 };
        int[] original = array.clone();
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertTrue(isArraySorted(result), "Array deve estar ordenado");
        assertTrue(haveSameElements(original, result), "Array deve conter os mesmos elementos");
        assertEquals(8, result.length, "Array deve ter 8 elementos");
        for (int value : result) {
            assertEquals(5, value, "Todos os elementos devem ser 5");
        }
    }

    @Test
    void testArrayComElementosNegativos() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { -5, 3, -1, 8, -10, 0, 2, -3 };
        int[] original = array.clone();
        int[] expected = { -10, -5, -3, -1, 0, 2, 3, 8 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertTrue(isArraySorted(result), "Array deve estar ordenado");
        assertTrue(haveSameElements(original, result), "Array deve conter os mesmos elementos");
        assertArrayEquals(expected, result, "Array com negativos deve estar ordenado corretamente");
    }

    @Test
    void testArrayComMisturaDePositivosNegativos() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { -100, 50, -25, 75, 0, -50, 25, 100, -75 };
        int[] original = array.clone();
        int[] expected = { -100, -75, -50, -25, 0, 25, 50, 75, 100 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertTrue(isArraySorted(result), "Array deve estar ordenado");
        assertTrue(haveSameElements(original, result), "Array deve conter os mesmos elementos");
        assertArrayEquals(expected, result, "Array com positivos e negativos deve estar ordenado corretamente");
    }

    @Test
    void testArrayAleatorio() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { 64, 34, 25, 12, 22, 11, 90, 88, 45, 50 };
        int[] original = array.clone();
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertTrue(isArraySorted(result), "Array deve estar ordenado");
        assertTrue(haveSameElements(original, result), "Array deve conter os mesmos elementos");
        assertEquals(original.length, result.length, "Array deve ter o mesmo tamanho");
    }

    @Test
    void testArrayAleatorioGrande() {
        SortingAlgorithm algorithm = createAlgorithm();
        Random random = new Random(42); // Seed fixo para reproducibilidade
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(10000) - 5000; // Valores entre -5000 e 5000
        }

        int[] original = array.clone();
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertTrue(isArraySorted(result), "Array grande deve estar ordenado");
        assertTrue(haveSameElements(original, result), "Array deve conter os mesmos elementos");
        assertEquals(original.length, result.length, "Array deve ter o mesmo tamanho");
    }

    @Test
    void testArrayComDoisElementos() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { 2, 1 };
        int[] expected = { 1, 2 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertTrue(isArraySorted(result), "Array deve estar ordenado");
        assertArrayEquals(expected, result, "Array de 2 elementos deve estar ordenado");
    }

    @Test
    void testArrayComDoisElementosJaOrdenados() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { 1, 2 };
        int[] expected = { 1, 2 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertTrue(isArraySorted(result), "Array deve estar ordenado");
        assertArrayEquals(expected, result, "Array de 2 elementos ja ordenados deve permanecer igual");
    }

    @Test
    void testArrayComDuplicatas() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { 5, 2, 8, 2, 9, 1, 5, 5 };
        int[] original = array.clone();
        int[] expected = { 1, 2, 2, 5, 5, 5, 8, 9 };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertTrue(isArraySorted(result), "Array deve estar ordenado");
        assertTrue(haveSameElements(original, result), "Array deve conter os mesmos elementos");
        assertArrayEquals(expected, result, "Array com duplicatas deve estar ordenado corretamente");
    }

    @Test
    void testArrayNaoModificaOriginal() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { 5, 2, 8, 1, 9 };
        int[] backup = array.clone();
        algorithm.sort(array);

        assertArrayEquals(backup, array, "Array original nao deve ser modificado");
    }

    @Test
    void testArrayComValoresExtremos() {
        SortingAlgorithm algorithm = createAlgorithm();
        int[] array = { Integer.MAX_VALUE, Integer.MIN_VALUE, 0, -1, 1 };
        int[] original = array.clone();
        int[] expected = { Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE };
        int[] result = algorithm.sort(array);

        assertNotNull(result, "O resultado nao deve ser null");
        assertTrue(isArraySorted(result), "Array deve estar ordenado");
        assertTrue(haveSameElements(original, result), "Array deve conter os mesmos elementos");
        assertArrayEquals(expected, result, "Array com valores extremos deve estar ordenado corretamente");
    }
}

