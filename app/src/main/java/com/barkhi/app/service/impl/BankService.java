package com.barkhi.app.service.impl;

import com.barkhi.app.logger.TransactionObserver;
import com.barkhi.app.logger.impl.TransactionLogger;
import com.barkhi.app.model.BankAccount;
import com.barkhi.app.repository.BankJpaRepository;
import com.barkhi.app.service.BankingService;
import com.barkhi.app.strategy.impl.DepositStrategy;
import com.barkhi.app.strategy.impl.WithDrawStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BankService implements BankingService {

    private static String DEPOSIT_TX_TYPE = "DEPOSIT";
    private static String WITHDRAW_TX_TYPE = "WITHDRAW";
    private static String TRANSFER_OUT_TX_TYPE = "TRANSFER_OUT";
    private static String TRANSFER_IN_TX_TYPE = "TRANSFER_IN";

    private volatile boolean isProcessing = false;

    private BankJpaRepository bankJpaRepository;

    private List<TransactionObserver> observers;

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public BankService(BankJpaRepository bankJpaRepository) {
        this.bankJpaRepository = bankJpaRepository;
    }

    public BankAccount createAccount(String holderName, BigDecimal initialBalance, String branchCode) {
        BankAccount account = new BankAccount();
        account.setAccountHolderName(holderName);
        account.setAccountNumber(generateAccountNumber(branchCode));
        account.setBalance(initialBalance);
        account.setBranchCode(branchCode);
        return bankJpaRepository.save(account);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deposit(String accountNumber, BigDecimal amount) {
        if (!isProcessing) {
            isProcessing = true;
            try {
                BankAccount account = getAccount(accountNumber);
                new DepositStrategy().execute(account, amount);
                bankJpaRepository.save(account);
                notifyObservers(accountNumber, DEPOSIT_TX_TYPE, amount);
            } catch (RuntimeException e) {
                throw new RuntimeException("Deposit failed due to an issue", e);
            } finally {
                isProcessing = false;
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void withdraw(String accountNumber, BigDecimal amount) {
        if (!isProcessing) {
            isProcessing = true;
            try {
                BankAccount account = getAccount(accountNumber);
                new WithDrawStrategy().execute(account, amount);
                bankJpaRepository.save(account);
                notifyObservers(accountNumber, WITHDRAW_TX_TYPE, amount);
            } catch (RuntimeException e) {
                throw new RuntimeException();
            } finally {
                isProcessing = false;
            }
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {
        CompletableFuture<Void> withdrawFuture = CompletableFuture.runAsync(() -> withdraw(fromAccount, amount), executorService);
        CompletableFuture<Void> depositFuture = withdrawFuture.thenRunAsync(() -> deposit(toAccount, amount), executorService);
        try {
            depositFuture.get();
            notifyObservers(fromAccount, TRANSFER_OUT_TX_TYPE, amount);
            notifyObservers(toAccount, TRANSFER_IN_TX_TYPE, amount);
        } catch (Exception e) {
            throw new NoSuchElementException("Transfer failed due to invalid account number or other issues", e);
        }
    }

    @Override
    @Transactional
    public BankAccount getAccount(String accountNumber) {
        BankAccount account = bankJpaRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NoSuchElementException("Account number " + accountNumber + " not found"));
        return account;

    }

    public List<BankAccount> displayAllAccounts() {
        return bankJpaRepository.findAll();
    }

    private void notifyObservers(String accountNumber, String transactionType, BigDecimal amount) {
        //todo is it ok to initialize directly
        observers = new ArrayList<>();
        TransactionLogger logger = new TransactionLogger();
        observers.add(logger);
        observers.forEach(observer ->
                observer.onTransaction(accountNumber, transactionType, amount));

    }

    private String generateAccountNumber(String branchCode) {
        return branchCode + "-" + Math.random();
    }

}
