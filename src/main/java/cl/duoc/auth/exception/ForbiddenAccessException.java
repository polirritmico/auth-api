/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenAccessException extends DomainException {
    public ForbiddenAccessException() {
        super("Access denied.", HttpStatus.FORBIDDEN);
    }

    public ForbiddenAccessException(String msg) {
        super(msg, HttpStatus.FORBIDDEN);
    }
}
