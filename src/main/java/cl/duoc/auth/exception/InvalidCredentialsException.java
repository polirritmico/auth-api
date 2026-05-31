/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() {
        super("Provided invalid credentials.", HttpStatus.UNAUTHORIZED);
    }

    public InvalidCredentialsException(String msg) {
        super(msg, HttpStatus.UNAUTHORIZED);
    }
}
