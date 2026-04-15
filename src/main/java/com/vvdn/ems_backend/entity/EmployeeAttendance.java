package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "employee_attendance",
        uniqueConstraints = @UniqueConstraint(columnNames = {"emp_id", "date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "emp_atten_id")
    private UUID empAttenId;

    @Column(name = "emp_id", nullable = false)
    private UUID empId;

    @Column(name = "in_time")
    private LocalTime inTime;

    @Column(name = "out_time")
    private LocalTime outTime;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "working_hour")
    private LocalTime workingHour;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @PrePersist
    protected void onCreate(){
        Instant now = Instant.now();
        if(createdOn == null) createdOn = now;
        updatedOn = now;
    }

    @PreUpdate
    protected void onUpdate(){
        updatedOn = Instant.now();
    }
}
