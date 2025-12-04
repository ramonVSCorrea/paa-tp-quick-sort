package paa.sort.domain.validation;

/**
 * Representa o resultado de uma validacao de array
 */
public class ValidationResult {
    private final boolean isValid;
    private final String message;

    private ValidationResult(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    /**
     * Cria um resultado de sucesso
     */
    public static ValidationResult success() {
        return new ValidationResult(true, "Array ordenado corretamente");
    }

    /**
     * Cria um resultado de falha com mensagem
     */
    public static ValidationResult failure(String reason) {
        return new ValidationResult(false, reason);
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("ValidationResult{isValid=%s, message='%s'}", isValid, message);
    }
}
