package com.libronova.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase singleton para el manejo de logs de la aplicación
 */
public class Logger {
    private static Logger instance;
    private static final String LOG_FILE = "app.log";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private PrintWriter writer;
    
    private Logger() {
        try {
            writer = new PrintWriter(new FileWriter(LOG_FILE, true));
        } catch (IOException e) {
            System.err.println("Error al inicializar el logger: " + e.getMessage());
        }
    }
    
    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }
    
    /**
     * Registra un mensaje de información
     */
    public void info(String message) {
        log("INFO", message);
    }
    
    /**
     * Registra un mensaje de advertencia
     */
    public void warning(String message) {
        log("WARNING", message);
    }
    
    /**
     * Registra un mensaje de error
     */
    public void error(String message) {
        log("ERROR", message);
    }
    
    /**
     * Registra un mensaje de error con excepción
     */
    public void error(String message, Throwable throwable) {
        log("ERROR", message + " - " + throwable.getMessage());
        if (throwable.getStackTrace() != null) {
            for (StackTraceElement element : throwable.getStackTrace()) {
                log("ERROR", "  at " + element.toString());
            }
        }
    }
    
    /**
     * Registra un mensaje de debug
     */
    public void debug(String message) {
        log("DEBUG", message);
    }
    
    /**
     * Método privado para escribir en el archivo de log
     */
    private void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String logMessage = String.format("[%s] %s: %s", timestamp, level, message);
        
        // Escribir en consola
        System.out.println(logMessage);
        
        // Escribir en archivo
        if (writer != null) {
            writer.println(logMessage);
            writer.flush();
        }
    }
    
    /**
     * Cierra el logger y libera recursos
     */
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
