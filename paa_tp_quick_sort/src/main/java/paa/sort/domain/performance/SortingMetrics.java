package paa.sort.domain.performance;

/**
 * Classe para armazenar metricas de operacoes durante a ordenacao
 */
public class SortingMetrics {
    private long comparisons;
    private long swaps;

    public SortingMetrics() {
        this.comparisons = 0;
        this.swaps = 0;
    }

    public void incrementComparisons() {
        this.comparisons++;
    }

    public void incrementSwaps() {
        this.swaps++;
    }

    public void addComparisons(long count) {
        this.comparisons += count;
    }

    public void addSwaps(long count) {
        this.swaps += count;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    public void reset() {
        this.comparisons = 0;
        this.swaps = 0;
    }

    public void add(SortingMetrics other) {
        this.comparisons += other.comparisons;
        this.swaps += other.swaps;
    }

    @Override
    public String toString() {
        return String.format("Comparacoes: %d, Trocas: %d", comparisons, swaps);
    }
}
