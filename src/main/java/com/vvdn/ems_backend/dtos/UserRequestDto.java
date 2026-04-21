package com.vvdn.ems_backend.dtos;


import lombok.*;

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
