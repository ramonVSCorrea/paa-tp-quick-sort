package paa.sort;

import paa.sort.application.QuickSortComparativeStudy;
import paa.sort.domain.exceptions.ValidationException;
import paa.sort.infrastructure.logging.ExceptionLogger;

/**
 * Classe principal para executar o estudo comparativo dos algoritmos Quicksort
 */
public class Main {
    private static final ExceptionLogger exceptionLogger = ExceptionLogger.getInstance();

    public static void main(String[] args) {
        try {
            validateCommandLineArguments(args);

            System.out.println("Iniciando estudo comparativo de algoritmos Quicksort...");
            exceptionLogger.logInfo("Aplicação iniciada", "Main.main");

            QuickSortComparativeStudy study = new QuickSortComparativeStudy();
            study.executeCompleteStudy();

            System.out.println("\\nEstudo concluído com sucesso!");
            exceptionLogger.logInfo("Aplicação finalizada com sucesso", "Main.main");

        } catch (ValidationException e) {
            handleValidationError(e);
        } catch (Exception e) {
            handleUnexpectedError(e);
        } finally {
            // Cleanup resources
            try {
                exceptionLogger.shutdown();
            } catch (Exception e) {
                System.err.println("Erro ao finalizar logger: " + e.getMessage());
            }
        }
    }

    /**
     * Valida argumentos da linha de comando
     */
    private static void validateCommandLineArguments(String[] args) throws ValidationException {
        if (args.length > 0) {
            // Por enquanto não aceitamos argumentos, mas validamos para futuras extensões
            for (String arg : args) {
                if (arg == null || arg.trim().isEmpty()) {
                    throw new ValidationException("Argumento vazio ou null não permitido", "args", arg);
                }
            }
        }

        exceptionLogger.logInfo("Argumentos da linha de comando validados: " + args.length + " argumentos",
                "Validação CLI");
    }

    /**
     * Trata erros de validação
     */
    private static void handleValidationError(ValidationException e) {
        String errorMessage = String.format("ERRO DE VALIDAÇÃO: %s (Campo: %s, Valor: %s)",
                e.getMessage(), e.getValidationField(), e.getInvalidValue());

        System.err.println(errorMessage);
        exceptionLogger.logValidationError(e, "Main.main - Erro de validação");

        System.exit(1);
    }

    /**
     * Trata erros inesperados
     */
    private static void handleUnexpectedError(Exception e) {
        String errorMessage = "ERRO INESPERADO: " + e.getMessage();

        System.err.println(errorMessage);
        System.err.println("Stack trace:");
        e.printStackTrace();

        exceptionLogger.logGenericError("UNEXPECTED_ERROR",
                "Erro inesperado na aplicação principal: " + e.getMessage(),
                e,
                "Main.main - Falha crítica");

        System.exit(4);
    }
}