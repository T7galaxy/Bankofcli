package com.Bankofcli.persistence;

import com.Bankofcli.domain.Account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AccountDAOTest implements AccountDAO {
    private final Map<String, Account> accounts = new HashMap<>();

    @Override
    public Account findById(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public void save(Account account) {
        accounts.put(account.getAccountId(), account);
    }

    @Override
    public void updateBalance(String accountId, BigDecimal newBalance) {
        Account existing = accounts.get(accountId);
        if (existing != null) {
            existing.setBalance(newBalance);
        }
    }

    @Override
    public boolean existsById(String accountId) {
        return accounts.containsKey(accountId);
    }
}