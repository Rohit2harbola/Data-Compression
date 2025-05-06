package com.mycompany.compression.gui;

import com.mycompany.compression.compression.HuffmanCompression;
import com.mycompany.compression.compression.BitStuffing;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class CompressionGUI extends JPanel {
    private JTextArea inputArea, outputArea;
    private JButton compressHuffmanButton, decompressHuffmanButton, compressBitStuffingButton, decompressBitStuffingButton, logoutButton;
    private JFileChooser fileChooser;
    private File selectedFile;

    private JTabbedPane tabbedPane;
    private int userManagementTabIndex;

    public CompressionGUI(JTabbedPane tabbedPane, int userManagementTabIndex) {
        this.tabbedPane = tabbedPane;
        this.userManagementTabIndex = userManagementTabIndex;

        setLayout(new BorderLayout());

        inputArea = new JTextArea(10, 50);
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);

        compressHuffmanButton = new JButton("Compress (Huffman)");
        decompressHuffmanButton = new JButton("Decompress (Huffman)");
        compressBitStuffingButton = new JButton("Compress (BitStuffing)");
        decompressBitStuffingButton = new JButton("Decompress (BitStuffing)");
        logoutButton = new JButton("Logout");

        fileChooser = new JFileChooser();

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(new JScrollPane(inputArea));
        textPanel.add(new JScrollPane(outputArea));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.add(compressHuffmanButton);
        buttonPanel.add(decompressHuffmanButton);
        buttonPanel.add(compressBitStuffingButton);
        buttonPanel.add(decompressBitStuffingButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Input and Output Areas:"), BorderLayout.WEST);
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        compressHuffmanButton.addActionListener(new CompressionActionListener(CompressionType.HUFFMAN, true));
        decompressHuffmanButton.addActionListener(new CompressionActionListener(CompressionType.HUFFMAN, false));
        compressBitStuffingButton.addActionListener(new CompressionActionListener(CompressionType.BITSTUFFING, true));
        decompressBitStuffingButton.addActionListener(new CompressionActionListener(CompressionType.BITSTUFFING, false));

        logoutButton.addActionListener(new LogoutActionListener());
    }

    private class LogoutActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tabbedPane.setSelectedIndex(userManagementTabIndex);
            }
        }
    }

    private class CompressionActionListener implements ActionListener {
        private final CompressionType type;
        private final boolean isCompression;
        private BitStuffing bitStuffing;

        CompressionActionListener(CompressionType type, boolean isCompression) {
            this.type = type;
            this.isCompression = isCompression;
            this.bitStuffing = new BitStuffing();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int userChoice = fileChooser.showOpenDialog(CompressionGUI.this);
            if (userChoice == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                if (selectedFile == null || !selectedFile.exists()) {
                    JOptionPane.showMessageDialog(null, "Please select a valid file!");
                    return;
                }

                try {
                    String result = isCompression ? compress(selectedFile) : decompress(selectedFile);
                    outputArea.setText(result);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        }

        private String compress(File inputFile) throws IOException {
            String outputFilePath = inputFile.getParent() + File.separator + "compressed_" + inputFile.getName();

            File outputFile = new File(outputFilePath);
            if (outputFile.exists()) {
                int confirm = JOptionPane.showConfirmDialog(null, "File already exists. Do you want to overwrite?", "Overwrite File", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return "Compression aborted!";
                }
            }

            if (type == CompressionType.HUFFMAN) {
                HuffmanCompression.compressText(inputFile.getAbsolutePath(), outputFilePath);
            } else {
                BitStuffing.compress(inputFile.getAbsolutePath(), outputFilePath);
            }
            return "Compressed file saved at: " + outputFilePath;
        }

        private String decompress(File inputFile) throws IOException {
            String outputFilePath = inputFile.getParent() + File.separator + "decompressed_" + inputFile.getName();

            File outputFile = new File(outputFilePath);
            if (outputFile.exists()) {
                int confirm = JOptionPane.showConfirmDialog(null, "File already exists. Do you want to overwrite?", "Overwrite File", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return "Decompression aborted!";
                }
            }

            if (type == CompressionType.HUFFMAN) {
                HuffmanCompression.decompressText(inputFile.getAbsolutePath(), outputFilePath);
            } else {
                ArrayList<String> dictionary = new ArrayList<>();
                ArrayList<String> outputList = new ArrayList<>();

        //        bitStuffing.decompress(inputFile.getAbsolutePath(), dictionary, outputList);
                saveToFile(outputFilePath, outputList);
            }
            return "Decompressed file saved at: " + outputFilePath;
        }

        private void saveToFile(String filePath, ArrayList<String> outputList) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : outputList) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }

    private enum CompressionType {
        HUFFMAN,
        BITSTUFFING
    }
}
