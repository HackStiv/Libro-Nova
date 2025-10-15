package com.libronova.exception;

/**
 * Excepción lanzada cuando un miembro excede su límite de préstamos
 */
public class MemberLimitExceededException extends LibroNovaException {
    
    public MemberLimitExceededException(String message) {
        super(message);
    }
    
    public MemberLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
