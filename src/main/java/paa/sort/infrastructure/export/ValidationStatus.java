package paa.sort.infrastructure.export;

/**
 * Enum que representa o status de validacao de um array ordenado
 */
public enum ValidationStatus {
    SUCCESS("ORDENADO CORRETAMENTE"),
    FAILURE("ERRO NA ORDENACAO");

    private final String message;

    ValidationStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Retorna o status baseado no resultado da validacao
     */
    public static ValidationStatus fromBoolean(boolean isValid) {
        return isValid ? SUCCESS : FAILURE;
    }
}
