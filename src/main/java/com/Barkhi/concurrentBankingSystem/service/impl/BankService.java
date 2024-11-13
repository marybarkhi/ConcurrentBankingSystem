package com.Barkhi.concurrentBankingSystem.service.impl;

import com.Barkhi.concurrentBankingSystem.logger.TransactionObserver;
import com.Barkhi.concurrentBankingSystem.logger.impl.TransactionLogger;
import com.Barkhi.concurrentBankingSystem.model.BankAccount;
import com.Barkhi.concurrentBankingSystem.repository.BankJpaRepository;
import com.Barkhi.concurrentBankingSystem.service.BankingService;
import com.Barkhi.concurrentBankingSystem.strategy.impl.DepositStrategy;
import com.Barkhi.concurrentBankingSystem.strategy.impl.WithDrawStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

@Service
public class BankService implements BankingService {

    private static String DEPOSIT_TX_TYPE = "DEPOSIT";
    private static String WITHDRAW_TX_TYPE = "WITHDRAW";
    private static String TRANSFER_OUT_TX_TYPE = "TRANSFER_OUT";
    private static String TRANSFER_IN_TX_TYPE = "TRANSFER_IN";


    private BankJpaRepository bankJpaRepository;

    private ExecutorService executorService = Executors.newFixedThreadPool(Thread.activeCount());

    private List<TransactionObserver> observers;

    public BankService(BankJpaRepository bankJpaRepository, List<TransactionObserver> observers) {
        this.bankJpaRepository = bankJpaRepository;
        this.observers = observers;
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
    public void deposit(String accountNumber, BigDecimal amount) {
        Future<Void> depositFuture = executorService.submit(() -> {
            try {
                BankAccount account = getAccount(accountNumber);
                new DepositStrategy().execute(account, amount);
                bankJpaRepository.save(account);
                notifyObservers(accountNumber, DEPOSIT_TX_TYPE, amount);
            } catch (Exception e) {
                // Handle exception
            }
            return null;
        });
        try {
            depositFuture.get(200, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void withdraw(String accountNumber, BigDecimal amount) {
        executorService.submit(() -> {
            try {
                BankAccount account = getAccount(accountNumber);
                new WithDrawStrategy().execute(account, amount);
                bankJpaRepository.save(account);
                notifyObservers(accountNumber, WITHDRAW_TX_TYPE, amount);
            } catch (Exception e) {
                // Handle exception
            }
        });
        //TODO if there is need to get a future
    }

    @Override
    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {
        executorService.submit(() -> {
            try {
                withdraw(fromAccount, amount);
                deposit(toAccount, amount);
                notifyObservers(fromAccount, TRANSFER_OUT_TX_TYPE, amount);
                notifyObservers(toAccount, TRANSFER_IN_TX_TYPE, amount);
            } catch (Exception e) {
                // Handle exception
            }
        });
    }

    public List<BankAccount> displayAllAccounts() {
        return bankJpaRepository.findAll();
    }

    private void notifyObservers(String accountNumber, String transactionType, BigDecimal amount) {
        //todo is it ok to initialize directly
        TransactionLogger logger = new TransactionLogger();
        observers.add(logger);
        observers.forEach(observer ->
                observer.onTransaction(accountNumber, transactionType, amount));
    }

    private String generateAccountNumber(String branchCode) {
        return branchCode + "-" + String.valueOf(Math.random());
    }

    public BankAccount getAccount(String accountNumber) {
        BankAccount account = bankJpaRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NoSuchElementException());
        return account;
    }

}
