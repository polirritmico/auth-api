/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.controller;

import cl.duoc.auth.api.AuthApi;
import cl.duoc.auth.dto.request.AuthRequest;
import cl.duoc.auth.dto.request.ChangeMyPasswordRequest;
import cl.duoc.auth.dto.request.ChangeUserPasswordRequest;
import cl.duoc.auth.dto.request.NewCredentialsRequest;
import cl.duoc.auth.dto.response.AuthResponse;
import cl.duoc.auth.dto.response.CredentialsResponse;
import cl.duoc.auth.service.AuthService;
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
public class AuthController implements AuthApi {
    private final AuthService service;

    @PostMapping
    public ResponseEntity<AuthResponse> auth(@Valid @RequestBody AuthRequest req) {
        return ResponseEntity.ok(service.auth(req));
    }

    @PostMapping("/register")
    public ResponseEntity<CredentialsResponse> createCredentials(@Valid @RequestBody NewCredentialsRequest req) {
        CredentialsResponse res = service.createCredentials(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updateMyPassword(@Valid @RequestBody ChangeMyPasswordRequest req, Principal principal) {
        service.changeOwnPassword(principal.getName(), req);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable String userId, @Valid @RequestBody ChangeUserPasswordRequest req) {
        service.changePassword(userId, req);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> revokeCredentials(@PathVariable String userId) {
        service.revokeCredentials(userId);
        return ResponseEntity.noContent().build();
    }
}
