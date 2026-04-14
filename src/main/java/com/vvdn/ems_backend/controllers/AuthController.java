package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.LoginRequest;
import com.vvdn.ems_backend.dtos.LoginResponse;
import com.vvdn.ems_backend.dtos.RefreshTokenRequest;
import com.vvdn.ems_backend.entity.User;
import com.vvdn.ems_backend.repository.UserRepository;
import com.vvdn.ems_backend.security.JwtUtil;
import com.vvdn.ems_backend.services.impl.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final AuthService authService;

    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AuthService authService, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.userRepository = userRepository;
        logger.info("AuthController initialized");
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        logger.info("Login attempt for user: {}", request == null ? "null" : request.getUsername());

        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            logger.warn("Login rejected: missing userName or password in request body");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username and password are required");
        }

        try {
            logger.debug("Authenticating user: {}", request.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            if (!authentication.isAuthenticated()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }
            logger.info("Login successful for user: {}", request.getUsername());


            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

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


//            logger.warn("Authentication returned unauthenticated state for user: {}", request.getUsername());
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        } catch (AuthenticationException exception) {
            logger.warn("Authentication failed for user '{}': {}", request.getUsername(), exception.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials", exception);
        }

//        try {
//            return authService.login(
//                    request.getUsername(),
//                    request.getPassword()
//            );
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
    }
    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody (required = false)RefreshTokenRequest request) {
        return authService.refreshToken(request.getRefreshToken());
    }


    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token missing");
        }

        String token = authHeader.substring(7);

        authService.logout(token);

        logger.info("User logged out successfully");

        return "Logout successful";
    }
}

