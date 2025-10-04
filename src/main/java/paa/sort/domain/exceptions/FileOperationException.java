package paa.sort.domain.exceptions;

import java.nio.file.Path;

/**
 * Exceção para erros de operações de arquivo
 */
public class FileOperationException extends SortingException {
    private final Path filePath;
    private final String operation;

    public FileOperationException(String message, String operation, Path filePath, Throwable cause) {
        super(message, "N/A", "N/A", 0, cause);
        this.operation = operation;
        this.filePath = filePath;
    }

    public FileOperationException(String message, String operation, Path filePath) {
        super(message, "N/A", "N/A", 0);
        this.operation = operation;
        this.filePath = filePath;
    }

    public Path getFilePath() {
        return filePath;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return String.format("FileOperationException{message='%s', operation='%s', path='%s'}",
                getMessage(), operation, filePath != null ? filePath.toString() : "null");
    }
}
