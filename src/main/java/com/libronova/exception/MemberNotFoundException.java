package com.libronova.exception;

/**
 * Excepción lanzada cuando no se encuentra un miembro
 */
public class MemberNotFoundException extends LibroNovaException {
    
    public MemberNotFoundException(String message) {
        super(message);
    }
    
    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
