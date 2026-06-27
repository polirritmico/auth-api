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
import static org.mockito.Mockito.when;

import cl.duoc.auth.dto.request.AuthRequest;
import cl.duoc.auth.dto.response.AuthResponse;
import cl.duoc.auth.exception.InvalidCredentialsException;
import cl.duoc.auth.model.Credentials;
import cl.duoc.auth.repository.CredentialsRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {

    @Autowired
    private AuthService service;

    @MockitoBean
    private CredentialsRepository repo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    void shouldGenerateTokenWithValidCredentialsRequest() {
        String caseName = "TestUser";
        String casePassword = "Password";
        AuthRequest given = new AuthRequest(caseName, casePassword);
        String expected = "TestUser";

        when(repo.findByUserId(caseName))
                .thenReturn(Optional.of(Credentials.builder()
                        .id(1L)
                        .userId(caseName)
                        .password(encoder.encode(casePassword))
                        .role("mockRole")
                        .createdAt(LocalDateTime.now())
                        .build()));

        AuthResponse response = service.auth(given);
        String token = response.getToken();

        DecodedJWT output = JWT.decode(token);

        assertEquals(expected, output.getSubject());
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
