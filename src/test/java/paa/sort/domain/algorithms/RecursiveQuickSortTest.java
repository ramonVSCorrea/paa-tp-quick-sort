package paa.sort.domain.algorithms;

import org.junit.jupiter.api.DisplayName;
import paa.sort.domain.SortingAlgorithm;

/**
 * Testes para o algoritmo RecursiveQuickSort
 */
@DisplayName("Testes do Quicksort Recursivo")
class RecursiveQuickSortTest extends SortingAlgorithmTestBase {

    @Override
    protected SortingAlgorithm createAlgorithm() {
        return new RecursiveQuickSort();
    }
}

