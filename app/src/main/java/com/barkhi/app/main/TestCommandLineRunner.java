package com.barkhi.app.main;

import com.barkhi.app.service.BankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TestCommandLineRunner implements CommandLineRunner {
    private final BankingService bankingService;
    @Override
    public void run(String... args) throws Exception {
        bankingService.createAccount("", BigDecimal.ONE,"");
    }
}
