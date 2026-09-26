package com.Bankofcli.persistence;

import com.Bankofcli.domain.Account;
import java.math.BigDecimal;

public interface AccountDAO {
    Account findById(String accountId);
    void save(Account account);
    void updateBalance(String accountId, BigDecimal newBalance);
    boolean existsById(String accountId);
}
