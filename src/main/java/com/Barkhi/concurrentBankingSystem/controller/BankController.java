package com.Barkhi.concurrentBankingSystem.controller;


import com.Barkhi.concurrentBankingSystem.model.BankAccount;
import com.Barkhi.concurrentBankingSystem.model.valueobject.AccountCreationRequest;
import com.Barkhi.concurrentBankingSystem.model.valueobject.TransactionRequest;
import com.Barkhi.concurrentBankingSystem.model.valueobject.TransferRequest;
import com.Barkhi.concurrentBankingSystem.service.impl.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public ResponseEntity<List<BankAccount>> displayAccounts() {
        List<BankAccount> bankAccounts = bankService.displayAllAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(bankAccounts);
    }

    @PostMapping("/account")
    public BankAccount createAccount(@RequestBody AccountCreationRequest request) {
        return bankService.createAccount(request.accountHolderName(), request.initialBalance(), request.branchCode());
    }

    @PostMapping("/accounts/{accountNumber}/deposit")
    public void deposit(@PathVariable String accountNumber, @RequestBody TransactionRequest request) {
        bankService.deposit(accountNumber, request.amount());
    }

    @PostMapping("/accounts/{accountNumber}/withdraw")
    public void withdraw(@PathVariable String accountNumber, @RequestBody TransactionRequest request) throws ExecutionException, InterruptedException {
        bankService.withdraw(accountNumber, request.amount());
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferRequest request) {
        bankService.transfer(request.fromAccountNumber(), request.toAccountNumber(), request.amount());
    }
}
