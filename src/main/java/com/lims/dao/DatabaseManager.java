package com.lims.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:./src/main/resources/database/db.sqlite";

    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
