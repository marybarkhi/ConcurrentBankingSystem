package com.Barkhi.concurrentBankingSystem.model.valueobject;

import java.math.BigDecimal;

public record TransferRequest(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
}
