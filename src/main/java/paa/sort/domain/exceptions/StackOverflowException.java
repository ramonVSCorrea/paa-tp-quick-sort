package paa.sort.domain.exceptions;

/**
 * Exceção específica para estouro de pilha em algoritmos recursivos
 */
public class StackOverflowException extends SortingException {
    private final int recursionDepth;
    private final int threshold;

    public StackOverflowException(String algorithmName, String dataType, int arraySize, int recursionDepth,
            int threshold) {
        super(String.format(
                "Estouro de pilha detectado no algoritmo %s com dados %s (tamanho: %d). Profundidade: %d, Threshold: %d",
                algorithmName, dataType, arraySize, recursionDepth, threshold),
                algorithmName, dataType, arraySize);
        this.recursionDepth = recursionDepth;
        this.threshold = threshold;
    }

    public int getRecursionDepth() {
        return recursionDepth;
    }

    public int getThreshold() {
        return threshold;
    }

    @Override
    public String toString() {
        return String.format(
                "StackOverflowException{message='%s', algorithm='%s', dataType='%s', size=%d, depth=%d, threshold=%d}",
                getMessage(), getAlgorithmName(), getDataType(), getArraySize(), recursionDepth, threshold);
    }
}
