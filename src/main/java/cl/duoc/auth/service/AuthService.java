/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.service;

import cl.duoc.auth.dto.request.AuthRequest;
import cl.duoc.auth.dto.response.AuthResponse;
import cl.duoc.auth.model.Credentials;
import cl.duoc.auth.repository.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final CredentialsRepository repo;
    private final JwtService jwt;
    private final BCryptPasswordEncoder encoder;

    private boolean checkPassword(AuthRequest req, Credentials cred) {
        return encoder.matches(req.getPassword(), cred.getPassword());
    }

    public AuthResponse auth(AuthRequest req) {
        log.info("Received auth request: {}", req);
        String invalidCredMsg = "Credenciales inválidas.";

        Credentials credentials = repo.findByUserId(req.getUser()).orElseThrow(() -> {
            log.error("Not matching username: {}", req.getUser());
            return new ResponseStatusException(HttpStatus.UNAUTHORIZED, invalidCredMsg);
        });

        if (!checkPassword(req, credentials)) {
            log.error("Bad credentials");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, invalidCredMsg);
        }

        log.info("Generating the token");
        return new AuthResponse(jwt.generateToken(credentials.getUserId(), credentials.getRole()));
    }
}
