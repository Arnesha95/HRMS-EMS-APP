package com.vvdn.ems_backend.security;


import com.vvdn.ems_backend.entity.User;
import com.vvdn.ems_backend.repository.UserLogInRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



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


        User user = userLogInRepo.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User not found in database: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        logger.info("User found: {}, role: {}", username, user.getAuthorities());



        if (!user.isActive()) {
            logger.warn("User is inactive: {}", username);
            throw new DisabledException("User account is inactive");
        }

//        if (user.getEmployee() == null) {
//            logger.warn("User has no employee mapped: {}", username);
//            throw new LockedException("User is not mapped to any employee");
//        }

        if (!user.getEmployee().getIsActive()) {
            logger.warn("Employee is inactive for user: {}", username);
            throw new DisabledException("Employee is inactive");
        }

        return user;

    }
}

