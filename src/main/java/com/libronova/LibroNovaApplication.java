package com.libronova;

import com.libronova.ui.LibroNovaUI;
import com.libronova.util.Logger;

import javax.swing.*;

/**
 * Clase principal de la aplicación LibroNova
 */
public class LibroNovaApplication {
    private static final Logger logger = Logger.getInstance();

    public static void main(String[] args) {
        try {
            // Configurar el look and feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warning("No se pudo configurar el look and feel del sistema: " + e.getMessage());
        }

        // Configurar el manejo de excepciones no capturadas
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            logger.error("Excepción no capturada en hilo " + thread.getName() + ": " + exception.getMessage(), exception);
            JOptionPane.showMessageDialog(
                null,
                "Ha ocurrido un error inesperado: " + exception.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        });

        try {
            logger.info("Iniciando aplicación LibroNova");
            
            // Mostrar mensaje de bienvenida
            JOptionPane.showMessageDialog(
                null,
                "¡Bienvenido a LibroNova!\n" +
                "Sistema de Gestión de Bibliotecas\n\n" +
                "Versión 1.0.0",
                "LibroNova",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Inicializar y mostrar la interfaz principal
            LibroNovaUI ui = new LibroNovaUI();
            ui.showMainMenu();

        } catch (Exception e) {
            logger.error("Error fatal en la aplicación: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(
                null,
                "Error fatal en la aplicación: " + e.getMessage() + "\n\n" +
                "Por favor, revise el archivo de log para más detalles.",
                "Error Fatal",
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            logger.info("Aplicación LibroNova finalizada");
            logger.close();
        }
    }
}
