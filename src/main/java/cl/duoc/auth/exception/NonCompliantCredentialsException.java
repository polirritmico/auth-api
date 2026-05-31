/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.exception;

import org.springframework.http.HttpStatus;

public class NonCompliantCredentialsException extends DomainException {
    public NonCompliantCredentialsException(String msg) {
        super(msg, HttpStatus.BAD_REQUEST);
    }
}
