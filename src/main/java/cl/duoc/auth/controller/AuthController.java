/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.controller;

import cl.duoc.auth.dto.request.AuthRequest;
import cl.duoc.auth.dto.request.ChangeMyPasswordRequest;
import cl.duoc.auth.dto.request.ChangeUserPasswordRequest;
import cl.duoc.auth.dto.request.NewCredentialsRequest;
import cl.duoc.auth.dto.response.AuthResponse;
import cl.duoc.auth.dto.response.CredentialsResponse;
import cl.duoc.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping
    @Operation(
            summary = "Authenticate user",
            description = "Validates user credentials and generates a JWT token for accessing secured endpoints.")
    @ApiResponse(responseCode = "200", description = "Authentication successful, JWT token returned")
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    @ApiResponse(responseCode = "401", description = "Invalid credentials or account revoked")
    public ResponseEntity<AuthResponse> auth(@Valid @RequestBody AuthRequest req) {
        return ResponseEntity.ok(service.auth(req));
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user credentials", description = "Creates a new credentials record.")
    @ApiResponse(responseCode = "201", description = "Credentials created successfully")
    public ResponseEntity<CredentialsResponse> createCredentials(@Valid @RequestBody NewCredentialsRequest req) {
        CredentialsResponse res = service.createCredentials(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/me/password")
    @Operation(
            summary = "Update own password",
            description =
                    "Allows an authenticated user to change their own password. Strictly requires the current password for verification.")
    @ApiResponse(responseCode = "204", description = "Password updated successfully")
    public ResponseEntity<Void> updateMyPassword(@Valid @RequestBody ChangeMyPasswordRequest req, Principal principal) {
        service.changeOwnPassword(principal.getName(), req);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/password")
    @Operation(
            summary = "Force password reset (Admin)",
            description =
                    "Allows an administrator to forcefully reset any user's password. Bypasses the current password check.")
    @ApiResponse(responseCode = "204", description = "Password reset successfully")
    public ResponseEntity<Void> changePassword(
            @PathVariable String userId, @Valid @RequestBody ChangeUserPasswordRequest req) {
        service.changePassword(userId, req);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    @Operation(
            summary = "Revoke credentials (Admin)",
            description =
                    "Soft-deletes a user's credentials by stamping the revokedAt timestamp. Prevents all future authentication attempts for this user ID.")
    @ApiResponse(responseCode = "204", description = "Credentials successfully revoked")
    public ResponseEntity<Void> revokeCredentials(@PathVariable String userId) {
        service.revokeCredentials(userId);
        return ResponseEntity.noContent().build();
    }
}
