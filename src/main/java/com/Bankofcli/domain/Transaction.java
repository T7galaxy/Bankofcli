package com.Bankofcli.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Transaction {
    public enum Type { DEPOSIT, WITHDRAW, TRANSFER }

    private UUID transactionId;
    private String accountId;
    private String relatedAccountId; // nullable, only used for TRANSFER
    private Type type;
    private BigDecimal amount;
    private OffsetDateTime createdAt;

    public Transaction(UUID transactionId, String accountId, String relatedAccountId,
                       Type type, BigDecimal amount, OffsetDateTime createdAt) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.relatedAccountId = relatedAccountId;
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public UUID getTransactionId() { return transactionId; }
    public String getAccountId() { return accountId; }
    public String getRelatedAccountId() { return relatedAccountId; }
    public Type getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}