package com.barkhi.app.strategy;

import com.barkhi.app.model.BankAccount;

import java.math.BigDecimal;

public interface TransactionStrategy {

    void execute(BankAccount account, BigDecimal amount);
}
