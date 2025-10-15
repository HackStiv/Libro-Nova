package com.libronova.exception;

/**
 * Excepción lanzada cuando no se encuentra un libro
 */
public class BookNotFoundException extends LibroNovaException {
    
    public BookNotFoundException(String message) {
        super(message);
    }
    
    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
