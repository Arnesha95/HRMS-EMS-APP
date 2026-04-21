package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.entity.*;
import com.vvdn.ems_backend.repository.EmpRepository;
import com.vvdn.ems_backend.repository.EmployeeOffboardingRepository;
import com.vvdn.ems_backend.repository.UserRepository;
import com.vvdn.ems_backend.services.EmployeeOffboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.vvdn.ems_backend.entity.OffboardingType.RESIGNATION;


@Service
@RequiredArgsConstructor
public class EmployeeOffboardingServiceImpl implements EmployeeOffboardingService {

    private final EmployeeOffboardingRepository offboardingRepo;
    private final EmpRepository employeeRepo;
    private final UserRepository userRepo;

    @Override
    public ResignationResponseDto applyResignation(ResignationRequestDto dto) {


        if (dto.getResignationDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Resignation date cannot be in future");
        }

        if (dto.getProposedLastWorkingDate().isBefore(dto.getResignationDate())) {
            throw new RuntimeException("Last working date cannot be before resignation date");
        }

        if (dto.getProposedLastWorkingDate().isBefore(dto.getResignationDate().plusDays(30))) {
            throw new RuntimeException("Notice period must be at least 30 days");
        }


        boolean exists = offboardingRepo.existsByEmployeeEmpIdAndOffboardingStatus(
                dto.getEmpId(),
                OffboardingStatus.PENDING
        );

        if (exists) {
            throw new RuntimeException("Resignation already pending");
        }

        Employee emp = employeeRepo.findById(dto.getEmpId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));


        EmployeeOffboarding entity = EmployeeOffboarding.builder()
                .employee(emp)
                .offboardingType(RESIGNATION)
                .resignationDate(dto.getResignationDate())
                .proposedLastWorkingDate(dto.getProposedLastWorkingDate())
                .reason(dto.getReason())
                .offboardingStatus(OffboardingStatus.PENDING)
                .build();

        offboardingRepo.save(entity);


        return mapToDto(entity, "Resignation submitted successfully");
    }


    @Override
    public List<ResignationResponseDto> getResignations(OffboardingStatus status) {

        List<EmployeeOffboarding> list;

        if (status == null) {
            list = offboardingRepo.findByOffboardingType(OffboardingType.RESIGNATION);
        } else {
            list = offboardingRepo.findByOffboardingStatusAndOffboardingType(
                    status,
                    OffboardingType.RESIGNATION
            );
        }

        return list.stream()
                .map(e -> mapToDto(e, "Requests fetched successfully"))
                .toList();
    }


    @Transactional
    @Override
    public ResignationResponseDto takeAction(UUID offboardingId, HrResignationActionDto dto, UUID hrId) {

        EmployeeOffboarding entity = offboardingRepo.findById(offboardingId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (entity.getOffboardingStatus() != OffboardingStatus.PENDING) {
            throw new RuntimeException("Already processed");
        }

        if (dto.getStatus() == OffboardingStatus.APPROVED) {

            if (dto.getFinalLastWorkingDate() == null) {
                throw new RuntimeException("Final last working date is required for approval");
            }

            if (dto.getFinalLastWorkingDate().isBefore(entity.getResignationDate())) {
                throw new RuntimeException("Invalid last working date");
            }
        }


        entity.setOffboardingStatus(dto.getStatus());
        entity.setFeedback(dto.getFeedback());
        entity.setIsGoodToRehire(dto.getIsGoodToRehire());
        entity.setActionBy(hrId);
        entity.setActionOn(Instant.now());



        if (dto.getStatus() == OffboardingStatus.APPROVED) {

            entity.setFinalLastWorkingDate(dto.getFinalLastWorkingDate());
            entity.setIsClearanceDone(false);

        }

        offboardingRepo.save(entity);

        return mapToDto(entity, "Action completed successfully");
    }


    @Transactional
    @Override
    public TerminationResponseDto initiateTermination(TerminationRequestDto dto, UUID hrId) {

        Employee emp = employeeRepo.findById(dto.getEmpId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        boolean alreadyTerminated = offboardingRepo.existsByEmployeeEmpIdAndOffboardingType(
                dto.getEmpId(),
                OffboardingType.TERMINATION
        );

        if (alreadyTerminated) {
            throw new RuntimeException("Employee already terminated");
        }

        EmployeeOffboarding entity = EmployeeOffboarding.builder()
                .employee(emp)
                .offboardingType(OffboardingType.TERMINATION)
                .resignationDate(dto.getTerminationDate())
                .finalLastWorkingDate(dto.getTerminationDate())
                .reason(dto.getReason())
                .feedback(dto.getFeedback())
                .isGoodToRehire(dto.getIsGoodToRehire())
                .offboardingStatus(OffboardingStatus.APPROVED)
                .actionBy(hrId)
                .actionOn(Instant.now())
                .isClearanceDone(false)
                .build();

        offboardingRepo.save(entity);

        return mapToTerminationDto(entity, "Termination processed successfully");
    }


    @Override
    public List<TerminationResponseDto> getTerminations(OffboardingStatus status) {
        List<EmployeeOffboarding> list;

        if (status == null) {
            list = offboardingRepo.findByOffboardingType(OffboardingType.TERMINATION);
        } else {
            list = offboardingRepo.findByOffboardingStatusAndOffboardingType(
                    status,
                    OffboardingType.TERMINATION
            );
        }

        return list.stream()
                .map(e -> mapToTerminationDto(e, "Fetched successfully"))
                .toList();
    }




    private ResignationResponseDto mapToDto(EmployeeOffboarding entity, String message) {

        return ResignationResponseDto.builder()
                .offboardingId(entity.getOffboardingId())
                .empId(entity.getEmployee().getEmpId())
                .employeeName(entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName())
                .resignationDate(entity.getResignationDate())
                .proposedLastWorkingDate(entity.getProposedLastWorkingDate())
                .finalLastWorkingDate(entity.getFinalLastWorkingDate())
                .status(entity.getOffboardingStatus().name())
                .build();
    }


    private TerminationResponseDto mapToTerminationDto(EmployeeOffboarding entity, String message) {

        return TerminationResponseDto.builder()
                .offboardingId(entity.getOffboardingId())
                .empId(entity.getEmployee().getEmpId())
                .employeeName(entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName())
                .terminationDate(entity.getFinalLastWorkingDate())
                .reason(entity.getReason())
                .status(entity.getOffboardingStatus().name())
                .isGoodToRehire(entity.getIsGoodToRehire())
                .build();
    }

}
