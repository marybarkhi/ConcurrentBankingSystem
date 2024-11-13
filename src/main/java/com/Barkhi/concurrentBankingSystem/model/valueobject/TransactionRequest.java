package com.Barkhi.concurrentBankingSystem.model.valueobject;

import java.math.BigDecimal;

public record TransactionRequest(String accountNumber,BigDecimal amount) {
}
