package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "leave_type")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "type_id", nullable = false, updatable = false)
    private UUID typeId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "carry_forward_allowed")
    private Boolean carryForwardAllowed;

    @Column(name = "post_application_allowed")
    private Boolean postApplicationAllowed;

    @Column(name = "max_consecutive_days")
    private Short maxConsecutiveDays;

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
