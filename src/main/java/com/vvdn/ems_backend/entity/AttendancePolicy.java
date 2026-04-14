package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "attendance_policy")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "att_policy_id", nullable = false, updatable = false)
    private UUID attPolicyId;

    @Column(name = "min_in_time")
    private LocalTime minInTime;

    @Column(name = "min_out_time")
    private LocalTime minOutTime;

    @Column(name = "min_working_hour")
    private Short minWorkingHour;

    @Column(name = "half_day_hour")
    private Short halfDayHour;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_on")
    private Instant createdOn = Instant.now();

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "updated_on")
    private Instant updatedOn = Instant.now();

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
