package gio.hobist.Service;

import gio.hobist.Entity.Token;
import gio.hobist.Repository.PasswordResetTokenRepository;
import gio.hobist.Repository.UserRepository;
import gio.hobist.utils.PasswordHasher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final MailService mailService;

    private final PasswordHasher passwordHasher = new PasswordHasher();


    public PasswordResetService(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.tokenRepository = passwordResetTokenRepository;
        this.mailService = mailService;
    }

    public void requestReset(String email, String appBaseUrl) {
        var user = userRepository.findByEmail(email);
        if (user == null) {
            return;
        }

        String rawToken = UUID.randomUUID() + "-" + UUID.randomUUID();
        String tokenHash = sha256Base64(rawToken);

        Token t = new Token();
        t.setUser(user);
        t.setTokenHash(tokenHash);
        t.setExpiresAt(Instant.now().plus(Duration.ofMinutes(30)));
        t.setUsed(false);

        tokenRepository.save(t);

        String link = appBaseUrl + "/reset-password?token=" + rawToken;
        mailService.sendMail(user.getEmail(), link);
    }

    public boolean isTokenValid(String token) {
        String hash = sha256Base64(token);
        var opt = tokenRepository.findTopByTokenHashOrderByExpiresAtDesc(hash);
        if (opt.isEmpty()) return false;

        Token t = opt.get();
        return !t.isUsed() && t.getExpiresAt().isAfter(Instant.now());
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword, String confirmPassword, BindingResult binding) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            binding.rejectValue("newPassword", "password.empty", "New password is required.");
            return;
        }
        if (newPassword.length() < 8) {
            binding.rejectValue("newPassword", "password.weak", "Password must be at least 8 characters.");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            binding.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match.");
            return;
        }

        String hash = sha256Base64(rawToken);
        var opt = tokenRepository.findTopByTokenHashOrderByExpiresAtDesc(hash);
        if (opt.isEmpty()) {
            binding.reject("token.invalid", "Invalid reset token.");
            return;
        }
        Token t = opt.get();
        if (t.isUsed() || t.getExpiresAt().isBefore(Instant.now())) {
            binding.reject("token.expired", "Reset token has expired or was already used.");
            return;
        }

        var user = t.getUser();
        user.setPassword(passwordHasher.hashPassword(newPassword));

        userRepository.save(user);

        t.setUsed(true);
        tokenRepository.save(t);

    }

    private String sha256Base64(String rawToken) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
