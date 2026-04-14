package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "leave_application")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "leave_application_id")
    private UUID leaveApplicationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_leave_id", nullable = false)
    private EmployeeLeave employeeLeaves;

    @Column(name = "status")
    private String status;

    @Column(name = "leave_day")
    private String leaveDay;

    @Column(name = "description")
    private String description;

    @Column(name = "no_of_days")
    private Short noOfDays;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "app_rej_by")
    private UUID appRejBy;

    @Column(name = "app_rej_on")
    private LocalDate appRejOn;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_on")
    private Instant createdOn;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (createdOn == null) createdOn = now;

    }
}

