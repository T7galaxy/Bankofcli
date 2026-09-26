package com.Bankofcli.persistence;

import com.Bankofcli.domain.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionDAOTest implements TransactionDAO {
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findRecentByAccountId(String accountId, int limit) {
        return transactions.stream()
                .filter(t -> t.getAccountId().equals(accountId))
                .limit(limit)
                .toList();
    }
}