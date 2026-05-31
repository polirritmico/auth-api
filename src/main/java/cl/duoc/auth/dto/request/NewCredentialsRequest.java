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
public class NewCredentialsRequest {
    @NotBlank(message = "El usuario es obligatorio")
    private String user;

    @NotBlank(message = "El rol del usuario es obligatorio")
    private String role;

    @NotBlank(message = "El password es obligatorio")
    private String password;
}
