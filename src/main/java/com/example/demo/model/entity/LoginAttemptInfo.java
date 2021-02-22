package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@NoArgsConstructor
@Getter
@Entity
@Table(name = "login_attempts")
public class LoginAttemptInfo {
    @Id
    @GeneratedValue
    @Column(name = "id", length = 16, unique = true, nullable = false)
    protected UUID id;

    @OneToOne(mappedBy = "loginAttemptInfo")
    private User user;

    @Column(name = "attempt_quantity")
    private Integer attemptQuantity;

    @Column(name = "accessible_login_datetime")
    private LocalDateTime accessibleLoginDateTime;

    @UpdateTimestamp
    @Column(name = "last_login_attempt_datetime")
    private LocalDateTime lastLoginAttemptDatetime;

    @CreationTimestamp
    @Column(name = "first_login_datetime")
    private LocalDateTime firstLoginDatetime;

    @PrePersist
    private void onCreate() {
        firstLoginDatetime = LocalDateTime.now();
    }
}
