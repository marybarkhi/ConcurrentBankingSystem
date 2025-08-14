package com.barkhi.app.strategy.impl;

import com.barkhi.app.model.BankAccount;
import com.barkhi.app.strategy.TransactionStrategy;

import java.math.BigDecimal;

public class DepositStrategy implements TransactionStrategy {
    @Override
    public void execute(BankAccount account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
    }
}
