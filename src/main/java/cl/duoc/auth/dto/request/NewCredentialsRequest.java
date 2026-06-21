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
@Schema(name = "NewCredentialsRequest", description = "Payload for registering new user credentials")
public class NewCredentialsRequest {
    @NotBlank(message = "El usuario es obligatorio")
    @Schema(description = "User identifier", example = "alan.brito", requiredMode = RequiredMode.REQUIRED)
    private String user;

    @NotBlank(message = "El rol del usuario es obligatorio")
    @Schema(description = "Role assigned to the user", example = "user", requiredMode = RequiredMode.REQUIRED)
    private String role;

    @NotBlank(message = "El password es obligatorio")
    @Schema(
            description = "New password adhering to security policies",
            example = "abc123!",
            requiredMode = RequiredMode.REQUIRED)
    private String password;
}
