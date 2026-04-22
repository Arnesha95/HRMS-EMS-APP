package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.DeptDesigMappingRequestDto;
import com.vvdn.ems_backend.dtos.DeptWiseDesigResponseDto;
import com.vvdn.ems_backend.dtos.DesignationDto;
import com.vvdn.ems_backend.entity.DeptDesigMapping;
import com.vvdn.ems_backend.repository.DesignationRepository;
import com.vvdn.ems_backend.repository.MappingRepository;
import com.vvdn.ems_backend.services.DeptDesigMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeptDesigMappingServiceImpl implements DeptDesigMappingService {

    private final MappingRepository repository;
    private final DesignationRepository designationRepository;

    @Override
    @Transactional
    public DeptWiseDesigResponseDto addMappings(DeptDesigMappingRequestDto requestDto) {

        UUID deptId = requestDto.getDeptId();
        UUID userId = requestDto.getUserId();

        List<DeptDesigMapping> mappings = requestDto.getDesigIds()
                .stream()
                .filter(desigId -> !repository.existsByDeptIdAndDesigId(deptId, desigId))
                .map(desigId -> DeptDesigMapping.builder()
                        .deptId(deptId)
                        .desigId(desigId)
                        .createdAt(Instant.now())
                        .createdBy(userId)
                        .build())
                .collect(Collectors.toList());

        repository.saveAll(mappings);

        return DeptWiseDesigResponseDto.builder()
                .message("mapping is done successfully")
                .build();
    }

    @Override
    public List<UUID> getDesignationsByDept(UUID deptId) {
        return repository.findByDeptId(deptId)
                .stream()
                .map(DeptDesigMapping::getDesigId)
                .collect(Collectors.toList());
    }

    @Override
    public DeptWiseDesigResponseDto deleteMapping(UUID deptId, UUID desigId) {

        List<DeptDesigMapping> mappings =
                repository.findByDeptId(deptId)
                        .stream()
                        .filter(m -> m.getDesigId().equals(desigId))
                        .collect(Collectors.toList());

        repository.deleteAll(mappings);

        return DeptWiseDesigResponseDto.builder()
                .message("mapping deleted successfully")
                .build();
    }

    @Override
    public List<DesignationDto> getDesignationDetailsByDept(UUID deptId) {

        List<UUID> desigIds = repository.findByDeptId(deptId)
                .stream()
                .map(DeptDesigMapping::getDesigId)
                .toList();

        return designationRepository.findAllById(desigIds)
                .stream()
                .map(d -> DesignationDto.builder()
                        .id(d.getId())          // ✅ fixed
                        .name(d.getTitle())     // ✅ fixed
                        .build())
                .toList();
    }

}

