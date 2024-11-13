package com.Barkhi.concurrentBankingSystem.controller;


import com.Barkhi.concurrentBankingSystem.model.BankAccount;
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

    @GetMapping
    public String displayAccounts(Model model) {
        List<BankAccount> bankAccounts = bankService.displayAllAccounts();
        model.addAttribute("accounts", bankAccounts);
        return "index";
    }

    @GetMapping("/new")
    public String showNewAccountPage(Model model) {
        BankAccount bankAccount = new BankAccount();
        model.addAttribute("account", bankAccount);
        return "newAccount";
    }

    @PostMapping("/create")
    public String createAccount(@ModelAttribute("account") BankAccount bankAccount) {
        bankService.createAccount(bankAccount.getAccountHolderName(), bankAccount.getBalance(),
                bankAccount.getBranchCode());
        return "redirect:/";

    }

    @GetMapping("/depositTransaction/{accountNumber}")
    public ModelAndView showDepositTransactionPage(@PathVariable String accountNumber) {
        ModelAndView modelAndView = new ModelAndView("depositTransaction");
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

    @GetMapping("/withDrawTransaction/{accountNumber}")
    public ModelAndView showWithDrawTransactionPage(@PathVariable String accountNumber) {
        ModelAndView modelAndView = new ModelAndView("withDrawTransaction");
        BankAccount bankAccount = bankService.getAccount(accountNumber);
        modelAndView.addObject("account", bankAccount);
        return modelAndView;
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        bankService.withdraw(accountNumber, amount);
        return "redirect:/";
    }

    @GetMapping("/transferMoneyTransaction/{sourceAccountNumber}")
    public ModelAndView showTransferMoneyPage(@PathVariable String sourceAccountNumber) {
        ModelAndView modelAndView = new ModelAndView("transferMoneyTransaction");
        BankAccount bankAccount = bankService.getAccount(sourceAccountNumber);
        modelAndView.addObject("account", bankAccount);
        return modelAndView;
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String accountNumber,
                           @RequestParam BigDecimal balance,
                           @RequestParam String destinationAccountNumber,
                           @RequestParam BigDecimal transferAmount) {
        if (balance.compareTo(transferAmount) <= 0) {
            //
        }
        bankService.transfer(accountNumber, destinationAccountNumber, transferAmount);
        return "redirect:/";
    }
}
