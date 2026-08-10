package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.entity.OffboardingStatus;


import java.util.List;
import java.util.UUID;

public interface EmployeeOffboardingService {

    ResignationResponseDto takeAction(UUID offboardingId, HrActionDto dto);

    List<ResignationResponseDto> getResignations(OffboardingStatus status);

    ResignationResponseDto applyResignation(ResignationRequestDto dto);

    TerminationResponseDto initiateTermination(TerminationRequestDto dto);

    List<TerminationResponseDto> getTerminations(OffboardingStatus status);

    TerminationResponseDto takeTerminationAction(UUID offboardingId, HrActionDto dto);

}
