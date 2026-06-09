/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AuthRequest", description = "Payload for user authentication")
public class AuthRequest {
    @NotBlank(message = "El usuario es obligatorio")
    @Schema(description = "User identifier", example = "test", requiredMode = RequiredMode.REQUIRED)
    private String user;

    @NotBlank(message = "El password es obligatorio")
    @Schema(description = "User password", example = "password", requiredMode = RequiredMode.REQUIRED)
    private String password;
}
