package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.entity.*;
import com.vvdn.ems_backend.exception.BadRequestException;
import com.vvdn.ems_backend.repository.EmpRepository;
import com.vvdn.ems_backend.repository.EmployeeOffboardingRepository;
import com.vvdn.ems_backend.services.EmployeeOffboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    //private final UserRepository userRepo;

    private UUID getLoggedInUserId() {

        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("User not authenticated");
        }

        String email = authentication.getName();

        return employeeRepo.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"))
                .getEmpId();
    }

    @Override
    public ResignationResponseDto applyResignation(ResignationRequestDto dto) {

        UUID empId = getLoggedInUserId();

        Employee emp = employeeRepo.findById(empId)
                .orElseThrow(() -> new BadRequestException("Employee not found"));


        LocalDate today = LocalDate.now();
        //LocalDate resignationDate = today;

        if (dto.getProposedLastWorkingDate() == null) {
            throw new BadRequestException("Proposed last working date is required");
        }

        if (dto.getProposedLastWorkingDate().isBefore(today)) {
            throw new BadRequestException("Last working date cannot be in the past");
        }

        if (dto.getProposedLastWorkingDate().isBefore(today.plusDays(30))) {
            throw new BadRequestException("Notice period must be at least 30 days");
        }


//        if (dto.getResignationDate().isAfter(LocalDate.now())) {
//            throw new RuntimeException("Resignation date cannot be in future");
//        }
//
//        if (dto.getProposedLastWorkingDate().isBefore(dto.getResignationDate())) {
//            throw new RuntimeException("Last working date cannot be before resignation date");
//        }
//
//        if (dto.getProposedLastWorkingDate().isBefore(dto.getResignationDate().plusDays(30))) {
//            throw new RuntimeException("Notice period must be at least 30 days");
//        }


        boolean exists = offboardingRepo.existsByEmployeeEmpIdAndOffboardingStatus(
                empId,
                OffboardingStatus.PENDING
        );

        if (exists) {
            throw new BadRequestException("Resignation already pending");
        }


        EmployeeOffboarding entity = EmployeeOffboarding.builder()
                .employee(emp)
                .offboardingType(RESIGNATION)
                .resignationDate(today)
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
    public ResignationResponseDto takeAction(UUID offboardingId, HrActionDto dto) {


        UUID hrId = getLoggedInUserId();


        EmployeeOffboarding entity = offboardingRepo.findById(offboardingId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (entity.getOffboardingStatus() != OffboardingStatus.PENDING) {
            throw new BadRequestException("Already processed");
        }

        if (dto.getStatus() == OffboardingStatus.APPROVED) {

            if (dto.getFinalLastWorkingDate() == null) {
                throw new BadRequestException("Final last working date is required for approval");
            }

            LocalDate today = LocalDate.now();

            if (dto.getFinalLastWorkingDate().isBefore(today)) {
                throw new BadRequestException("Final last working date cannot be in the past");
            }


            if (dto.getFinalLastWorkingDate().isBefore(entity.getResignationDate())) {
                throw new BadRequestException("Invalid last working date");
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
    public TerminationResponseDto initiateTermination(TerminationRequestDto dto) {

        UUID hrId = getLoggedInUserId();

        Employee emp = employeeRepo.findById(dto.getEmpId())
                .orElseThrow(() -> new BadRequestException("Employee not found"));


        boolean alreadyTerminated = offboardingRepo
                .existsByEmployeeEmpIdAndOffboardingTypeAndOffboardingStatusIn(
                        dto.getEmpId(),
                        OffboardingType.TERMINATION,
                        List.of(OffboardingStatus.PENDING, OffboardingStatus.APPROVED)
                );

        if (dto.getTerminationDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Termination date cannot be in the past");
        }

        if (dto.getIsGoodToRehire() != null) {
            throw new BadRequestException("Rehire eligibility cannot be set during termination");
        }

        if (alreadyTerminated) {
            throw new BadRequestException("Employee already terminated");
        }

        EmployeeOffboarding entity = EmployeeOffboarding.builder()
                .employee(emp)
                .offboardingType(OffboardingType.TERMINATION)
                .resignationDate(dto.getTerminationDate()) // rename later ideally
                .proposedLastWorkingDate(dto.getTerminationDate())
                .reason(dto.getReason())
                .feedback(dto.getFeedback())
                .isGoodToRehire(false)
                .offboardingStatus(OffboardingStatus.PENDING)
                .isClearanceDone(false)
                .actionBy(hrId)
                .actionOn(Instant.now())
                .build();

        offboardingRepo.save(entity);

        return mapToTerminationDto(entity, "Termination processed successfully");
    }


    @Transactional
    @Override
    public TerminationResponseDto takeTerminationAction(UUID offboardingId, HrActionDto dto) {

        UUID hrId = getLoggedInUserId();

        EmployeeOffboarding entity = offboardingRepo.findById(offboardingId)
                .orElseThrow(() -> new BadRequestException("Request not found"));


        if (entity.getOffboardingStatus() != OffboardingStatus.PENDING) {
            throw new BadRequestException("Already processed");
        }

        if (dto.getStatus() == OffboardingStatus.APPROVED) {

            if (dto.getFinalLastWorkingDate() == null) {
                throw new BadRequestException("Final last working date is required");
            }

            if (dto.getFinalLastWorkingDate().isBefore(LocalDate.now())) {
                throw new BadRequestException("Final last working date cannot be in the past");
            }
        }

        if (dto.getIsGoodToRehire() != null) {
            throw new BadRequestException("Rehire eligibility cannot be set for termination");
        }

        entity.setOffboardingStatus(dto.getStatus());
        entity.setFeedback(dto.getFeedback());
        entity.setIsGoodToRehire(false);
        entity.setActionBy(hrId);
        entity.setActionOn(Instant.now());

        if (dto.getStatus() == OffboardingStatus.APPROVED) {
            entity.setFinalLastWorkingDate(dto.getFinalLastWorkingDate());
            entity.setIsClearanceDone(false);
        }

        offboardingRepo.save(entity);

        return mapToTerminationDto(entity, "Termination action completed");
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
