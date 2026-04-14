package com.vvdn.ems_backend.dtos;

import jakarta.persistence.Column;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private UUID id;

    private String userName;

    private String role;

    private UUID empId;

    private Boolean isActive = true;

    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    private String updatedBy;

    public UserDto(UUID id, String username, String role, UUID empId) {
    }
}
