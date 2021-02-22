package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Setter
@NoArgsConstructor
@Getter
@Entity
@Table(name = "app_users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", length = 16, unique = true, nullable = false)
    protected UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Account> accounts;

    @Column
    private UUID loggingId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "login_attempt_id", referencedColumnName = "id")
    private LoginAttemptInfo loginAttemptInfo;
}