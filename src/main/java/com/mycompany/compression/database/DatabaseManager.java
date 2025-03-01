package com.mycompany.compression.database;

import java.sql.*;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() throws SQLException {
        // Replace with your PostgreSQL database URL, username, and password
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Compression", "postgres", "admin123");
    }

    public void addUser(String username, String passwordHash) throws SQLException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            stmt.executeUpdate();
        }
    }

    public boolean validateUser(String username, String passwordHash) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    // Close the connection when finished
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
