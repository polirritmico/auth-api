/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.api;

import cl.duoc.auth.dto.request.AuthRequest;
import cl.duoc.auth.dto.request.ChangeMyPasswordRequest;
import cl.duoc.auth.dto.request.ChangeUserPasswordRequest;
import cl.duoc.auth.dto.request.NewCredentialsRequest;
import cl.duoc.auth.dto.response.AuthResponse;
import cl.duoc.auth.dto.response.CredentialsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication")
public interface AuthApi {
    @Operation(
            summary = "Authenticate user",
            description = "Validates user credentials and generates a JWT token for accessing secured endpoints.")
    @ApiResponse(
            responseCode = "200",
            description = "Authentication successful, JWT token returned",
            content = @Content)
    @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content)
    @ApiResponse(responseCode = "401", description = "Invalid credentials or account revoked", content = @Content)
    public ResponseEntity<AuthResponse> auth(AuthRequest req);

    @Operation(summary = "Register new user credentials", description = "Creates a new credentials record.")
    @ApiResponse(responseCode = "201", description = "Credentials created successfully")
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request payload or non-compliant password",
            content = @Content)
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden to create credentials with elevated roles",
            content = @Content)
    @ApiResponse(responseCode = "409", description = "User credentials already exists", content = @Content)
    public ResponseEntity<CredentialsResponse> createCredentials(NewCredentialsRequest req);

    @Operation(
            summary = "Update own password",
            description =
                    "Allows an authenticated user to change their own password. Strictly requires the current password for verification.")
    @ApiResponse(responseCode = "204", description = "Password updated successfully")
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request payload or non-compliant password",
            content = @Content)
    @ApiResponse(responseCode = "401", description = "Current password does not match", content = @Content)
    public ResponseEntity<Void> updateMyPassword(ChangeMyPasswordRequest req, Principal principal);

    @Operation(
            summary = "Force password reset (Admin)",
            description = "Allows an administrator to forcefully reset any user's password. Bypasses password check.")
    @ApiResponse(responseCode = "204", description = "Password reset successfully")
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request payload or non-compliant new password",
            content = @Content)
    @ApiResponse(responseCode = "404", description = "Target credentials not found", content = @Content)
    public ResponseEntity<Void> changePassword(String userId, ChangeUserPasswordRequest req);

    @Operation(
            summary = "Revoke credentials (Admin)",
            description =
                    "Soft-deletes a user's credentials by stamping the revokedAt timestamp. Prevents all future authentication attempts for this user ID.")
    @ApiResponse(responseCode = "204", description = "Credentials successfully revoked")
    @ApiResponse(responseCode = "404", description = "Target credentials not found", content = @Content)
    public ResponseEntity<Void> revokeCredentials(String userId);
}
