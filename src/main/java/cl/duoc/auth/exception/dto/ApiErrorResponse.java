/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "ApiErrorResponse", description = "Standaridized error response payload")
public class ApiErrorResponse {

    @Schema(description = "Time the error occurred", example = "2026-06-09T17:07:23")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP error description", example = "Bad Request")
    private String error;

    @Schema(description = "Detailed error message", example = "El usuario es obligatorio")
    private String message;

    @Schema(description = "Endpoint where the error occurred", example = "/api/v1/auth")
    private String path;

    @Schema(description = "Exception class name", example = "MethodArgumentNotValidException")
    private String kind;
}
