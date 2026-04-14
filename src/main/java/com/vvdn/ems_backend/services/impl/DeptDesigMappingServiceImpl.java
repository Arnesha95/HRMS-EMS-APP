package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.DeptDesigMappingRequestDto;
import com.vvdn.ems_backend.dtos.DeptDesigMappingResponseDto;
import com.vvdn.ems_backend.entity.DeptDesigMapping;
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

    @Override
    @Transactional
    public DeptDesigMappingResponseDto addMappings(DeptDesigMappingRequestDto requestDto) {

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

        return DeptDesigMappingResponseDto.builder()
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
    public DeptDesigMappingResponseDto deleteMapping(UUID deptId, UUID desigId) {

        List<DeptDesigMapping> mappings =
                repository.findByDeptId(deptId)
                        .stream()
                        .filter(m -> m.getDesigId().equals(desigId))
                        .collect(Collectors.toList());

        repository.deleteAll(mappings);

        return DeptDesigMappingResponseDto.builder()
                .message("mapping deleted successfully")
                .build();
    }

}

