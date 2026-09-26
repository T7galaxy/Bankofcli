package com.Bankofcli.service;

import com.Bankofcli.domain.Transaction;
import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    BigDecimal checkBalance(String accountId);
    void deposit(String accountId, BigDecimal amount);
    void withdraw(String accountId, BigDecimal amount);
    void transfer(String fromAccountId, String toAccountId, BigDecimal amount);
    List<Transaction> getRecentTransactions(String accountId, int limit);
    void register(String accountId,String pin);
    void login(String accountId,String pin);
}