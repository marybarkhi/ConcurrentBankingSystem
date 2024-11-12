package com.Barkhi.concurrentBankingSystem.model.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

public record TransactionRequest(BigDecimal amount) {
}
