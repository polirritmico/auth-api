/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.mapper;

import cl.duoc.auth.dto.request.NewCredentialsRequest;
import cl.duoc.auth.dto.response.CredentialsResponse;
import cl.duoc.auth.model.Credentials;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtoModelMapper {
    public Credentials credentialsFromNewCredentialRequest(NewCredentialsRequest req, String password) {
        return Credentials.builder()
                .userId(req.getUser())
                .password(password)
                .role(req.getRole())
                .build();
    }

    public CredentialsResponse toCredentialsResponse(Credentials credentials) {
        return CredentialsResponse.builder()
                .userId(credentials.getUserId())
                .role(credentials.getRole())
                .build();
    }
}
