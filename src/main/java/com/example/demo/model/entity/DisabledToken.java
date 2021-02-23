package com.example.demo.model.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Getter
@Entity
@Table(name = "disabled_tokens")
public class DisabledToken {
    @Id
    @GeneratedValue
    @Column(name = "id", length = 16, unique = true, nullable = false)
    protected UUID id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "blocked_datetime")
    private LocalDateTime blockedDateTime;
}
