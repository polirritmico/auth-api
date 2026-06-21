/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CredentialsResponse", description = "Payload for user credentials")
public class CredentialsResponse {
    @Schema(description = "User identifier", example = "test", requiredMode = RequiredMode.REQUIRED)
    private String userId;

    @Schema(description = "User role", example = "admin", requiredMode = RequiredMode.REQUIRED)
    private String role;
}
