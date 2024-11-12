package com.Barkhi.concurrentBankingSystem.model.valueobject;

import java.math.BigDecimal;

public record AccountCreationRequest(String accountHolderName, BigDecimal initialBalance, String branchCode) {
}
