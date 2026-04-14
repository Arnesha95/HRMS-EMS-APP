package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "leave_policy")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeavePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "policy_id", nullable = false, updatable = false)
    private UUID policyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "no_of_days")
    private Short noOfDays;

    @Column(name = "year")
    private Short year;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_type_id", nullable = false)
    private EmploymentType employmentType;

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