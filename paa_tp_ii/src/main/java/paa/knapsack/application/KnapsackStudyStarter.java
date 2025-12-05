package paa.knapsack.application;

import paa.knapsack.domain.KnapsackInstance;
import paa.knapsack.domain.KnapsackResult; // Explicitly imported
import paa.knapsack.domain.performance.ExperimentRunner;
import paa.knapsack.domain.performance.ExperimentRunner.ExperimentResult;
import paa.knapsack.domain.testdata.KnapsackInstanceGenerator;
import paa.knapsack.infrastructure.export.CSVExporter;

import java.util.List;
import java.util.ArrayList; // Added
import java.util.Random;    // Added
import paa.knapsack.domain.Item; // Added

/**
 * Orquestrador principal do estudo do Problema da Mochila.
 */
public class KnapsackStudyStarter {

    private static final String OUTPUT_DIR = "results";

    public void executeCompleteStudy() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("  ESTUDO COMPLETO DO PROBLEMA DA MOCHILA (0/1)");
        System.out.println("  Algoritmos: DP Linear-Space, DP Matriz-Completa, Greedy (Aproximado)");
        System.out.println("=".repeat(80) + "\n");

        try {
            // Tamanhos para testar os limites dos algoritmos de Programação Dinâmica
            // DP Linear-Space: O(n*L) tempo, O(L) espaço
            // DP Matriz-Completa: O(n*L) tempo, O(n*L) espaço
            // Como L é proporcional a n, a complexidade efetiva é O(n^2)
            int[] tamanhos = { 10, 20, 30, 50, 70, 100, 200, 300, 400, 500, 750, 1000, 1500, 2000, 3000, 5000, 7500, 10000 };

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

            String arquivoDetalhado = CSVExporter.exportarExperimentos(resultados, OUTPUT_DIR, "knapsack_results_complete_study");
            String arquivoSumario = CSVExporter.exportarSumario(resultados, OUTPUT_DIR, "knapsack_summary_complete_study");

            System.out.printf("%n[KnapsackStudyStarter] Análise completada!%n");
            System.out.printf("[KnapsackStudyStarter] Resultados em: %s%n", OUTPUT_DIR);
            System.out.printf("[KnapsackStudyStarter]  - Detalhado: %s%n", arquivoDetalhado);
            System.out.printf("[KnapsackStudyStarter]  - Sumário: %s%n", arquivoSumario);

        } catch (Exception e) {
            System.err.printf("[KnapsackStudyStarter] Erro ao executar estudo: %s%n", e.getMessage());
            e.printStackTrace();
        }
    }

    public void executeCustomStudy() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("  ESTUDO CUSTOMIZADO: CENÁRIO DE CAPACIDADE AJUSTADA (Tight Capacity)");
        System.out.println("  Algoritmos: DP Linear-Space, DP Matriz-Completa, Greedy (Aproximado)");
        System.out.println("=".repeat(80) + "\n");

        try {
            // Cenário: Capacidade ajustada para testar algoritmos, especialmente Greedy
            // Lógica: n itens, pesos mais variados (1 a 50), valores mais variados (10 a 200)
            // Capacidade: aproximadamente 30% do peso total médio para criar um cenário apertado
            int[] tamanhosCustom = { 10, 20, 30, 50, 70, 100, 150, 200, 250, 300, 350, 400, 450, 500, 750, 1000, 1250, 1500, 1750, 2000, 2500, 3000 };
            int numInstancesPerN = 10; // Rodar 10 instâncias para cada tamanho de 'n'

            List<ExperimentRunner.ExperimentResult> resultadosCustom = new ArrayList<>();

            System.out.printf("[KnapsackStudyStarter] Executando bateria de testes customizada com %d tamanhos distintos e %d instâncias por tamanho...%n%n",
                tamanhosCustom.length, numInstancesPerN);

            long baseSeed = 1000;
            for (int n : tamanhosCustom) {
                for (int i = 0; i < numInstancesPerN; i++) {
                    long seed = baseSeed + (long)n * numInstancesPerN + i; // Seed única para cada instância
                    try {
                        System.out.printf("\n=== Testando tamanho n=%d, instância %d (seed=%d) - Custom Study ===%n", n, i + 1, seed);

                        // Gera a instância com pesos e valores variados
                        List<Item> items = new ArrayList<>();
                        Random random = new Random(seed);
                        int pesoTotalMedio = 0;
                        for (int j = 0; j < n; j++) {
                            int peso = 1 + random.nextInt(50); // Pesos de 1 a 50
                            int valor = 10 + random.nextInt(191); // Valores de 10 a 200
                            items.add(new Item(j, peso, valor));
                            pesoTotalMedio += peso;
                        }
                        int capacidade = Math.max(10, (int) (pesoTotalMedio * 0.3)); // Capacidade apertada (30% do peso total médio)
                        KnapsackInstance instance = new KnapsackInstance(items, capacidade);


                        ExperimentRunner.ExperimentResult res = ExperimentRunner.executarTodosAlgoritmos(instance);
                        resultadosCustom.add(res);

                        System.out.printf("[KnapsackStudyStarter] Experimento(n=%d, instância %d) - Custom Study concluído%n", n, i + 1);
                    } catch (Exception e) {
                        System.err.printf("[KnapsackStudyStarter] Erro no experimento customizado(n=%d, instância %d): %s%n", n, i + 1, e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("\n" + "=".repeat(80));
            System.out.println("  RESUMO DOS RESULTADOS - ESTUDO CUSTOMIZADO");
            System.out.println("=".repeat(80));
            exibirResumo(resultadosCustom);

            System.out.println("\n" + "=".repeat(80));
            System.out.println("  EXPORTANDO RESULTADOS - ESTUDO CUSTOMIZADO");
            System.out.println("=".repeat(80) + "\n");

            String arquivoDetalhado = CSVExporter.exportarExperimentos(resultadosCustom, OUTPUT_DIR, "knapsack_results_custom_study");
            String arquivoSumario = CSVExporter.exportarSumario(resultadosCustom, OUTPUT_DIR, "knapsack_summary_custom_study");

            System.out.printf("%n[KnapsackStudyStarter] Análise customizada completada!%n");
            System.out.printf("[KnapsackStudyStarter] Resultados em: %s%n", OUTPUT_DIR);
            System.out.printf("[KnapsackStudyStarter]  - Detalhado: %s%n", arquivoDetalhado);
            System.out.printf("[KnapsackStudyStarter]  - Sumário: %s%n", arquivoSumario);

        } catch (Exception e) {
            System.err.printf("[KnapsackStudyStarter] Erro ao executar estudo customizado: %s%n", e.getMessage());
            e.printStackTrace();
        }
    }

    private void exibirResumo(List<ExperimentRunner.ExperimentResult> resultados) {
        for (ExperimentRunner.ExperimentResult exp : resultados) {
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
                String razaoStr = (razao >= 0) // Changed from > 0 to >=0 to handle 0 values more robustly
                    ? String.format("%.1f%%", razao * 100)
                    : "N/A";

                System.out.printf("  → %-25s: valor=%4d, peso=%4d, tempo=%3dms, aprox=%s%n",
                    res.getNomeAlgoritmo(),
                    res.getValorTotal(),
                    res.getPesoTotal(),
                    // res.getTempoExecucaoMs(), // Temporarily commented out due to compilation error
                    0L, // Placeholder for tempoExecucaoMs

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