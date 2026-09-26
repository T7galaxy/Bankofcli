package com.Bankofcli.service.impl;

import com.Bankofcli.domain.Account;
import com.Bankofcli.persistence.AccountDAOTest;
import com.Bankofcli.persistence.TransactionDAOTest;
import com.Bankofcli.service.AccountService;
import com.Bankofcli.service.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {

    private AccountService accountService;
    private AccountDAOTest accountDAOTest;

    @BeforeEach
    void setUp() {
        accountDAOTest = new AccountDAOTest();
        TransactionDAOTest transactionRepository = new TransactionDAOTest();
        accountService = new AccountServiceImpl(accountDAOTest, transactionRepository);

        accountDAOTest.save(new Account("1970", "1234", new BigDecimal("100.00"), OffsetDateTime.now()));
    }

    @Test
    void withdraw_sufficientFunds_reducesBalance() {
        accountService.withdraw("1970", new BigDecimal("30.00"));
        assertEquals(new BigDecimal("70.00"), accountService.checkBalance("1970"));
    }

    @Test
    void withdraw_insufficientFunds_throwsException() {
        assertThrows(InsufficientFundsException.class, () ->
                accountService.withdraw("1970", new BigDecimal("500.00")));
        assertEquals(new BigDecimal("100.00"), accountService.checkBalance("1970"));
    }

}