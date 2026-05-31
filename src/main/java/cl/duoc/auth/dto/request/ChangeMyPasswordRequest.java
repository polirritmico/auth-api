/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMyPasswordRequest {
    @NotBlank(message = "El password actual es obligatorio")
    private String currentPassword;

    @NotBlank(message = "El nuevo password es obligatorio")
    private String newPassword;
}
