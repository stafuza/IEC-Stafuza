package br.com.fatecads.fatecads.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fatecads.fatecads.entity.PasswordResetToken;
import br.com.fatecads.fatecads.entity.Usuario;
import br.com.fatecads.fatecads.repository.PasswordResetTokenRepository;
import br.com.fatecads.fatecads.repository.UsuarioRepository;

@Service
public class PasswordResetService {
    private static final Duration TOKEN_TTL = Duration.ofMinutes(60);

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    private final SecureRandom secureRandom = new SecureRandom();

    public Optional<PasswordResetToken> createTokenForEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmailUsuario(email).orElse(null);
        if (usuario == null) {
            return Optional.empty();
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setUsuario(usuario);
        token.setToken(generateToken());
        token.setExpiresAt(LocalDateTime.now().plus(TOKEN_TTL));
        token.setUsed(false);

        return Optional.of(tokenRepository.save(token));
    }

    public Optional<PasswordResetToken> findValidToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(found -> !found.isUsed())
                .filter(found -> found.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> found = findValidToken(token);
        if (found.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = found.get();
        usuarioService.updatePassword(resetToken.getUsuario(), newPassword);
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
        return true;
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
