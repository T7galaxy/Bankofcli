package com.Bankofcli.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class Account {
    private String accountId;
    private String pinHash;
    private BigDecimal balance;
    private OffsetDateTime createdAt;

    public Account(String accountId, String pinHash, BigDecimal balance, OffsetDateTime createdAt) {
        this.accountId = accountId;
        this.pinHash = pinHash;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public String getAccountId() { return accountId; }
    public String getPinHash() { return pinHash; }
    public BigDecimal getBalance() { return balance; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setBalance(BigDecimal balance) { this.balance = balance; }
}