package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "employment_type")
public class EmploymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "emp_type_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "emp_type_name",nullable = false, unique = true)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn = Instant.now();

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn = Instant.now();

    @Column(name = "updated_by")
    private UUID updatedBy;


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
