package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "emp_id")
    private UUID empId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @Column(name = "pan_num")
    private String panNum;

    @Column(name = "aadhar_num")
    private String aadharNum;

    @Column(name = "passport_num")
    private String passportNum;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "offer_letter_num")
    private String offerLetterNum;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "reporting_manager")
    private UUID reportingManager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_type_id", nullable = false)
    private EmploymentType employmentType;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "notice_period")
    private Short noticePeriod;


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
