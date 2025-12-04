package paa.knapsack.domain.performance;

import paa.knapsack.domain.KnapsackInstance;
import paa.knapsack.domain.KnapsackResult;
import paa.knapsack.domain.algorithms.KnapsackSolver;
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

    public static ExperimentResult executarTodosAlgoritmos(
            KnapsackInstance instance, boolean executarBF, double epsilonFPTAS) {

        int numItems = instance.getNumItems();
        int capacidade = instance.getCapacidade();
        long seed = 0;

        List<KnapsackResult> resultados = new ArrayList<>();
        KnapsackResult resultadoOtimo = null;

        // BruteForce
        if (executarBF && numItems <= 22) {
            try {
                System.out.printf("[ExperimentRunner] Executando BruteForce (n=%d)...%n", numItems);
                resultadoOtimo = KnapsackSolver.solveBruteForce(instance);
                resultados.add(resultadoOtimo);
                System.out.printf("[ExperimentRunner] BruteForce: %s%n", resultadoOtimo);
            } catch (Exception e) {
                System.err.printf("[ExperimentRunner] Erro BruteForce: %s%n", e.getMessage());
            }
        }

        // DP
        try {
            System.out.printf("[ExperimentRunner] Executando DP-LinearSpace...%n");
            KnapsackResult resDp = KnapsackSolver.solveDPLinearSpace(instance);
            resultados.add(resDp);
            if (resultadoOtimo == null) resultadoOtimo = resDp;
            System.out.printf("[ExperimentRunner] DP: %s%n", resDp);
        } catch (Exception e) {
            System.err.printf("[ExperimentRunner] Erro DP: %s%n", e.getMessage());
        }

        // Greedy
        try {
            System.out.printf("[ExperimentRunner] Executando Greedy-Ratio...%n");
            KnapsackResult resGreedy = KnapsackSolver.solveGreedyByRatio(instance);
            resultados.add(resGreedy);
            System.out.printf("[ExperimentRunner] Greedy: %s%n", resGreedy);
        } catch (Exception e) {
            System.err.printf("[ExperimentRunner] Erro Greedy: %s%n", e.getMessage());
        }

        // FPTAS
        for (double eps : new double[]{ 0.5, 0.1, 0.05 }) {
            try {
                System.out.printf("[ExperimentRunner] Executando FPTAS(ε=%.2f)...%n", eps);
                KnapsackResult resFptas = KnapsackSolver.solveFPTAS(instance, eps);
                resultados.add(resFptas);
                System.out.printf("[ExperimentRunner] FPTAS: %s%n", resFptas);
            } catch (Exception e) {
                System.err.printf("[ExperimentRunner] Erro FPTAS: %s%n", e.getMessage());
            }
        }

        return new ExperimentResult(numItems, capacidade, seed, resultados, resultadoOtimo);
    }

    public static List<ExperimentResult> executarBateria(int[] tamanhos, boolean executarBF) {
        List<ExperimentResult> resultados = new ArrayList<>();

        for (int n : tamanhos) {
            long seed = 42 + n;
            try {
                System.out.printf("%n=== Testando tamanho n=%d (seed=%d) ===%n", n, seed);
                KnapsackInstance instance =
                    paa.knapsack.domain.testdata.KnapsackInstanceGenerator.generateBenchmark(n, seed);

                ExperimentResult res = executarTodosAlgoritmos(instance, executarBF, 0.1);
                resultados.add(res);

                System.out.printf("[ExperimentRunner] Experimento(n=%d) concluído%n", n);
            } catch (Exception e) {
                System.err.printf("[ExperimentRunner] Erro no experimento(n=%d): %s%n", n, e.getMessage());
            }
        }

        return resultados;
    }
}

