package paa.sort.domain.algorithms;

import paa.sort.domain.performance.SortingMetrics;

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
        sort(array, low, high, new SortingMetrics());
    }

    /**
     * Ordena um subarray usando Insertion Sort com metricas
     * @param array Array a ser ordenado
     * @param low Indice inicial
     * @param high Indice final
     * @param metrics Objeto para coletar metricas
     */
    public static void sort(int[] array, int low, int high, SortingMetrics metrics) {
        for (int i = low + 1; i <= high; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= low) {
                metrics.incrementComparisons(); // Comparacao: array[j] > key
                if (array[j] > key) {
                    array[j + 1] = array[j];
                    metrics.incrementSwaps(); // Movimento/troca
                    j--;
                } else {
                    break;
                }
            }

            if (j + 1 != i) { // So conta como troca se houve movimento
                array[j + 1] = key;
                metrics.incrementSwaps(); // Insercao do key na posicao correta
            }
        }
    }
}
