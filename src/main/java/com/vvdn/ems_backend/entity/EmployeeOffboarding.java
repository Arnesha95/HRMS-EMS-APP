package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "employee_offboarding")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeOffboarding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "offboarding_id")
    private UUID offboardingId;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "offboarding_type")
    private  OffboardingType offboardingType;

    @Column(name = "resignation_date")
    private LocalDate resignationDate;

    @Column(name = "proposed_LWD")
    private LocalDate proposedLastWorkingDate;

    @Column(name = "final_LWD")
    private LocalDate finalLastWorkingDate;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "offboarding_status")
    private OffboardingStatus offboardingStatus;

    @Column(name = "action_by")
    private UUID actionBy;

    @Column(name = "action_on")
    private Instant actionOn;

    @Column(name = "is_clearance_done")
    private Boolean isClearanceDone;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "is_good_to_hire")
    private Boolean isGoodToRehire;

}
