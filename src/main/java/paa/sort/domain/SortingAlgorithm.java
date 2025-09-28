package paa.sort.domain;

/**
 * Interface para algoritmos de ordenacao
 */
public interface SortingAlgorithm {
    /**
     * Ordena um array de inteiros
     * @param array Array a ser ordenado
     * @return Array ordenado
     */
    int[] sort(int[] array);

    /**
     * Retorna o nome do algoritmo
     * @return Nome do algoritmo
     */
    String getName();
}
