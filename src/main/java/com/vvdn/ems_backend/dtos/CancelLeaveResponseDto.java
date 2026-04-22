package com.vvdn.ems_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CancelLeaveResponseDto {
    private String message;
    private Instant cancelledOn;
}
