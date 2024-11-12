package com.Barkhi.concurrentBankingSystem.repository;

import com.Barkhi.concurrentBankingSystem.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BankJpaRepository extends JpaRepository<BankAccount, UUID> {

    @Query("SELECT a FROM BankAccount a WHERE a.accountNumber = :accountNumber")
    Optional<BankAccount> findByAccountNumber(@Param("accountNumber") String accountNumber);

    // Find accounts by account holder name
    List<BankAccount> findByAccountHolderNameContainingIgnoreCase(String holderName);

    // Find accounts with balance greater than specified amount
    @Query("SELECT a FROM BankAccount a WHERE a.balance > :amount")
    List<BankAccount> findAccountsWithBalanceGreaterThan(@Param("amount") BigDecimal amount);

    // Find accounts with balance less than specified amount
    @Query("SELECT a FROM BankAccount a WHERE a.balance < :amount")
    List<BankAccount> findAccountsWithBalanceLessThan(@Param("amount") BigDecimal amount);

    // Check if account number already exists
    boolean existsByAccountNumber(String accountNumber);

    // Find accounts with zero balance
    @Query("SELECT a FROM BankAccount a WHERE a.balance = 0")
    List<BankAccount> findAccountsWithZeroBalance();

    // Count accounts by holder name
    long countByAccountHolderName(String holderName);

    // Custom query to update balance
    @Modifying
    @Query("UPDATE BankAccount a SET a.balance = :newBalance WHERE a.accountNumber = :accountNumber")
    int updateBalance(@Param("accountNumber") String accountNumber, @Param("newBalance") BigDecimal newBalance);
}
