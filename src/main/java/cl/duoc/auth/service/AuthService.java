/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.service;

import cl.duoc.auth.dto.request.AuthRequest;
import cl.duoc.auth.dto.request.ChangeMyPasswordRequest;
import cl.duoc.auth.dto.request.ChangeUserPasswordRequest;
import cl.duoc.auth.dto.request.NewCredentialsRequest;
import cl.duoc.auth.dto.response.AuthResponse;
import cl.duoc.auth.dto.response.CredentialsResponse;
import cl.duoc.auth.exception.InvalidCredentialsException;
import cl.duoc.auth.exception.NonCompliantCredentialsException;
import cl.duoc.auth.exception.NotUniqueUserException;
import cl.duoc.auth.exception.ResourceNotFoundException;
import cl.duoc.auth.mapper.DtoModelMapper;
import cl.duoc.auth.model.Credentials;
import cl.duoc.auth.repository.CredentialsRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final CredentialsRepository repo;
    private final JwtService jwt;
    private final BCryptPasswordEncoder encoder;
    private final DtoModelMapper mapper;

    private final int minPasswordLength = 6;
    private final String hasSpecialCharPattern = ".*[^a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\d].*";
    private final String hasNumbersPattern = ".*\\d.*";
    private final String hasLettersPattern = ".*[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ].*";

    private void logRequest(String msg) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : "anonymous";
        log.info(msg + " by user " + user);
    }

    private boolean checkPassword(String password, Credentials cred) {
        return encoder.matches(password, cred.getPassword());
    }

    private String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public AuthResponse auth(AuthRequest req) {
        log.info("Received auth request: {}", req);

        Credentials credentials = repo.findByUserId(req.getUser()).orElseThrow(() -> {
            log.error("Not matching username: {}", req.getUser());
            return new InvalidCredentialsException();
        });

        if (!checkPassword(req.getPassword(), credentials)) {
            log.error("Bad credentials.");
            throw new InvalidCredentialsException();
        }

        log.info("Generating the token");
        return new AuthResponse(jwt.generateToken(credentials.getUserId(), credentials.getRole()));
    }

    @Transactional
    public CredentialsResponse createCredentials(NewCredentialsRequest req) {
        logRequest("Received new credentials request: " + req.getUser());
        if (repo.existsByUserId(req.getUser())) {
            log.error("User already exists.");
            throw new NotUniqueUserException(req.getUser());
        }
        Credentials newCredentials = mapper.credentialsFromNewCredentialRequest(req, encodePassword(req.getPassword()));
        return mapper.toCredentialsResponse(repo.save(newCredentials));
    }

    @Transactional
    public void revokeCredentials(String userId) {
        logRequest("Received revoke credentials request for: " + userId);

        Credentials credentials = repo.findByUserId(userId).orElseThrow(() -> {
            log.error("Unexpected: Target active credentials not found for: {}", userId);
            return new ResourceNotFoundException("Credenciales no encontradas");
        });

        credentials.setRevokedAt(LocalDateTime.now());
        repo.save(credentials);
    }

    @Transactional
    public void changePassword(String userId, ChangeUserPasswordRequest req) {
        logRequest("Received admin change password reset request for user: " + userId);

        Credentials credentials = repo.findByUserId(userId).orElseThrow(() -> {
            log.error("Target credentials not found for: {}", userId);
            return new ResourceNotFoundException("Credenciales no encontradas");
        });

        validateNewPasswordMinLength(req.getNewPassword());
        validateNewPasswordHasLetters(req.getNewPassword());
        validateNewPasswordHasNumbers(req.getNewPassword());
        validateNewPasswordHasSpecialCharacters(req.getNewPassword());

        credentials.setPassword(encodePassword(req.getNewPassword()));
        repo.save(credentials);
    }

    @Transactional
    public void changeOwnPassword(String userId, ChangeMyPasswordRequest req) {
        logRequest("Received change password request: " + userId);

        Credentials credentials = repo.findByUserId(userId).orElseThrow(() -> {
            log.error("Unexpected: Not matching username: {} request by logged user", userId);
            return new InvalidCredentialsException();
        });

        validateNewPasswordIsDifferent(req.getCurrentPassword(), req.getNewPassword(), userId);
        validateOldPassword(req.getCurrentPassword(), credentials);
        validateNewPasswordMinLength(req.getNewPassword());
        validateNewPasswordHasLetters(req.getNewPassword());
        validateNewPasswordHasNumbers(req.getNewPassword());
        validateNewPasswordHasSpecialCharacters(req.getNewPassword());

        credentials.setPassword(encodePassword(req.getNewPassword()));
        repo.save(credentials);

        log.debug("Added validated credentials for user " + credentials.getUserId());
    }

    private void validateNewPasswordHasSpecialCharacters(String password) {
        if (!password.matches(hasSpecialCharPattern)) {
            log.error("Password validation failed: missing special character.");
            throw new NonCompliantCredentialsException(
                    "La nueva contraseña debe contener al menos un carácter especial.");
        }
    }

    private void validateNewPasswordHasNumbers(String password) {
        if (!password.matches(hasNumbersPattern)) {
            log.error("Password validation failed: missing numeric character.");
            throw new NonCompliantCredentialsException("La nueva contraseña debe contener al menos un número.");
        }
    }

    private void validateNewPasswordHasLetters(String password) {
        if (!password.matches(hasLettersPattern)) {
            log.error("Password validation failed: missing alphabetic character.");
            throw new NonCompliantCredentialsException("La nueva contraseña debe contener al menos una letra.");
        }
    }

    private void validateNewPasswordMinLength(String password) {
        if (password.length() < minPasswordLength) {
            log.error("New password does not complies min length");
            String msg = "La nueva contraseña debe tener al menos " + minPasswordLength + " caracteres.";
            throw new NonCompliantCredentialsException(msg);
        }
    }

    private void validateOldPassword(String reqCurrentPassword, Credentials cred) {
        if (!checkPassword(reqCurrentPassword, cred)) {
            log.error("Bad password by user: {}", cred.getUserId());
            throw new InvalidCredentialsException();
        }
    }

    private void validateNewPasswordIsDifferent(String reqCurPass, String reqNewPass, String userId) {
        if (reqCurPass.equals(reqNewPass)) {
            log.error("Old and new passwords are equal for user: {}", userId);
            throw new NonCompliantCredentialsException("La contraseña debe ser diferente");
        }
    }
}
