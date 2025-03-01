package com.mycompany.compression.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class UserManagementPanel extends JPanel {
    private JButton registerButton;
    private JButton changePasswordButton;
    private JButton logoutButton;
    private String currentUsername;  // Holds the username of the currently logged-in user

    // Database connection parameters (replace with your PostgreSQL credentials)
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Compression";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin123";

    public UserManagementPanel() {
        setLayout(new GridBagLayout()); // Switch to GridBagLayout for flexible positioning
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components

        // Initialize buttons
        registerButton = new JButton("Register");
        changePasswordButton = new JButton("Change Password");
        logoutButton = new JButton("Logout");

        // Set grid settings for the Register button
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbc);

        // Set grid settings for the Change Password button
        gbc.gridy = 1;
        add(changePasswordButton, gbc);

        // Set grid settings for the Logout button
        gbc.gridy = 2;
        add(logoutButton, gbc);

        // Add action listeners to buttons
        registerButton.addActionListener(new RegisterActionListener());
        changePasswordButton.addActionListener(new ChangePasswordActionListener());
        logoutButton.addActionListener(new LogoutActionListener());
    }

    // This method should be called when login is successful to update the logged-in username
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = JOptionPane.showInputDialog("Enter username:");
            String password = new String(JOptionPane.showInputDialog("Enter password:"));

            if (username != null && password != null && !username.trim().isEmpty() && !password.trim().isEmpty()) {
                // Register in the background to keep UI responsive
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        registerUser(username, password);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                            JOptionPane.showMessageDialog(null, "User registered successfully!");
                        } catch (ExecutionException | InterruptedException ex) {
                            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                        }
                    }
                }.execute();
            } else {
                JOptionPane.showMessageDialog(null, "Username and password cannot be empty.");
            }
        }
    }

private class ChangePasswordActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = JOptionPane.showInputDialog("Enter your username:");
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username cannot be empty.");
            return;
        }

        String newPassword = JOptionPane.showInputDialog("Enter new password:");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "New password cannot be empty.");
            return;
        }

        // Validate and update password in the background
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                if (validateUser(username)) {
                    changePassword(username, newPassword);
                    return true;
                }
                return false;
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(null, "Password changed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Username not found. Password reset failed.");
                    }
                } catch (ExecutionException | InterruptedException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        }.execute();
    }
}


    private class LogoutActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentUsername == null) {
                JOptionPane.showMessageDialog(null, "No user is logged in.");
                return;
            }

            // Clear the current username when logged out
            setCurrentUsername(null);
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
        }
    }

    // Hash the password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Database Operations (PostgreSQL)
    private void registerUser(String username, String password) throws SQLException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Hash the password before storing it
            String hashedPassword = hashPassword(password);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
        }
    }

private void changePassword(String username, String newPassword) throws SQLException {
    String query = "UPDATE users SET password = ? WHERE username = ?";
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        // Hash the new password before updating it
        String hashedPassword = hashPassword(newPassword);
        stmt.setString(1, hashedPassword);
        stmt.setString(2, username);
        stmt.executeUpdate();
    }
}


    // Simulate user login by checking if the user exists in the database
    public void simulateLogin(String username) {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return validateUser(username);
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        setCurrentUsername(username);  // Set the current logged-in username
                        JOptionPane.showMessageDialog(null, "User logged in successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "User does not exist.");
                    }
                } catch (ExecutionException | InterruptedException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

private boolean validateUser(String username) throws SQLException {
    String query = "SELECT 1 FROM users WHERE username = ?";
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, username);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next(); // Returns true if the username exists
        }
    }
}

    // Load PostgreSQL JDBC driver
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
