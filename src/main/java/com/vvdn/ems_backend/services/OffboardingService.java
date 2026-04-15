package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.OffboardingRequestDto;
import com.vvdn.ems_backend.dtos.OffboardingResponseDto;
import com.vvdn.ems_backend.dtos.ResignationActionDto;
import com.vvdn.ems_backend.dtos.ResignationRequestDto;

public interface OffboardingService {

    // Employee
    OffboardingResponseDto submitResignation(ResignationRequestDto dto);

    // HR
    OffboardingResponseDto handleResignation(ResignationActionDto dto);

    // HR Offboarding
    OffboardingResponseDto offboardEmployee(OffboardingRequestDto dto);
}
