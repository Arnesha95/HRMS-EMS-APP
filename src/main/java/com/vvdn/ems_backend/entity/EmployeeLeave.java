package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "employee_leaves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "emp_leave_id")
    private UUID empLeaveId;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private LeavePolicy leavePolicy;

    @Column(name = "total_leaves")
    private Float totalLeaves;

    @Column(name = "used_leaves")
    private Float usedLeaves;

    @Column(name = "remaining_leaves")
    private Float remainingLeaves;

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
