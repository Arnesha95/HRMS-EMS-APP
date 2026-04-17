package com.vvdn.ems_backend.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private UUID userId;
    private String username;
    private String role;
   // private UUID empId;
    private boolean isActive;

}
