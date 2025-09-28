package paa.sort.domain.algorithms;

import paa.sort.domain.SortingAlgorithm;

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
        if (array == null || array.length <= 1) {
            return array;
        }
        int[] sortedArray = array.clone();
        quickSort(sortedArray, 0, sortedArray.length - 1);
        return sortedArray;
    }

    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            // Se o subarray e pequeno, usa Insertion Sort
            if (high - low + 1 <= threshold) {
                InsertionSort.sort(array, low, high);
            } else {
                int pivotIndex = partition(array, low, high);
                quickSort(array, low, pivotIndex - 1);
                quickSort(array, pivotIndex + 1, high);
            }
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high]; // Ultimo elemento como pivo
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public String getName() {
        return "Quicksort Hibrido (M=" + threshold + ")";
    }
}
