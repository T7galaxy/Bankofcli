package com.Bankofcli.service.impl;

import com.Bankofcli.domain.Account;
import com.Bankofcli.domain.Transaction;
import com.Bankofcli.persistence.AccountDAO;
import com.Bankofcli.persistence.TransactionDAO;
import com.Bankofcli.service.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountServiceImpl implements AccountService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    public AccountServiceImpl(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    @Override
    public BigDecimal checkBalance(String accountId) {
        Account account = getAccountOrThrow(accountId);
        return account.getBalance();
    }

    @Override
    public void deposit(String accountId, BigDecimal amount) {
        Account account = getAccountOrThrow(accountId);

        BigDecimal newBalance = account.getBalance().add(amount);
        accountDAO.updateBalance(accountId, newBalance);

        transactionDAO.save(new Transaction(
                UUID.randomUUID(), accountId, null,
                Transaction.Type.DEPOSIT, amount, OffsetDateTime.now()
        ));
    }

    @Override
    public void withdraw(String accountId, BigDecimal amount) {
        Account account = getAccountOrThrow(accountId);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for account " + accountId);
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);
        accountDAO.updateBalance(accountId, newBalance);

        transactionDAO.save(new Transaction(
                UUID.randomUUID(), accountId, null,
                Transaction.Type.WITHDRAW, amount, OffsetDateTime.now()
        ));
        logger.info("Account {} withdrew {}", accountId, amount);
    }

    @Override
    public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        Account fromAccount = getAccountOrThrow(fromAccountId);
        Account toAccount = getAccountOrThrow(toAccountId);

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for account " + fromAccountId);
        }

        BigDecimal newFromBalance = fromAccount.getBalance().subtract(amount);
        BigDecimal newToBalance = toAccount.getBalance().add(amount);

        accountDAO.updateBalance(fromAccountId, newFromBalance);
        accountDAO.updateBalance(toAccountId, newToBalance);

        transactionDAO.save(new Transaction(
                UUID.randomUUID(), fromAccountId, toAccountId,
                Transaction.Type.TRANSFER, amount, OffsetDateTime.now()
        ));
    }
    @Override
    public void register(String accountId, String pin) {
        if (accountDAO.existsById(accountId)) {
            throw new AccountAlreadyExistsException("Account ID already exists: " + accountId);
        }
        Account newAccount = new Account(accountId, pin, BigDecimal.ZERO, OffsetDateTime.now());
        accountDAO.save(newAccount);
    }

    @Override
    public void login(String accountId, String pin) {
        Account account = accountDAO.findById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("No account found: " + accountId);
        }
        if (!account.getPinHash().equals(pin)) {
            logger.error("Login failed: incorrect PIN entered for {}", accountId);
            throw new InvalidPinException("Incorrect PIN for account " + accountId);
        }
    }

    @Override
    public List<Transaction> getRecentTransactions(String accountId, int limit) {
        getAccountOrThrow(accountId);
        return transactionDAO.findRecentByAccountId(accountId, limit);
    }

    private Account getAccountOrThrow(String accountId) {
        Account account = accountDAO.findById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("No account found: " + accountId);
        }
        return account;
    }
}