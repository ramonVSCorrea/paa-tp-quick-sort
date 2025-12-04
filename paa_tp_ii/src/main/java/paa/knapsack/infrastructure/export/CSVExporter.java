package paa.knapsack.infrastructure.export;

import paa.knapsack.domain.KnapsackResult;
import paa.knapsack.domain.performance.ExperimentRunner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exportador de resultados para formato CSV.
 */
public class CSVExporter {

    private static final DateTimeFormatter TIME_FORMATTER =
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static String exportarExperimentos(
            List<ExperimentRunner.ExperimentResult> experimentos,
            String caminhoSaida) throws IOException {

        File dir = new File(caminhoSaida);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String nomeArquivo = String.format("knapsack_results_%s.csv", timestamp);
        String caminhoCompleto = new File(dir, nomeArquivo).getAbsolutePath();

        try (FileWriter writer = new FileWriter(caminhoCompleto)) {
            writer.write("n,L,seed,algoritmo,valor,peso,tempo_ms,memoria_kb,razao_aprox\n");

            for (ExperimentRunner.ExperimentResult exp : experimentos) {
                for (KnapsackResult res : exp.resultados) {
                    double razao = exp.calcularRazaoAproximacao(res);
                    String linha = String.format(
                        "%d,%d,%d,%s,%d,%d,%d,%d,%.4f\n",
                        exp.numItems, exp.capacidade, exp.seed,
                        res.getNomeAlgoritmo(), res.getValorTotal(),
                        res.getPesoTotal(), res.getTempoExecutacaoMs(),
                        res.getMemoriaUsadaKb(), razao > 0 ? razao : 0.0
                    );
                    writer.write(linha);
                }
            }

            System.out.printf("[CSVExporter] Arquivo exportado: %s%n", caminhoCompleto);
        }

        return caminhoCompleto;
    }

    public static String exportarSumario(
            List<ExperimentRunner.ExperimentResult> experimentos,
            String caminhoSaida) throws IOException {

        File dir = new File(caminhoSaida);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String caminhoCompleto = new File(dir, "knapsack_summary.csv").getAbsolutePath();

        try (FileWriter writer = new FileWriter(caminhoCompleto)) {
            writer.write("n,L,seed");

            java.util.Set<String> algoritmos = new java.util.LinkedHashSet<>();
            for (ExperimentRunner.ExperimentResult exp : experimentos) {
                for (KnapsackResult res : exp.resultados) {
                    algoritmos.add(res.getNomeAlgoritmo());
                }
            }

            for (String algo : algoritmos) {
                writer.write(String.format(",%s_valor,%s_tempo_ms", algo, algo));
            }
            writer.write("\n");

            for (ExperimentRunner.ExperimentResult exp : experimentos) {
                writer.write(String.format("%d,%d,%d", exp.numItems, exp.capacidade, exp.seed));

                java.util.Map<String, KnapsackResult> resultsMap = new java.util.HashMap<>();
                for (KnapsackResult res : exp.resultados) {
                    resultsMap.put(res.getNomeAlgoritmo(), res);
                }

                for (String algo : algoritmos) {
                    KnapsackResult res = resultsMap.get(algo);
                    if (res != null) {
                        writer.write(String.format(",%d,%d",
                            res.getValorTotal(), res.getTempoExecutacaoMs()));
                    } else {
                        writer.write(",-,-");
                    }
                }
                writer.write("\n");
            }

            System.out.printf("[CSVExporter] Sumário exportado: %s%n", caminhoCompleto);
        }

        return caminhoCompleto;
    }
}

