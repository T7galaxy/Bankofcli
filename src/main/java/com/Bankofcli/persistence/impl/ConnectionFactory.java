package com.Bankofcli.persistence.impl;

import com.Bankofcli.persistence.DatabaseConnectionException;
import com.Bankofcli.persistence.impl.Dbsecrets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = Dbsecrets.URL;
    private static final String USER = Dbsecrets.USER;
    private static final String PASSWORD = Dbsecrets.PASSWORD;

    public static Connection createConnection() throws SQLException {
        try{
        return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {

            throw new DatabaseConnectionException("Unable to connect to the database. Please make sure it's running.", e);
        }
    }
}