package com.barkhi.app.logger.impl;

import com.barkhi.app.logger.TransactionObserver;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionLogger implements TransactionObserver {
    private static final String LOG_FILE = "transactions.log";


    public void onTransaction(String accountNumber, String transactionType, BigDecimal amount) {
        String logMessage = String.format("[%s] Account: %s, Type: %s, Amount: %s",
                LocalDateTime.now(), accountNumber, transactionType, amount);

        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(logMessage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
