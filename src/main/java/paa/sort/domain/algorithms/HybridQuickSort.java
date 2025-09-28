package paa.sort.domain.algorithms;

import paa.sort.domain.SortingAlgorithm;
import paa.sort.domain.performance.SortingMetrics;

/**
 * Implementacao do Quicksort hibrido que usa Insertion Sort para subarrays pequenos
 */
public class HybridQuickSort implements SortingAlgorithm {
    private final int threshold;

    public HybridQuickSort(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public int[] sort(int[] array) {
        return sort(array, new SortingMetrics());
    }

    @Override
    public int[] sort(int[] array, SortingMetrics metrics) {
        if (array == null || array.length <= 1) {
            return array;
        }
        int[] sortedArray = array.clone();
        quickSort(sortedArray, 0, sortedArray.length - 1, metrics);
        return sortedArray;
    }

    private void quickSort(int[] array, int low, int high, SortingMetrics metrics) {
        metrics.incrementComparisons(); // Comparacao: low < high
        if (low < high) {
            // Se o subarray e pequeno, usa Insertion Sort
            metrics.incrementComparisons(); // Comparacao: high - low + 1 <= threshold
            if (high - low + 1 <= threshold) {
                InsertionSort.sort(array, low, high, metrics);
            } else {
                int pivotIndex = partition(array, low, high, metrics);
                quickSort(array, low, pivotIndex - 1, metrics);
                quickSort(array, pivotIndex + 1, high, metrics);
            }
        }
    }

    private int partition(int[] array, int low, int high, SortingMetrics metrics) {
        int pivot = array[high]; // Ultimo elemento como pivo
        int i = low - 1;

        for (int j = low; j < high; j++) {
            metrics.incrementComparisons(); // Comparacao: array[j] <= pivot
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j, metrics);
            }
        }
        swap(array, i + 1, high, metrics);
        return i + 1;
    }

    private void swap(int[] array, int i, int j, SortingMetrics metrics) {
        if (i != j) { // So conta como troca se as posicoes forem diferentes
            metrics.incrementSwaps();
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    @Override
    public String getName() {
        return "Quicksort Hibrido (M=" + threshold + ")";
    }
}
