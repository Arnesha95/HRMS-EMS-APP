package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "dept_desig_mapping",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"dept_id", "desig_id"})
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeptDesigMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "dept_id", nullable = false)
    private UUID deptId;

    @Column(name = "desig_id", nullable = false)
    private UUID desigId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
