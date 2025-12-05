package paa.knapsack.domain.performance;

import paa.knapsack.domain.KnapsackInstance;
import paa.knapsack.domain.KnapsackResult;
import paa.knapsack.domain.algorithms.KnapsackDPLinear;
import paa.knapsack.domain.algorithms.KnapsackPD;
import java.util.ArrayList;
import java.util.List;

/**
 * Executor de testes de performance comparativos.
 */
public class ExperimentRunner {

    public static class ExperimentResult {
        public final int numItems;
        public final int capacidade;
        public final long seed;
        public final List<KnapsackResult> resultados;
        public final KnapsackResult resultadoOtimo;

        public ExperimentResult(int numItems, int capacidade, long seed,
                List<KnapsackResult> resultados, KnapsackResult resultadoOtimo) {
            this.numItems = numItems;
            this.capacidade = capacidade;
            this.seed = seed;
            this.resultados = resultados;
            this.resultadoOtimo = resultadoOtimo;
        }

        public double calcularRazaoAproximacao(KnapsackResult resultado) {
            if (resultadoOtimo == null || resultadoOtimo.getValorTotal() == 0) {
                return -1.0;
            }
            return (double) resultado.getValorTotal() / resultadoOtimo.getValorTotal();
        }
    }

    public static ExperimentResult executarTodosAlgoritmos(KnapsackInstance instance) {

        int numItems = instance.getNumItems();
        int capacidade = instance.getCapacidade();
        long seed = 0;

        List<KnapsackResult> resultados = new ArrayList<>();
        KnapsackResult resultadoOtimo = null;

        // DP Linear Space
        try {
            System.out.printf("[ExperimentRunner] Executando DP-LinearSpace...%n");
            KnapsackResult resDpLinear = KnapsackDPLinear.solveDPLinearSpace(instance);
            resultados.add(resDpLinear);
            resultadoOtimo = resDpLinear;
            System.out.printf("[ExperimentRunner] DP-LinearSpace: %s%n", resDpLinear);
        } catch (Exception e) {
            System.err.printf("[ExperimentRunner] Erro DP-LinearSpace: %s%n", e.getMessage());
        }

        // DP Matriz Completa
        try {
            System.out.printf("[ExperimentRunner] Executando DP-MatrizCompleta...%n");
            KnapsackResult resDpMatriz = KnapsackPD.solve(instance);
            resultados.add(resDpMatriz);
            if (resultadoOtimo == null) resultadoOtimo = resDpMatriz;
            System.out.printf("[ExperimentRunner] DP-MatrizCompleta: %s%n", resDpMatriz);
        } catch (Exception e) {
            System.err.printf("[ExperimentRunner] Erro DP-MatrizCompleta: %s%n", e.getMessage());
        }

        return new ExperimentResult(numItems, capacidade, seed, resultados, resultadoOtimo);
    }

    public static List<ExperimentResult> executarBateria(int[] tamanhos) {
        List<ExperimentResult> resultados = new ArrayList<>();

        for (int n : tamanhos) {
            long seed = 42 + n;
            try {
                System.out.printf("%n=== Testando tamanho n=%d (seed=%d) ===%n", n, seed);
                KnapsackInstance instance =
                    paa.knapsack.domain.testdata.KnapsackInstanceGenerator.generateBenchmark(n, seed);

                ExperimentResult res = executarTodosAlgoritmos(instance);
                resultados.add(res);

                System.out.printf("[ExperimentRunner] Experimento(n=%d) concluído%n", n);
            } catch (Exception e) {
                System.err.printf("[ExperimentRunner] Erro no experimento(n=%d): %s%n", n, e.getMessage());
            }
        }

        return resultados;
    }
}

