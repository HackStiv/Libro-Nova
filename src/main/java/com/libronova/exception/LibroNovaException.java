package com.libronova.exception;

/**
 * Excepción base para el sistema LibroNova
 */
public class LibroNovaException extends Exception {
    
    public LibroNovaException(String message) {
        super(message);
    }
    
    public LibroNovaException(String message, Throwable cause) {
        super(message, cause);
    }
}
