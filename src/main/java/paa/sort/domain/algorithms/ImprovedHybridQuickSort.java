package paa.sort.domain.algorithms;

import paa.sort.domain.SortingAlgorithm;

/**
 * Implementacao do Quicksort hibrido melhorado com mediana-de-tres para escolha do pivo
 */
public class ImprovedHybridQuickSort implements SortingAlgorithm {
    private final int threshold;

    public ImprovedHybridQuickSort(int threshold) {
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
                // Usa mediana-de-tres para escolher o pivo
                int pivotIndex = medianOfThree(array, low, high);
                swap(array, pivotIndex, high); // Move o pivo para o final

                int partitionIndex = partition(array, low, high);
                quickSort(array, low, partitionIndex - 1);
                quickSort(array, partitionIndex + 1, high);
            }
        }
    }

    /**
     * Encontra a mediana de tres elementos (primeiro, meio e ultimo)
     * e retorna o indice do elemento mediano
     */
    private int medianOfThree(int[] array, int low, int high) {
        int mid = low + (high - low) / 2;

        if (array[low] > array[mid]) {
            swap(array, low, mid);
        }
        if (array[mid] > array[high]) {
            swap(array, mid, high);
        }
        if (array[low] > array[mid]) {
            swap(array, low, mid);
        }

        return mid; // O elemento do meio e a mediana
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
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
        return "Quicksort Hibrido Melhorado (M=" + threshold + ", Mediana-de-3)";
    }
}
