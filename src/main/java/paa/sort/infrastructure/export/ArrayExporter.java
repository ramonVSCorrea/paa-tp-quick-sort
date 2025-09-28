package paa.sort.infrastructure.export;

import paa.sort.domain.performance.PerformanceResult;
import paa.sort.domain.testdata.DataType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Classe responsavel por exportar arrays testados e resultados para arquivos .txt
 */
public class ArrayExporter {
    private final String baseOutputDirectory;

    public ArrayExporter() {
        this.baseOutputDirectory = "arrays_testados";
        createDirectoryStructure();
    }

    /**
     * Cria a estrutura de diretorios organizada por tipo de dados
     */
    private void createDirectoryStructure() {
        try {
            // Diretorio principal
            Files.createDirectories(Paths.get(baseOutputDirectory));

            // Subdiretorios para cada tipo de dados
            String[] dataTypes = {"aleatorio", "ordenado", "ordenado_inverso", "muitos_duplicados", "pior_caso"};

            for (String dataType : dataTypes) {
                String typeDir = baseOutputDirectory + "/" + dataType;
                Files.createDirectories(Paths.get(typeDir + "/arrays_originais"));
                Files.createDirectories(Paths.get(typeDir + "/arrays_ordenados"));
                Files.createDirectories(Paths.get(typeDir + "/resultados"));
            }

        } catch (IOException e) {
            System.err.println("Erro ao criar estrutura de diretorios: " + e.getMessage());
        }
    }

    /**
     * Converte DataType para nome de diretorio
     */
    private String getDirectoryName(DataType dataType) {
        return switch (dataType) {
            case RANDOM -> "aleatorio";
            case SORTED -> "ordenado";
            case REVERSE_SORTED -> "ordenado_inverso";
            case MANY_DUPLICATES -> "muitos_duplicados";
            case WORST_CASE -> "pior_caso";
        };
    }

