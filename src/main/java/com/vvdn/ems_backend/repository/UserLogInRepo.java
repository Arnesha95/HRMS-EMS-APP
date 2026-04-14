package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserLogInRepo extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);
}
