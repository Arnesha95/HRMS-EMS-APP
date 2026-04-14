package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "holiday_calendar")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolidayCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "holiday_id", updatable = false, nullable = false)
    private UUID holidayId;

    @Column(name = "holiday_name")
    private String holidayName;

    @Column(name = "holiday_date")
    private LocalDate holidayDate;

    @Column(name = "holiday_type")
    private String holidayType;

    @Column(name = "calendar_year")
    private Short calendarYear;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (createdOn == null) createdOn = now;
        updatedOn = now;

        if (calendarYear == null && holidayDate != null) {
            calendarYear = (short) holidayDate.getYear();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = Instant.now();

        if (holidayDate != null) {
            calendarYear = (short) holidayDate.getYear();
        }
    }
}
