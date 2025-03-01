package com.mycompany.compression;

import com.mycompany.compression.gui.LoginPanel;
import com.mycompany.compression.gui.UserManagementPanel;
import com.mycompany.compression.gui.CompressionGUI;

import javax.swing.*;

public class Compression {
    public static void main(String[] args) {
        // Create the main frame for the application
        JFrame frame = new JFrame("Compression Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create a JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create panels
        UserManagementPanel userManagementPanel = new UserManagementPanel();
//        CompressionGUI compressionGUIPanel = new CompressionGUI(tabbedPane, userManagementPanel);
CompressionGUI compressionGUIPanel = new CompressionGUI(tabbedPane, tabbedPane.indexOfTab("User Management"));


        // Create the login panel and pass the tabbedPane reference
        LoginPanel loginPanel = new LoginPanel(tabbedPane, compressionGUIPanel, userManagementPanel);

        // Add panels to the JTabbedPane
        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("User Management", userManagementPanel);

        // Initially hide the Compression GUI tab
        tabbedPane.addTab("Compression GUI", compressionGUIPanel);
        tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Compression GUI"), false);

        // Add JTabbedPane to the frame
        frame.add(tabbedPane);

        // Make the frame visible
        frame.setVisible(true);
    }
}
