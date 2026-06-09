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
@Schema(name = "ChangeUserPasswordRequest", description = "Payload for an administrator forcing a user password reset")
public class ChangeUserPasswordRequest {
    @NotBlank(message = "El nuevo password es obligatorio")
    @Schema(
            description = "The new password adhering to security policies",
            example = "123abc!",
            requiredMode = RequiredMode.REQUIRED)
    private String newPassword;
}
