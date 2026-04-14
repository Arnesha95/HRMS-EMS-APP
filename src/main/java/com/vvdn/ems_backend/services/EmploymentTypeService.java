package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.EmploymentTypeRequestDto;
import com.vvdn.ems_backend.dtos.EmploymentTypeResponseDto;
import com.vvdn.ems_backend.entity.EmploymentType;

import java.util.List;
import java.util.UUID;

public interface EmploymentTypeService {

    EmploymentTypeResponseDto createEmploymentType(EmploymentTypeRequestDto request);

    EmploymentTypeResponseDto updateEmploymentType(UUID id, EmploymentTypeRequestDto request);

    EmploymentTypeResponseDto patchEmploymentType(UUID id, EmploymentTypeRequestDto request);

    EmploymentTypeResponseDto deleteEmploymentType(UUID id);

    List<EmploymentType> getAllEmploymentTypes();

    EmploymentType getEmploymentTypeById(UUID id);
}
