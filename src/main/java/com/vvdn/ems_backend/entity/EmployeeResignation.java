package com.vvdn.ems_backend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "employee_resignation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResignation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "resignation_id", nullable = false)
    private UUID resignationId;

    @ManyToOne
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    @Column(name = "resig_date", nullable = false)
    private LocalDate resignationDate;

    @Column(name = "proposed_LWD", nullable = false)
    private LocalDate proposedLastWorkingDay;

    @Column(name = "reason")
    private String reason;

    @Column(name = "notice_period")
    private Integer noticePeriod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ResignationStatus status;

    @Column(name = "final_LWD", nullable = false)
    private LocalDate finalLastWorkingDay; // set by HR
}
