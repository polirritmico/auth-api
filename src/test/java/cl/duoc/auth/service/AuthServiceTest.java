/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cl.duoc.auth.dto.request.AuthRequest;
import cl.duoc.auth.dto.response.AuthResponse;
import cl.duoc.auth.exception.InvalidCredentialsException;
import cl.duoc.auth.model.Credentials;
import cl.duoc.auth.repository.CredentialsRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService service;

    @Mock
    private CredentialsRepository repo;

    @BeforeEach
    void setup() {
        repo.deleteAll();
        repo.save(Credentials.builder()
                .id(1L)
                .userId("TestUser")
                .password("Password")
                .role("admin")
                .createdAt(LocalDateTime.of(2026, 1, 1, 0, 0))
                .build());
    }

    @Test
    void shouldGenerateTokenWithValidCredentialsRequest() {
        AuthRequest given = new AuthRequest("TestUser", "Password");
        String expected = "Bearer";

        AuthResponse response = service.auth(given);
        String output = response.getToken();

        assertEquals(expected, output);
    }

    @Test
    void shouldRaiseInvalidCredentialsExceptionWithBadCredentialsRequest() {
        AuthRequest given = new AuthRequest("MockUser", "BadPassword");
        Class<InvalidCredentialsException> expectedType = InvalidCredentialsException.class;
        String expectedMessage = "credenciales inválidas";

        InvalidCredentialsException err = assertThrows(expectedType, () -> service.auth(given));
        String output = err.getMessage();

        assertTrue(output.contains(expectedMessage));
    }
}
