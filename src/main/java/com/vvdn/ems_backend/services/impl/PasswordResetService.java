package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.entity.PasswordResetToken;
import com.vvdn.ems_backend.entity.User;
import com.vvdn.ems_backend.repository.PasswordResetTokenRepository;
import com.vvdn.ems_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
   // private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void requestReset(String email) {

        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(15 * 60)) // 15 mins
                .build();

        tokenRepository.deleteByUser(user);
        tokenRepository.save(resetToken);

      //  emailService.sendResetEmail(user.getUsername(), token);
    }

    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setIsFirstLogin(false);

        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }
}
