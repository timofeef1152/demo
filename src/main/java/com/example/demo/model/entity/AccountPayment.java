package com.example.demo.model.entity;

import com.example.demo.model.enums.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "account_payments")
public class AccountPayment {
    @Id
    @GeneratedValue
    @Column(name = "id", length = 16, unique = true, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "from_account", nullable = false)
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account", nullable = false)
    private Account toAccount;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private Long amount;

    @CreationTimestamp
    @Column(name = "operation_datetime")
    private LocalDateTime requestDate;
}
