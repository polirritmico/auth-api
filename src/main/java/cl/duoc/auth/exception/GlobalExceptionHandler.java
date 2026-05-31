/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.exception;

import cl.duoc.auth.exception.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.error("Param validation failed: {}", ex);
        return ResponseEntity.badRequest()
                .body(ex.getBindingResult().getFieldErrors().stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage,
                                (prevErr, newErr) -> prevErr + ", " + newErr)));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        log.error("Resource not found at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, req);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainException(DomainException ex, HttpServletRequest req) {
        HttpStatus status = ex.getStatus();
        log.error("Domain exception thrown at {} [{}]: {}", req.getRequestURI(), status.value(), ex.getMessage(), ex);
        return buildErrorResponse(status, ex, req);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            HttpStatus status, RuntimeException ex, HttpServletRequest req) {
        return ResponseEntity.status(status)
                .body(ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(req.getRequestURI())
                        .kind(ex.getClass().getSimpleName())
                        .build());
    }
}
