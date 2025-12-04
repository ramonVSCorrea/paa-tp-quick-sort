package paa.sort.domain.testdata;

import paa.sort.domain.exceptions.ValidationException;
import paa.sort.infrastructure.logging.ExceptionLogger;

import java.util.Random;

/**
 * Gerador de dados de teste para os algoritmos de ordenacao
 */
public class TestDataGenerator {
    private final Random random;
    private final ExceptionLogger exceptionLogger;

    public TestDataGenerator() {
        this.random = new Random();
        this.exceptionLogger = ExceptionLogger.getInstance();
    }

    public TestDataGenerator(long seed) {
        this.random = new Random(seed);
        this.exceptionLogger = ExceptionLogger.getInstance();
        exceptionLogger.logInfo("TestDataGenerator inicializado com seed: " + seed, "Inicialização");
    }

    /**
     * Gera um array de acordo com o tipo especificado
     */
    public int[] generateData(DataType type, int size) throws ValidationException {
        try {
            validateGenerationParameters(type, size);
        } catch (ValidationException e) {
            exceptionLogger.logValidationError(e, "generateData");
            throw e;
        }

        exceptionLogger.logInfo("Gerando dados: tipo=" + type.getDescription() + ", tamanho=" + size,
                "Geração de dados");

        try {
            int[] result = switch (type) {
                case RANDOM -> generateRandomArray(size);
                case SORTED -> generateSortedArray(size);
                case REVERSE_SORTED -> generateReverseSortedArray(size);
                case MANY_DUPLICATES -> generateManyDuplicatesArray(size);
                case WORST_CASE -> generateWorstCaseArray(size);
            };

            exceptionLogger.logInfo("Dados gerados com sucesso: " + result.length + " elementos", "Geração de dados");
            return result;

        } catch (Exception e) {
            ValidationException validationException = new ValidationException(
                    "Falha ao gerar dados de teste: " + e.getMessage(),
                    "generateData",
                    "tipo=" + type + ", tamanho=" + size,
                    e);
            exceptionLogger.logValidationError(validationException, "Geração de dados falhou");
            throw validationException;
        }
    }

    /**
     * Valida parâmetros para geração de dados
     */
    private void validateGenerationParameters(DataType type, int size) throws ValidationException {
        if (type == null) {
            throw new ValidationException("Tipo de dados não pode ser null", "type", null);
        }

        if (size <= 0) {
            throw new ValidationException("Tamanho deve ser positivo", "size", size);
        }

        if (size > 1_000_000) {
            exceptionLogger.logWarning("Tamanho muito grande pode causar problemas de memória: " + size,
                    "Validação de tamanho");
        }
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
