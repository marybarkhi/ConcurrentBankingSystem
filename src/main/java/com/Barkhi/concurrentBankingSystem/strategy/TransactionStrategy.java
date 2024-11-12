package com.Barkhi.concurrentBankingSystem.strategy;

import com.Barkhi.concurrentBankingSystem.model.BankAccount;

import java.math.BigDecimal;

public interface TransactionStrategy {

    void execute(BankAccount account, BigDecimal amount);
}
