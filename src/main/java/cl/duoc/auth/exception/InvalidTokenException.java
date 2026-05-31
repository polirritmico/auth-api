/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends DomainException {
    public InvalidTokenException(String msg) {
        super(msg, HttpStatus.UNAUTHORIZED);
    }
}
