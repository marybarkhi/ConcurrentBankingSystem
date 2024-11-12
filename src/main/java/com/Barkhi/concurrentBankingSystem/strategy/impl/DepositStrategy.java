package com.Barkhi.concurrentBankingSystem.strategy.impl;

import com.Barkhi.concurrentBankingSystem.model.BankAccount;
import com.Barkhi.concurrentBankingSystem.strategy.TransactionStrategy;

import java.math.BigDecimal;

public class DepositStrategy implements TransactionStrategy {
    @Override
    public void execute(BankAccount account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
    }
}
