package paa.sort.domain.testdata;

import java.util.Random;

/**
 * Gerador de dados de teste para os algoritmos de ordenacao
 */
public class TestDataGenerator {
    private final Random random;

    public TestDataGenerator() {
        this.random = new Random();
    }

    public TestDataGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Gera um array de acordo com o tipo especificado
     */
    public int[] generateData(DataType type, int size) {
        return switch (type) {
            case RANDOM -> generateRandomArray(size);
            case SORTED -> generateSortedArray(size);
            case REVERSE_SORTED -> generateReverseSortedArray(size);
            case MANY_DUPLICATES -> generateManyDuplicatesArray(size);
            case WORST_CASE -> generateWorstCaseArray(size);
        };
    }

    /**
     * Gera array aleatorio
     */
    private int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size * 10); // Valores entre 0 e size*10
        }
        return array;
    }

    /**
     * Gera array ja ordenado
     */
    private int[] generateSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    /**
     * Gera array ordenado de forma inversa
     */
    private int[] generateReverseSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i - 1;
        }
        return array;
    }

    /**
     * Gera array com muitos elementos repetidos
     */
    private int[] generateManyDuplicatesArray(int size) {
        int[] array = new int[size];
        int numUniqueValues = Math.max(1, size / 10); // 10% de valores unicos

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(numUniqueValues);
        }
        return array;
    }

    /**
     * Gera array que forca o pior caso do Quicksort
     * (array ja ordenado com pivo sempre sendo o maior elemento)
     */
    private int[] generateWorstCaseArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }
}
