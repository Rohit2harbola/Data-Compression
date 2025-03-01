package com.mycompany.compression.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private JTabbedPane tabbedPane;
    private Component compressionGUIPanel;
    private Component userManagementPanel;
    private Image backgroundImage;

    // Database connection parameters
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Compression";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin123";

    public LoginPanel(JTabbedPane tabbedPane, Component compressionGUIPanel, Component userManagementPanel) {
        this.tabbedPane = tabbedPane;
        this.compressionGUIPanel = compressionGUIPanel;
        this.userManagementPanel = userManagementPanel;

        // Load the background image
        try {
            URL imageUrl = new URL("https://i.pinimg.com/736x/4a/90/33/4a903338c0e478248153bd8f3f6f6745.jpg");
            backgroundImage = ImageIO.read(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Login Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(236, 240, 241));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Username field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setForeground(new Color(236, 240, 241));
        add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBackground(new Color(236, 240, 241));
        usernameField.setForeground(Color.BLACK);
        usernameField.setBorder(new LineBorder(new Color(41, 128, 185), 1));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setForeground(new Color(236, 240, 241));
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(236, 240, 241));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(new LineBorder(new Color(41, 128, 185), 1));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(new LineBorder(new Color(41, 128, 185), 1));
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        // Status label
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(231, 76, 60));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(statusLabel, gbc);

        // Add action listener for login button
        loginButton.addActionListener(new LoginActionListener());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();

            if (username.isEmpty() || password.length == 0) {
                statusLabel.setText("Please enter both username and password.");
                return;
            }

            if (authenticate(username, password)) {
                statusLabel.setText("Login successful!");
                JOptionPane.showMessageDialog(null, "Welcome " + username + "!");
                tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Compression GUI"), true);
                tabbedPane.setSelectedComponent(compressionGUIPanel);
                tabbedPane.removeTabAt(tabbedPane.indexOfTab("Login"));
                tabbedPane.removeTabAt(tabbedPane.indexOfTab("User Management"));
            } else {
                statusLabel.setText("Invalid username or password.");
                JOptionPane.showMessageDialog(null, "Redirecting to User Management.");
                tabbedPane.setSelectedComponent(userManagementPanel);
            }
        }

        private boolean authenticate(String username, char[] password) {
            String query = "SELECT * FROM users WHERE username = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String storedHashedPassword = rs.getString("password");
                        String hashedPassword = hashPassword(password);
                        return hashedPassword.equals(storedHashedPassword);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                statusLabel.setText("Database error: " + ex.getMessage());
            }
            return false;
        }

        private String hashPassword(char[] password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(new String(password).getBytes());
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
    }
}
