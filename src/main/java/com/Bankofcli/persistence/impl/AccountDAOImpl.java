package com.Bankofcli.persistence.impl;

import com.Bankofcli.domain.Account;
import com.Bankofcli.persistence.AccountDAO;
import com.Bankofcli.persistence.DataAccessException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.OffsetDateTime;

public class AccountDAOImpl implements AccountDAO {
    private final Connection connection;

    public AccountDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Account findById(String accountId) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                            rs.getString("account_id"),
                            rs.getString("pin_hash"),
                            rs.getBigDecimal("balance"),
                            rs.getObject("created_at", OffsetDateTime.class)
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find account " + accountId, e);
        }
    }

    @Override
    public void save(Account account) {
        String sql = "INSERT INTO accounts (account_id, pin_hash, balance) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, account.getAccountId());
            ps.setString(2, account.getPinHash());
            ps.setBigDecimal(3, account.getBalance());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to save account " + account.getAccountId(), e);
        }
    }

    @Override
    public void updateBalance(String accountId, BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, newBalance);
            ps.setString(2, accountId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update balance for " + accountId, e);
        }
    }

    @Override
    public boolean existsById(String accountId) {
        return findById(accountId) != null;
    }
}