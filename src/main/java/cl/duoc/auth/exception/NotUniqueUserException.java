/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.exception;

import org.springframework.http.HttpStatus;

public class NotUniqueUserException extends DomainException {
    public NotUniqueUserException(String userId) {
        super("User already exists: " + userId, HttpStatus.CONFLICT);
    }
}
