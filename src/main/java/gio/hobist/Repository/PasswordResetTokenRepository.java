package gio.hobist.Repository;

import gio.hobist.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findTopByTokenHashOrderByExpiresAtDesc(String tokenHash);
}
