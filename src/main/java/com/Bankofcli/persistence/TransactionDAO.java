package com.Bankofcli.persistence;

import com.Bankofcli.domain.Transaction;
import java.util.List;

public interface TransactionDAO {
    void save(Transaction transaction);
    List<Transaction> findRecentByAccountId(String accountId, int limit);
}