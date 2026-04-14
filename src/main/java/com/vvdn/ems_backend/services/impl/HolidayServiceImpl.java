package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.HolidayRequestDto;
import com.vvdn.ems_backend.dtos.HolidayResponseDto;
import com.vvdn.ems_backend.entity.HolidayCalendar;
import com.vvdn.ems_backend.repository.HolidayRepository;
import com.vvdn.ems_backend.services.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository repository;

    @Override
    public HolidayResponseDto addHoliday(HolidayRequestDto request) {

        HolidayCalendar holiday = HolidayCalendar.builder()
                .holidayName(request.getHolidayName())
                .holidayDate(request.getHolidayDate())
                .holidayType(request.getHolidayType())
                .calendarYear(request.getCalendarYear())
                .isActive(true)
                .createdBy(request.getCreatedBy())
                .createdOn(Instant.now())
                .build();

        repository.save(holiday);

        return HolidayResponseDto.builder()
                .message("Holiday added successfully")
                .build();
    }

    @Override
    public HolidayResponseDto updateHoliday(UUID id, HolidayRequestDto request) {

        HolidayCalendar holiday = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));

        if (request.getHolidayName() != null)
            holiday.setHolidayName(request.getHolidayName());

        if (request.getHolidayType() != null)
            holiday.setHolidayType(request.getHolidayType());

        if (request.getHolidayName() != null)
            holiday.setHolidayName(request.getHolidayName());

        repository.save(holiday);

        return HolidayResponseDto.builder()
                .message("Holiday updated successfully")
                .build();
    }

    @Override
    public HolidayResponseDto deactivateHoliday(UUID id) {

        HolidayCalendar holiday = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));

        holiday.setIsActive(false);
        holiday.setUpdatedOn(Instant.now());

        repository.save(holiday);

        return HolidayResponseDto.builder()
                .message("Holiday deactivated successfully")
                .build();
    }

    @Override
    public HolidayCalendar getHolidayById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));
    }

    @Override
    public List<HolidayCalendar> getAllHolidays() {
        short currentYear = (short) java.time.Year.now().getValue();

        return repository.findByIsActiveTrueAndCalendarYear(currentYear);

    }
}
