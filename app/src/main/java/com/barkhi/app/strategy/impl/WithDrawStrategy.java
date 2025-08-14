package com.barkhi.app.strategy.impl;

import com.barkhi.app.model.BankAccount;
import com.barkhi.app.strategy.TransactionStrategy;

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
