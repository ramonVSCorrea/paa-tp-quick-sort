package paa.sort.domain.validation;

/**
 * Classe utilitaria para validacao de arrays
 */
public final class ArrayValidator {

    private ArrayValidator() {
        // Previne instanciacao
        throw new UnsupportedOperationException("Classe utilitaria nao deve ser instanciada");
    }

    /**
     * Verifica se um array esta ordenado em ordem crescente
     *
     * @param array Array a ser verificado
     * @return true se o array esta ordenado, false caso contrario
     */
    public static boolean isSorted(int[] array) {
        if (array == null || array.length <= 1) {
            return true;
        }

        for (int currentIndex = 1; currentIndex < array.length; currentIndex++) {
            if (array[currentIndex] < array[currentIndex - 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se um array esta ordenado e retorna um resultado detalhado
     *
     * @param array Array a ser verificado
     * @return ValidationResult com status e mensagem
     */
    public static ValidationResult validate(int[] array) {
        if (array == null) {
            return ValidationResult.failure("Array e nulo");
        }

        if (array.length == 0) {
            return ValidationResult.success();
        }

        for (int currentIndex = 1; currentIndex < array.length; currentIndex++) {
            if (array[currentIndex] < array[currentIndex - 1]) {
                return ValidationResult.failure(
                    String.format("Array nao esta ordenado na posicao %d: %d > %d",
                        currentIndex, array[currentIndex - 1], array[currentIndex])
                );
            }
        }

        return ValidationResult.success();
    }
}
