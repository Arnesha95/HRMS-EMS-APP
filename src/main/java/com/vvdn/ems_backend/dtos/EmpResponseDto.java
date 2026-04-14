package com.vvdn.ems_backend.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmpResponseDto {

    private String message;
    private String username;
    private String password;
}
