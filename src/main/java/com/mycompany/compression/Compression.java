//


























package com.mycompany.compression;

import com.mycompany.compression.gui.LoginPanel;
import com.mycompany.compression.gui.UserManagementPanel;
import com.mycompany.compression.gui.CompressionGUI;

import javax.swing.*;

public class Compression {

    // Entry point — launch Welcome screen first
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new com.mycompany.compression.gui.Welcome(); // Launch Welcome screen first
        });
    }

    // Launches the main compression GUI
    public static void launchMainApplication() {
        JFrame frame = new JFrame("Welcome to Compression And Decompression Using Hybrid Approch");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        UserManagementPanel userManagementPanel = new UserManagementPanel();
        CompressionGUI compressionGUIPanel = new CompressionGUI(tabbedPane, tabbedPane.indexOfTab("User Management"));
        LoginPanel loginPanel = new LoginPanel(tabbedPane, compressionGUIPanel, userManagementPanel);

        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("User Management", userManagementPanel);
        tabbedPane.addTab("Compression GUI", compressionGUIPanel);
        tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Compression GUI"), false);

        frame.add(tabbedPane);
        frame.setLocationRelativeTo(null); // center on screen
        frame.setVisible(true);
    }
}
//Compression.java file