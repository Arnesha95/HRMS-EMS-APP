package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "employee_offboarding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeOffboarding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "offboarding_id", nullable = false)
    private UUID offboardingId;

    @OneToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @Column(name = "last_working_day")
    private LocalDate lastWorkingDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "offboarding_type")
    private OffboardingType offboardingType;

    @Column(name = "exit_reason")
    private String exitReason;

    @Column(name = "clearance_status")
    private Boolean clearanceStatus; // Admin clearance

    @Column(name = "is_good_to_hire")
    private Boolean rehireEligible;

    @Column(name = "notice_period_status")
    private String noticePeriodStatus;
}
