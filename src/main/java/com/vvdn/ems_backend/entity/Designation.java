package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "designation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "desig_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @PrePersist
    protected void onCreate(){
        Instant now = Instant.now();
        if(createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = Instant.now();
    }

}
