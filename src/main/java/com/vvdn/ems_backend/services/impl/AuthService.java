package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.LoginResponse;
import com.vvdn.ems_backend.entity.User;
import com.vvdn.ems_backend.repository.UserLogInRepo;
import com.vvdn.ems_backend.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserLogInRepo userLogInRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;


    public LoginResponse login(String username, String password) throws Exception {
        logger.info("Login attempt for username: {}", username);

        Optional<User> userOpt = userLogInRepo.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new Exception("Invalid username or password");
        }

        User user = userOpt.get();

        logger.info("User found: {}", user.getUsername());
        logger.info("Stored hashed password: {}", user.getPassword());

        boolean isMatch = passwordEncoder.matches(password, user.getPassword());

        logger.info("Password match result: {}", isMatch);

        if (!isMatch) {
            logger.warn("Password did not match for user: {}", username);
            throw new Exception("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid username or password");
        }

//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            logger.warn("Invalid password for user: {}", username);
//            throw new Exception("Invalid username or password");
//        }

        String accessToken = jwtUtil.generateAccessToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );

        String refreshToken = jwtUtil.generateRefreshToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );

        return new LoginResponse(
                accessToken,
                refreshToken,
                "Login successful",
                user.getRole().name(),
                user.getUsername()
        );
    }

    public LoginResponse refreshToken(String refreshToken) {

        if (!jwtUtil.validateToken(refreshToken, "refresh")) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String userId = jwtUtil.extractUserId(refreshToken);
        String role = jwtUtil.extractRole(refreshToken);

        String newAccessToken = jwtUtil.generateAccessToken(
                UUID.fromString(userId),
                username,
                role
        );

        String newRefreshToken = jwtUtil.generateRefreshToken(
                UUID.fromString(userId),
                username,
                role
        );

        return new LoginResponse(
                newAccessToken,
                newRefreshToken,
                "Token refreshed",
                role,
                username
        );
    }


    public void logout(String token){
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }

        if (tokenBlacklistService.isBlacklisted(token)) {
            logger.warn("Logout attempted with already-blacklisted token");
        }

        tokenBlacklistService.blacklistToken(token);
        logger.info("Token successfully blacklisted on logout");
    }

}
