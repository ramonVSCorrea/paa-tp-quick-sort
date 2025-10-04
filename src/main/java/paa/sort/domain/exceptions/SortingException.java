package paa.sort.domain.exceptions;

/**
 * Exceção base para erros relacionados aos algoritmos de ordenação
 */
public class SortingException extends Exception {
    private final String algorithmName;
    private final String dataType;
    private final int arraySize;
    private final long executionTime;

    public SortingException(String message, String algorithmName, String dataType, int arraySize) {
        this(message, algorithmName, dataType, arraySize, 0, null);
    }

    public SortingException(String message, String algorithmName, String dataType, int arraySize, Throwable cause) {
        this(message, algorithmName, dataType, arraySize, 0, cause);
    }

    public SortingException(String message, String algorithmName, String dataType, int arraySize, long executionTime,
            Throwable cause) {
        super(message, cause);
        this.algorithmName = algorithmName;
        this.dataType = dataType;
        this.arraySize = arraySize;
        this.executionTime = executionTime;
    }

    public SortingException(String message, String algorithmName, String dataType, int arraySize, long executionTime) {
        super(message);
        this.algorithmName = algorithmName;
        this.dataType = dataType;
        this.arraySize = arraySize;
        this.executionTime = executionTime;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getDataType() {
        return dataType;
    }

    public int getArraySize() {
        return arraySize;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    @Override
    public String toString() {
        return String.format("SortingException{message='%s', algorithm='%s', dataType='%s', size=%d, time=%dns}",
                getMessage(), algorithmName, dataType, arraySize, executionTime);
    }
}
