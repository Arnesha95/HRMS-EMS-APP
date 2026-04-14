package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.HolidayRequestDto;
import com.vvdn.ems_backend.dtos.HolidayResponseDto;
import com.vvdn.ems_backend.entity.HolidayCalendar;

import java.util.List;
import java.util.UUID;

public interface HolidayService {

    HolidayResponseDto addHoliday(HolidayRequestDto request);

    HolidayResponseDto updateHoliday(UUID id, HolidayRequestDto request);

    HolidayResponseDto deactivateHoliday(UUID id);

    HolidayCalendar getHolidayById(UUID id);

    List<HolidayCalendar> getAllHolidays();

}
