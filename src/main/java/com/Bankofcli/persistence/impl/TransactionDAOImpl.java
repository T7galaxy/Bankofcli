package com.Bankofcli.persistence.impl;

import com.Bankofcli.domain.Transaction;
import com.Bankofcli.persistence.TransactionDAO;
import com.Bankofcli.persistence.DataAccessException;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionDAOImpl implements TransactionDAO {
    private final Connection connection;

    public TransactionDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO transactions (transaction_id, account_id, related_account_id, type, amount) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, transaction.getTransactionId());
            ps.setString(2, transaction.getAccountId());
            ps.setString(3, transaction.getRelatedAccountId());
            ps.setString(4, transaction.getType().name());
            ps.setBigDecimal(5, transaction.getAmount());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to save transaction for account " + transaction.getAccountId(), e);
        }
    }

    @Override
    public List<Transaction> findRecentByAccountId(String accountId, int limit) {
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY created_at DESC LIMIT ?";
        List<Transaction> results = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch transactions for account " + accountId, e);
        }
        return results;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        return new Transaction(
                (UUID) rs.getObject("transaction_id"),
                rs.getString("account_id"),
                rs.getString("related_account_id"),
                Transaction.Type.valueOf(rs.getString("type")),
                rs.getBigDecimal("amount"),
                rs.getObject("created_at", OffsetDateTime.class)
        );
    }
}