package com.Barkhi.concurrentBankingSystem.controller;


import com.Barkhi.concurrentBankingSystem.model.BankAccount;
import com.Barkhi.concurrentBankingSystem.model.valueobject.TransferRequest;
import com.Barkhi.concurrentBankingSystem.service.impl.BankService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping
public class BankController {

    private BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/accounts")
    public String displayAccounts(Model model) {
        List<BankAccount> bankAccounts = bankService.displayAllAccounts();
        model.addAttribute("accounts", bankAccounts);
        return "";
    }

    @GetMapping("/new")
    public String showNewAccountPage(Model model) {
        BankAccount bankAccount = new BankAccount();
        model.addAttribute("account", bankAccount);
        return "NewAccount";
    }

    @PostMapping("/create")
    public String createAccount(@ModelAttribute("account") BankAccount bankAccount) {
        bankService.createAccount(bankAccount.getAccountHolderName(), bankAccount.getBalance(), bankAccount.getBranchCode());
        return "redirect:/";

    }

    @GetMapping("/transaction/{accountNumber}")
    public ModelAndView showTransactionPage(@PathVariable String accountNumber) {
        ModelAndView modelAndView = new ModelAndView("Transaction");
        BankAccount bankAccount = bankService.getAccount(accountNumber);
        modelAndView.addObject("account", bankAccount);
        return modelAndView;
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
       /* if (!accountNumber.equals(transactionRequest.accountNumber())) {
            model.addAttribute("message",
                    "Cannot deposit account " + transactionRequest.accountNumber()
                            + " doesn't match id to update: " + accountNumber + ".");
            return "error-page";
        }*/
        bankService.deposit(accountNumber, amount);
        return "redirect:/";
    }

    @PostMapping("/withdraw")
    public void withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount){
        bankService.withdraw(accountNumber, amount);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferRequest request) {
        bankService.transfer(request.fromAccountNumber(), request.toAccountNumber(), request.amount());
    }
}
