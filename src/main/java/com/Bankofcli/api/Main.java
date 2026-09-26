package com.Bankofcli.api;

import com.Bankofcli.persistence.AccountDAO;
import com.Bankofcli.persistence.TransactionDAO;
import com.Bankofcli.persistence.impl.ConnectionFactory;
import com.Bankofcli.persistence.DatabaseConnectionException;
import com.Bankofcli.service.AccountService;
import com.Bankofcli.service.impl.AccountServiceImpl;
import com.Bankofcli.service.AccountNotFoundException;
import com.Bankofcli.service.AccountAlreadyExistsException;
import com.Bankofcli.service.InsufficientFundsException;
import com.Bankofcli.service.InvalidPinException;
import com.Bankofcli.persistence.DataAccessException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = ConnectionFactory.createConnection()) {
            AccountDAO accountDAO = new com.Bankofcli.persistence.impl.AccountDAOImpl(connection);
            TransactionDAO transactionDAO = new com.Bankofcli.persistence.impl.TransactionDAOImpl(connection);
            AccountService accountService = new AccountServiceImpl(accountDAO, transactionDAO);

            Scanner scanner = new Scanner(System.in);

            //login/register menu
            String loggedInAccountId = null;

            while (loggedInAccountId == null) {
                System.out.println("\n=== Bank of CLI ===");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine();

                try {
                    switch (choice) {
                        case "1" -> {
                            System.out.print("Choose an Account ID: ");
                            String accountId = scanner.nextLine();
                            System.out.print("Choose a PIN: ");
                            String pin = scanner.nextLine();

                            accountService.register(accountId, pin);
                            System.out.println("Account created successfully. Please log in.");
                        }
                        case "2" -> {
                            System.out.print("Enter account ID: ");
                            String accountId = scanner.nextLine();
                            System.out.print("Enter PIN: ");
                            String pin = scanner.nextLine();

                            accountService.login(accountId, pin);
                            System.out.println("Login successful. Welcome, " + accountId + "!");
                            loggedInAccountId = accountId;
                        }
                        case "3" -> {
                            System.out.println("Exiting");
                            return;
                        }
                        default -> System.out.println("Invalid option, try again.");
                    }
                } catch (AccountAlreadyExistsException | AccountNotFoundException | InvalidPinException e) {
                    System.out.println(e.getMessage());
                } catch (DataAccessException e) {
                    System.out.println("Service unavailable. Please try again later.");
                }
            }

            // main menu
            boolean running = true;
            while (running) {
                System.out.println("\n=== Account: " + loggedInAccountId + " ===");
                System.out.println("1. Check Balance");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Transfer");
                System.out.println("5. View Recent Transactions");
                System.out.println("6. Logout");
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine();

                try {
                    switch (choice) {
                        case "1" -> {
                            BigDecimal balance = accountService.checkBalance(loggedInAccountId);
                            System.out.println("Balance: $" + balance);
                        }
                        case "2" -> {
                            System.out.print("Deposit Amount: ");
                            BigDecimal amount = new BigDecimal(scanner.nextLine());
                            accountService.deposit(loggedInAccountId, amount);
                            System.out.println("Deposit successful.");
                        }
                        case "3" -> {
                            System.out.print("Withdraw Amount: ");
                            BigDecimal amount = new BigDecimal(scanner.nextLine());
                            accountService.withdraw(loggedInAccountId, amount);
                            System.out.println("Withdrawal successful.");
                        }
                        case "4" -> {
                            System.out.print("To Account ID: ");
                            String to = scanner.nextLine();
                            System.out.print("Transfer amount: ");
                            BigDecimal amount = new BigDecimal(scanner.nextLine());
                            accountService.transfer(loggedInAccountId, to, amount);
                            System.out.println("Transfer successful.");
                        }
                        case "5" -> {
                            for (var t : accountService.getRecentTransactions(loggedInAccountId, 10)) {
                                System.out.println(t.getType() + " $" + t.getAmount() + " at " + t.getCreatedAt());
                            }
                        }
                        case "6" -> {
                            System.out.println("Logged out.");
                            running = false;
                        }
                        default -> System.out.println("Invalid option, try again.");
                    }
                } catch (AccountNotFoundException | InsufficientFundsException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (DataAccessException e) {
                    System.out.println("Service unavailable. Please try again later.");
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number for the amount.");
                }
            }
        } catch (DatabaseConnectionException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("A database error occurred. Please try again later.");
        }
    }
}