/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.exception;

import org.springframework.http.HttpStatus;

public abstract class DomainException extends RuntimeException {
    private static final boolean ENABLE_SUPPRESSION = false;
    private static final boolean WRITABLE_STACK_TRACE = false;
    private final HttpStatus status;

    protected DomainException(String msg, HttpStatus status) {
        super(msg, null, ENABLE_SUPPRESSION, WRITABLE_STACK_TRACE);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
