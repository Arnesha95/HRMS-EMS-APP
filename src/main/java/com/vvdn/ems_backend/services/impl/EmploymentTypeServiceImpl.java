package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.EmploymentTypeRequestDto;
import com.vvdn.ems_backend.dtos.EmploymentTypeResponseDto;
import com.vvdn.ems_backend.entity.EmploymentType;
import com.vvdn.ems_backend.repository.EmploymentTypeRepository;
import com.vvdn.ems_backend.services.EmploymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmploymentTypeServiceImpl implements EmploymentTypeService {

    private final EmploymentTypeRepository repository;

    @Override
    public EmploymentTypeResponseDto createEmploymentType(EmploymentTypeRequestDto request) {

        EmploymentType employmentType = EmploymentType.builder()
                .name(request.getName())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .createdBy(request.getCreatedBy())
                .updatedBy(request.getUpdatedBy())
                .build();

        repository.save(employmentType);

        return EmploymentTypeResponseDto.builder()
                .message("Employment Type created successfully")
                .build();
    }

    @Override
    public EmploymentTypeResponseDto updateEmploymentType(UUID id, EmploymentTypeRequestDto request) {

        EmploymentType employmentType = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment Type not found"));

        employmentType.setName(request.getName());
        employmentType.setIsActive(request.getIsActive());
        employmentType.setUpdatedBy(request.getUpdatedBy());

        repository.save(employmentType);

        return EmploymentTypeResponseDto.builder()
                .message("Employment Type updated successfully")
                .build();
    }

    @Override
    public EmploymentTypeResponseDto patchEmploymentType(UUID id, EmploymentTypeRequestDto request) {

        EmploymentType employmentType = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment Type not found"));

        if (request.getName() != null) {
            employmentType.setName(request.getName());
        }

        if (request.getIsActive() != null) {
            employmentType.setIsActive(request.getIsActive());
        }

        if (request.getUpdatedBy() != null) {
            employmentType.setUpdatedBy(request.getUpdatedBy());
        }

        repository.save(employmentType);

        return EmploymentTypeResponseDto.builder()
                .message("Employment Type patched successfully")
                .build();
    }

    @Override
    public EmploymentTypeResponseDto deleteEmploymentType(UUID id) {

        EmploymentType employmentType = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment Type not found"));

        repository.delete(employmentType);

        return EmploymentTypeResponseDto.builder()
                .message("Employment Type deleted successfully")
                .build();
    }

    @Override
    public List<EmploymentType> getAllEmploymentTypes() {
        return repository.findAll();
    }

    @Override
    public EmploymentType getEmploymentTypeById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment Type not found"));
    }
}
