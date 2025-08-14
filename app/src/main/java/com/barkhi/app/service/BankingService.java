package com.barkhi.app.service;

import com.barkhi.app.model.BankAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface BankingService {

    BankAccount createAccount(String holderName, BigDecimal initialBalance, String branchCode);

    void deposit(String accountNumber, BigDecimal amount);

    void withdraw(String accountNumber, BigDecimal amount) throws ExecutionException, InterruptedException;

    void transfer(String fromAccount, String toAccount, BigDecimal amount);

    BankAccount getAccount(String accountNumber);

    List<BankAccount> displayAllAccounts();
}