    /**
     * Salva o array original testado
     */
    public void saveOriginalArray(DataType dataType, int size, int[] originalArray) {
        String dirName = getDirectoryName(dataType);
        String filename = String.format("%s/%s/arrays_originais/array_original_%s_%d.txt",
            baseOutputDirectory, dirName, dataType.name().toLowerCase(), size);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("========================================\n");
            writer.write("ARRAY ORIGINAL TESTADO\n");
            writer.write("========================================\n");
            writer.write(String.format("Tipo: %s\n", dataType.getDescription()));
            writer.write(String.format("Tamanho: %d elementos\n", size));
            writer.write(String.format("Data/Hora: %s\n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            writer.write("----------------------------------------\n");
            writer.write("Elementos:\n");

            writeAllArrayElements(writer, originalArray);

        } catch (IOException e) {
            System.err.println("Erro ao salvar array original: " + e.getMessage());
        }
    }

    /**
     * Salva o array apos ordenacao
     */
    public void saveSortedArray(String algorithmName, DataType dataType, int size, int[] sortedArray) {
        String dirName = getDirectoryName(dataType);
        String cleanAlgorithmName = algorithmName.replace(" ", "_")
                                                .replace("(", "")
                                                .replace(")", "")
                                                .replace("=", "-")
                                                .replace(",", "");

        String filename = String.format("%s/%s/arrays_ordenados/array_ordenado_%s_%s_%d.txt",
            baseOutputDirectory, dirName, cleanAlgorithmName, dataType.name().toLowerCase(), size);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("========================================\n");
            writer.write("ARRAY APOS ORDENACAO\n");
            writer.write("========================================\n");
            writer.write(String.format("Algoritmo: %s\n", algorithmName));
            writer.write(String.format("Tipo Original: %s\n", dataType.getDescription()));
            writer.write(String.format("Tamanho: %d elementos\n", size));
            writer.write(String.format("Data/Hora: %s\n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            writer.write("----------------------------------------\n");
            writer.write("Elementos ordenados:\n");

            writeAllArrayElements(writer, sortedArray);

            // Verificacao de ordenacao
            writer.write("\n----------------------------------------\n");
            writer.write("Verificacao: ");
            writer.write(isArraySorted(sortedArray) ? "ORDENADO CORRETAMENTE" : "ERRO NA ORDENACAO");
            writer.write("\n");

        } catch (IOException e) {
            System.err.println("Erro ao salvar array ordenado: " + e.getMessage());
        }
    }

    /**
     * Salva os resultados dos testes de performance
     */
    public void saveTestResults(List<PerformanceResult> results, String testType, DataType dataType) {
        String dirName = getDirectoryName(dataType);
        String filename = String.format("%s/%s/resultados/resultados_%s.txt",
            baseOutputDirectory, dirName, testType.toLowerCase().replace(" ", "_"));

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("========================================\n");
            writer.write("RESULTADOS DOS TESTES DE PERFORMANCE\n");
            writer.write("========================================\n");
            writer.write(String.format("Tipo de Teste: %s\n", testType));
            writer.write(String.format("Data/Hora: %s\n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            writer.write(String.format("Total de Testes: %d\n", results.size()));
            writer.write("----------------------------------------\n\n");

            for (PerformanceResult result : results) {
                writer.write(String.format("Algoritmo: %s\n", result.getAlgorithmName()));
                writer.write(String.format("Tamanho do Array: %d elementos\n", result.getArraySize()));
                writer.write(String.format("Tempo de Execucao: %.3f ms (%.0f ns)\n",
                    result.getExecutionTimeMillis(), (double)result.getExecutionTimeNanos()));
                writer.write(String.format("Status: %s\n", result.isSuccessful() ? "SUCESSO" : "FALHA"));
                writer.write("----------------------------------------\n");
            }

        } catch (IOException e) {
            System.err.println("Erro ao salvar resultados: " + e.getMessage());
        }
    }

    /**
     * Salva um resumo geral de todos os testes
     */
    public void saveGeneralSummary(List<PerformanceResult> allResults, int optimalThreshold) {
        String filename = String.format("%s/resumo_geral.txt", baseOutputDirectory);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("========================================\n");
            writer.write("RESUMO GERAL DO ESTUDO COMPARATIVO\n");
            writer.write("========================================\n");
            writer.write(String.format("Data/Hora: %s\n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            writer.write(String.format("Threshold Otimo Determinado: M=%d\n", optimalThreshold));
            writer.write(String.format("Total de Testes Realizados: %d\n", allResults.size()));
            writer.write("----------------------------------------\n\n");

            // Agrupa por tipo de dados
            String currentDataType = "";
            for (PerformanceResult result : allResults) {
                if (!result.getDataType().equals(currentDataType)) {
                    currentDataType = result.getDataType();
                    writer.write(String.format("\n=== RESULTADOS PARA: %s ===\n", currentDataType.toUpperCase()));
                }

                writer.write(String.format("Tamanho: %d | %s: %.3f ms | %s\n",
                    result.getArraySize(),
                    result.getAlgorithmName(),
                    result.getExecutionTimeMillis(),
                    result.isSuccessful() ? "OK" : "ERRO"));
            }

            writer.write("\n========================================\n");
            writer.write("ESTRUTURA DOS ARQUIVOS GERADOS:\n");
            writer.write("arrays_testados/\n");
            writer.write("|-- aleatorio/\n");
            writer.write("|   |-- arrays_originais/\n");
            writer.write("|   |-- arrays_ordenados/\n");
            writer.write("|   |-- resultados/\n");
            writer.write("|-- ordenado/\n");
            writer.write("|   |-- arrays_originais/\n");
            writer.write("|   |-- arrays_ordenados/\n");
            writer.write("|   |-- resultados/\n");
            writer.write("|-- ... (outros tipos)\n");
            writer.write("========================================\n");

        } catch (IOException e) {
            System.err.println("Erro ao salvar resumo geral: " + e.getMessage());
        }
    }

    /**
     * Escreve todos os elementos do array no arquivo
     */
    private void writeAllArrayElements(FileWriter writer, int[] array) throws IOException {
        for (int i = 0; i < array.length; i++) {
            writer.write(String.format("[%d] = %d\n", i, array[i]));
        }

        writer.write(String.format("\nTotal de elementos: %d\n", array.length));
    }

    /**
     * Verifica se o array esta ordenado
     */
    private boolean isArraySorted(int[] array) {
        if (array == null || array.length <= 1) {
            return true;
        }

        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }
}
