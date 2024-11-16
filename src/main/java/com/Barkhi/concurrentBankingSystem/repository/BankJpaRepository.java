package com.Barkhi.concurrentBankingSystem.repository;

import com.Barkhi.concurrentBankingSystem.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankJpaRepository extends JpaRepository<BankAccount, UUID> {

    @Query("SELECT a FROM BankAccount a WHERE a.accountNumber = :accountNumber")
    Optional<BankAccount> findByAccountNumber(@Param("accountNumber") String accountNumber);
}
