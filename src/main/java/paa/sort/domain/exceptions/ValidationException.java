package paa.sort.domain.exceptions;

/**
 * Exceção para erros de validação de entrada
 */
public class ValidationException extends SortingException {
    private final String validationField;
    private final Object invalidValue;

    public ValidationException(String message, String validationField, Object invalidValue) {
        super(message, "N/A", "N/A", 0);
        this.validationField = validationField;
        this.invalidValue = invalidValue;
    }

    public ValidationException(String message, String validationField, Object invalidValue, Throwable cause) {
        super(message, "N/A", "N/A", 0, cause);
        this.validationField = validationField;
        this.invalidValue = invalidValue;
    }

    public String getValidationField() {
        return validationField;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    @Override
    public String toString() {
        return String.format("ValidationException{message='%s', field='%s', value=%s}",
                getMessage(), validationField, invalidValue);
    }
}
