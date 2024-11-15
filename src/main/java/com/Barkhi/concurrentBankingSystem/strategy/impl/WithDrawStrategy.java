package com.Barkhi.concurrentBankingSystem.strategy.impl;

import com.Barkhi.concurrentBankingSystem.model.BankAccount;
import com.Barkhi.concurrentBankingSystem.strategy.TransactionStrategy;

import java.math.BigDecimal;

public class WithDrawStrategy implements TransactionStrategy {
    @Override
    public void execute(BankAccount account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException();
        }
        account.setBalance(account.getBalance().subtract(amount));
    }
}
