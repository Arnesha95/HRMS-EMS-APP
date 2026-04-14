package com.vvdn.ems_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToOne
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    private Employee employee;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private Instant createdOn;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private Instant updatedOn;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return isActive;
    }


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
