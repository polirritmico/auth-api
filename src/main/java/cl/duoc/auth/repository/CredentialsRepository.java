/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.repository;

import cl.duoc.auth.model.Credentials;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    boolean existsByUserId(String userId);

    Optional<Credentials> findByUserIdAndRevokedAtIsNull(String userId);

    List<Credentials> findByRevokedAtIsNull();

    default List<Credentials> findAll() {
        return findByRevokedAtIsNull();
    }

    default Optional<Credentials> findByUserId(String userId) {
        return findByUserIdAndRevokedAtIsNull(userId);
    }
}
