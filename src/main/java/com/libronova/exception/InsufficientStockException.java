package com.libronova.exception;

/**
 * Excepción lanzada cuando no hay suficiente stock disponible
 */
public class InsufficientStockException extends LibroNovaException {
    
    public InsufficientStockException(String message) {
        super(message);
    }
    
    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
