package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.HolidayCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HolidayRepository extends JpaRepository<HolidayCalendar, UUID> {

    List<HolidayCalendar> findByIsActiveTrueAndCalendarYear(Short year);

}
