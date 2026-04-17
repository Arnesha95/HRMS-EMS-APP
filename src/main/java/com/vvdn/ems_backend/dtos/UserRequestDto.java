package com.vvdn.ems_backend.dtos;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private String username;
    private String password;
    private String role;
   // private UUID empId;
    private boolean isActive;

}
