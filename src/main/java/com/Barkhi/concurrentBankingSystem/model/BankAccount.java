package com.Barkhi.concurrentBankingSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Entity
@Table(name = "t_bankAccount")
public class BankAccount {
    @Id
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountHolderName;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String branchCode;

    public BankAccount() {
        this.id = UUID.randomUUID();
    }

}
