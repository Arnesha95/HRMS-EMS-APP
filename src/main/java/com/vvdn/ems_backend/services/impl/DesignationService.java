package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.entity.Designation;
import com.vvdn.ems_backend.repository.DesignationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DesignationService {

    private final DesignationRepository designationRepository;

    public DesignationService(DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }

    public DesignationResponseDto addDesignation(DesignationRequestDto request){

        Designation designation = Designation.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .isActive(true)
                .createdBy(request.getCreatedBy())
                .build();

        designationRepository.save(designation);

        return new DesignationResponseDto("Designation added successfully");

    }

    public List<DesignationListResponseDto> getActiveDesignations() {

        List<Designation> designations = designationRepository.findByIsActiveTrue();

        return designations.stream().map(desig -> DesignationListResponseDto.builder()
                .id(desig.getId())
                .title(desig.getTitle())
                .description(desig.getDescription())
                .isActive(desig.getIsActive())
                .createdOn(Instant.from(desig.getCreatedAt()))
                .build()
        ).collect(Collectors.toList());
    }

    public DesignationListResponseDto getDesignationByID(UUID id){

        Designation desig = designationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Designation not found"));

        return DesignationListResponseDto.builder()
                .id(desig.getId())
                .title(desig.getTitle())
                .description(desig.getDescription())
                .isActive(desig.getIsActive())
                .createdOn(Instant.from(desig.getCreatedAt()))
                .build();
    }


    public DesignationResponseDto updateDesignation(UUID id, DesignationRequestDto request){
        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Designation not found"));

        if (request.getTitle() != null)
            designation.setTitle(request.getTitle());

        if (request.getDescription() != null)
            designation.setDescription(request.getDescription());

        if (request.getIsActive() != null)
            designation.setIsActive(request.getIsActive());

        if (request.getCreatedBy() != null)
            designation.setCreatedBy(request.getCreatedBy());

        if (request.getUpdatedBy() != null)
            designation.setUpdatedBy(request.getUpdatedBy());
        
        designationRepository.save(designation);

        return new DesignationResponseDto("Designation updated successfully");

    }

    public DesignationResponseDto deactivateDesignation(UUID id) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Designation not found"));

        if (!designation.getIsActive()) {
            throw new RuntimeException("Designation already deactivated");
        }

        designation.setIsActive(false);
        designationRepository.save(designation);

        return new DesignationResponseDto("Designation deactivated successfully");
    }


    public DesignationResponseDto deleteDesignation(UUID id) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Designation not found"));

        designationRepository.delete(designation);

        return new DesignationResponseDto("Designation deleted successfully");
    }

}
