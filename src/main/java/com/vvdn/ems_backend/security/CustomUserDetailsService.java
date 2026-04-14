package com.vvdn.ems_backend.security;

import com.vvdn.ems_backend.entity.Employee;
import com.vvdn.ems_backend.entity.User;
import com.vvdn.ems_backend.repository.EmpRepository;
import com.vvdn.ems_backend.repository.UserLogInRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserLogInRepo userLogInRepo;

    public CustomUserDetailsService(UserLogInRepo userLogInRepo) {
        this.userLogInRepo = userLogInRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading user by username: {}", username);


        return userLogInRepo.findByUsername(username)
                .map(user -> {
                    logger.info("User found: {}, role: {}", username, user.getAuthorities());
                    return (UserDetails) user;
                })

                .orElseThrow(() -> {
                    logger.warn("User not found in database: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
    }
}

