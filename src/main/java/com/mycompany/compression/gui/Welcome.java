//Welcome.java is create on GUI folder
package com.mycompany.compression.gui;

import com.mycompany.compression.Compression;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

public class Welcome extends JFrame {
    private Image backgroundImage;

    public Welcome() {
        setTitle("Welcome to Compression And Decompression Using Hybrid Approach");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        // Load background image from resources
        try (InputStream is = getClass().getResourceAsStream("/login panel.png")) {
            if (is != null) {
                backgroundImage = ImageIO.read(is);
            } else {
                System.err.println("Background image not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel("Welcome to Compression And Decompression Using Hybrid Approach");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton getStartedButton = new JButton("GET STARTED NOW");
        getStartedButton.setBackground(new Color(255, 128, 0));
        getStartedButton.setForeground(Color.WHITE);
        getStartedButton.setFont(new Font("Arial", Font.BOLD, 16));
        getStartedButton.setFocusPainted(false);
        getStartedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        getStartedButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        getStartedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Compression.launchMainApplication();
                dispose();
            }
        });

        panel.add(Box.createVerticalStrut(60));
        panel.add(titleLabel);
        panel.add(Box.createVerticalGlue());
        panel.add(getStartedButton);
        panel.add(Box.createVerticalStrut(60));

        setContentPane(panel);
        setVisible(true);
    }
}