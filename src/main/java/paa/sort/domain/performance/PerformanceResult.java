package paa.sort.domain.performance;

/**
 * Resultado de um teste de performance
 */
public class PerformanceResult {
    private final String algorithmName;
    private final String dataType;
    private final int arraySize;
    private final long executionTimeNanos;
    private final boolean successful;

    public PerformanceResult(String algorithmName, String dataType, int arraySize,
                           long executionTimeNanos, boolean successful) {
        this.algorithmName = algorithmName;
        this.dataType = dataType;
        this.arraySize = arraySize;
        this.executionTimeNanos = executionTimeNanos;
        this.successful = successful;
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

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public double getExecutionTimeMillis() {
        return executionTimeNanos / 1_000_000.0;
    }

    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | Tamanho: %d | Tempo: %.2f ms | Sucesso: %s",
                algorithmName, dataType, arraySize, getExecutionTimeMillis(), successful);
    }
}
