package paa.sort.infrastructure.logging;

import paa.sort.domain.exceptions.SortingException;
import paa.sort.domain.exceptions.StackOverflowException;
import paa.sort.domain.exceptions.ValidationException;
import paa.sort.domain.exceptions.FileOperationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

/**
 * Classe responsável por logging estruturado de exceções
 */
public class ExceptionLogger {
    private static final Logger logger = Logger.getLogger(ExceptionLogger.class.getName());
    private static final String LOG_FILE = "logs/sorting_exceptions.log";
    private static ExceptionLogger instance;
    private static boolean initialized = false;

    private ExceptionLogger() {
        initializeLogger();
    }

    public static synchronized ExceptionLogger getInstance() {
        if (instance == null) {
            instance = new ExceptionLogger();
        }
        return instance;
    }

    private void initializeLogger() {
        try {
            // Cria diretório de logs se não existir
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("logs"));
            
            // Configura FileHandler
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            
            // Configura console handler para erros críticos
            logger.setLevel(Level.ALL);
            
            initialized = true;
            logger.info("ExceptionLogger inicializado com sucesso");
        } catch (IOException e) {
            System.err.println("Erro ao inicializar ExceptionLogger: " + e.getMessage());
        }
    }

    /**
     * Log de exceção de ordenação com contexto completo
     */
    public void logSortingException(SortingException exception, String additionalContext) {
        if (!initialized) return;
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logMessage = String.format(
            "[%s] SORTING_EXCEPTION | Algoritmo: %s | Tipo: %s | Tamanho: %d | Tempo: %dns | Contexto: %s | Detalhes: %s",
            timestamp,
            exception.getAlgorithmName(),
            exception.getDataType(),
            exception.getArraySize(),
            exception.getExecutionTime(),
            additionalContext,
            exception.getMessage()
        );
        
        logger.severe(logMessage);
        
        if (exception.getCause() != null) {
            logger.severe("Causa raiz: " + exception.getCause().getMessage());
        }
    }

    /**
     * Log de StackOverflow com informações específicas
     */
    public void logStackOverflow(StackOverflowException exception, String additionalContext) {
        if (!initialized) return;
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logMessage = String.format(
            "[%s] STACK_OVERFLOW | Algoritmo: %s | Profundidade: %d | Threshold: %d | Tamanho: %d | Contexto: %s",
            timestamp,
            exception.getAlgorithmName(),
            exception.getRecursionDepth(),
            exception.getThreshold(),
            exception.getArraySize(),
            additionalContext
        );
        
        logger.severe(logMessage);
        
        // Log de recomendação
        String recommendation = String.format(
            "RECOMENDAÇÃO: Considere aumentar o threshold para %d ou usar algoritmo iterativo para tamanhos > %d",
            exception.getThreshold() * 2,
            exception.getArraySize()
        );
        logger.warning(recommendation);
    }

    /**
     * Log de erro de validação
     */
    public void logValidationError(ValidationException exception, String additionalContext) {
        if (!initialized) return;
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logMessage = String.format(
            "[%s] VALIDATION_ERROR | Campo: %s | Valor: %s | Contexto: %s | Detalhes: %s",
            timestamp,
            exception.getValidationField(),
            exception.getInvalidValue(),
            additionalContext,
            exception.getMessage()
        );
        
        logger.warning(logMessage);
    }

    /**
     * Log de erro de arquivo
     */
    public void logFileOperationError(FileOperationException exception, String additionalContext) {
        if (!initialized) return;
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logMessage = String.format(
            "[%s] FILE_OPERATION_ERROR | Operação: %s | Arquivo: %s | Contexto: %s | Detalhes: %s",
            timestamp,
            exception.getOperation(),
            exception.getFilePath(),
            additionalContext,
            exception.getMessage()
        );
        
        logger.severe(logMessage);
        
        if (exception.getCause() != null) {
            logger.severe("Causa raiz: " + exception.getCause().getMessage());
        }
    }

    /**
     * Log de erro genérico com contexto
     */
    public void logGenericError(String errorType, String message, Throwable cause, String additionalContext) {
        if (!initialized) return;
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logMessage = String.format(
            "[%s] %s | Contexto: %s | Detalhes: %s",
            timestamp,
            errorType,
            additionalContext,
            message
        );
        
        logger.severe(logMessage);
        
        if (cause != null) {
            logger.severe("Causa raiz: " + cause.getMessage());
            // Log do stack trace para erros críticos
            logger.severe("Stack trace: " + java.util.Arrays.toString(cause.getStackTrace()));
        }
    }

    /**
     * Log de warning com contexto
     */
    public void logWarning(String message, String additionalContext) {
        if (!initialized) return;
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logMessage = String.format(
            "[%s] WARNING | Contexto: %s | Detalhes: %s",
            timestamp,
            additionalContext,
            message
        );
        
        logger.warning(logMessage);
    }

    /**
     * Log de info com contexto
     */
    public void logInfo(String message, String additionalContext) {
        if (!initialized) return;
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logMessage = String.format(
            "[%s] INFO | Contexto: %s | Detalhes: %s",
            timestamp,
            additionalContext,
            message
        );
        
        logger.info(logMessage);
    }

    /**
     * Finaliza o logger
     */
    public void shutdown() {
        if (initialized) {
            logger.info("ExceptionLogger sendo finalizado");
            // Fecha todos os handlers
            for (var handler : logger.getHandlers()) {
                handler.close();
            }
            initialized = false;
        }
    }
}
