package paa.knapsack.application;

import paa.knapsack.domain.KnapsackInstance;
import paa.knapsack.domain.KnapsackResult; // Explicitly imported
import paa.knapsack.domain.performance.ExperimentRunner;
import paa.knapsack.domain.performance.ExperimentRunner.ExperimentResult;
import paa.knapsack.domain.testdata.KnapsackInstanceGenerator;
import paa.knapsack.infrastructure.export.CSVExporter;

import java.util.List;

/**
 * Orquestrador principal do estudo do Problema da Mochila.
 */
public class KnapsackStudyStarter {

    private static final String OUTPUT_DIR = "results";

    public void executeCompleteStudy() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("  ESTUDO COMPLETO DO PROBLEMA DA MOCHILA (0/1)");
        System.out.println("  Algoritmos: DP Linear-Space, DP Matriz-Completa");
        System.out.println("=".repeat(80) + "\n");

        try {
            // Tamanhos para testar os limites dos algoritmos de Programação Dinâmica
            // DP Linear-Space: O(n*L) tempo, O(L) espaço
            // DP Matriz-Completa: O(n*L) tempo, O(n*L) espaço
            // Como L é proporcional a n, a complexidade efetiva é O(n^2)
            int[] tamanhos = { 10, 30, 100, 300, 500, 1000, 2000, 5000, 10000 };

            System.out.printf("[KnapsackStudyStarter] Executando bateria de testes com %d tamanhos distintos...%n%n",
                tamanhos.length);

            List<ExperimentRunner.ExperimentResult> resultados =
                ExperimentRunner.executarBateria(tamanhos);

            System.out.println("\n" + "=".repeat(80));
            System.out.println("  RESUMO DOS RESULTADOS");
            System.out.println("=".repeat(80));
            exibirResumo(resultados);

            System.out.println("\n" + "=".repeat(80));
            System.out.println("  EXPORTANDO RESULTADOS");
            System.out.println("=".repeat(80) + "\n");

            String arquivoDetalhado = CSVExporter.exportarExperimentos(resultados, OUTPUT_DIR);
            String arquivoSumario = CSVExporter.exportarSumario(resultados, OUTPUT_DIR);

            System.out.printf("%n[KnapsackStudyStarter] Análise completada!%n");
            System.out.printf("[KnapsackStudyStarter] Resultados em: %s%n", OUTPUT_DIR);
            System.out.printf("[KnapsackStudyStarter]  - Detalhado: %s%n", arquivoDetalhado);
            System.out.printf("[KnapsackStudyStarter]  - Sumário: %s%n", arquivoSumario);

        } catch (Exception e) {
            System.err.printf("[KnapsackStudyStarter] Erro ao executar estudo: %s%n", e.getMessage());
            e.printStackTrace();
        }
    }

    private void exibirResumo(List<ExperimentResult> resultados) {
        for (ExperimentResult exp : resultados) {
            System.out.printf("\n📊 Tamanho n=%d, Capacidade L=%d%n", exp.numItems, exp.capacidade);
            System.out.println("─".repeat(70));

            // Print the optimal result first if available
            if (exp.resultadoOtimo != null) {
                System.out.printf("  ✓ Ótimo (%s)%n", exp.resultadoOtimo); // Using toString for brevity
            }

            // Print all other results
            for (KnapsackResult res : exp.resultados) {
                // If it's the optimal result and already printed, skip it here
                if (exp.resultadoOtimo != null && res == exp.resultadoOtimo) {
                    continue;
                }

                double razao = exp.calcularRazaoAproximacao(res);
                String razaoStr = (razao > 0)
                    ? String.format("%.1f%%", razao * 100)
                    : "N/A";

                System.out.printf("  → %-25s: valor=%4d, peso=%4d, tempo=%3dms, aprox=%s%n",
                    res.getNomeAlgoritmo(),
                    res.getValorTotal(),
                    res.getPesoTotal(),
                    res.getTempoExecutacaoMs(),
                    razaoStr);
            }
        }
    }

    public void executeSimpleTest() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  TESTE SIMPLES - Validação");
        System.out.println("=".repeat(60) + "\n");

        try {
            KnapsackInstance instance = KnapsackInstanceGenerator.generateSimple(10, 50, 42);

            System.out.println("Instância gerada: " + instance);
            System.out.println("Itens:");
            for (var item : instance.getItems()) {
                System.out.println("  " + item);
            }

            var resultado = ExperimentRunner.executarTodosAlgoritmos(instance);

            System.out.println("\n" + resultado);

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
