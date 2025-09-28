package paa.sort.domain.algorithms;

/**
 * Implementacao do Insertion Sort para uso no Quicksort hibrido
 */
public class InsertionSort {

    /**
     * Ordena um subarray usando Insertion Sort
     * @param array Array a ser ordenado
     * @param low Indice inicial
     * @param high Indice final
     */
    public static void sort(int[] array, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= low && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
}
