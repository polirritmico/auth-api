/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import cl.duoc.auth.dto.request.AuthRequest;
import cl.duoc.auth.dto.request.NewCredentialsRequest;
import cl.duoc.auth.dto.response.AuthResponse;
import cl.duoc.auth.exception.InvalidCredentialsException;
import cl.duoc.auth.exception.NonCompliantCredentialsException;
import cl.duoc.auth.model.Credentials;
import cl.duoc.auth.repository.CredentialsRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @BeforeEach
    void setUpSecurityContext() {
        String authRole = "admin";
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        GrantedAuthority authority = new SimpleGrantedAuthority(authRole);

        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        Mockito.doReturn(List.of(authority)).when(auth).getAuthorities();
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldRaiseNonCompliantCredentialsWhenCreatingShortPassword() {
        String caseName = "TestUser";
        String caseRole = "admin";
        String casePassword = "a0!";
        Class<NonCompliantCredentialsException> expectedType = NonCompliantCredentialsException.class;
        String expectedMessage = "al menos 6 caracteres";
        NewCredentialsRequest given = new NewCredentialsRequest(caseName, caseRole, casePassword);

        NonCompliantCredentialsException err = assertThrows(expectedType, () -> service.createCredentials(given));
        String output = err.getMessage();

        assertTrue(output.contains(expectedMessage));
    }

    @Test
    void shouldRaiseNonCompliantCredentialsWhenCreatingPwdWithoutNumbers() {
        String caseName = "TestUser";
        String caseRole = "admin";
        String casePassword = "OnlyLettersPassword";
        Class<NonCompliantCredentialsException> expectedType = NonCompliantCredentialsException.class;
        String expectedMessage = "debe contener al menos un número";
        NewCredentialsRequest given = new NewCredentialsRequest(caseName, caseRole, casePassword);

        NonCompliantCredentialsException err = assertThrows(expectedType, () -> service.createCredentials(given));
        String output = err.getMessage();

        assertTrue(output.contains(expectedMessage));
    }

    @Test
    void shouldGenerateDifferentTokenAtDifferentTimes() {
        String caseName = "TestUser";
        String casePassword = "Password";
        AuthRequest given = new AuthRequest(caseName, casePassword);

        when(repo.findByUserId(caseName))
                .thenReturn(Optional.of(Credentials.builder()
                        .id(1L)
                        .userId(caseName)
                        .password(encoder.encode(casePassword))
                        .role("mockRole")
                        .createdAt(LocalDateTime.now())
                        .build()));

        AuthResponse firstResponse = service.auth(given);

        // Modify the time through mockito to avoid delays
        AuthResponse secondResponse;
        Instant futureTime = Instant.now().plusSeconds(2);
        try (MockedStatic<Instant> mockedInstant = mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS)) {
            mockedInstant.when(Instant::now).thenReturn(futureTime);
            secondResponse = service.auth(given);
        }

        String firstTokenResponse = firstResponse.getToken();
        String secondTokenResponse = secondResponse.getToken();

        String firstOutput = JWT.decode(firstTokenResponse).getToken();
        String secondOutput = JWT.decode(secondTokenResponse).getToken();
        assertNotEquals(firstOutput, secondOutput);
    }

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
