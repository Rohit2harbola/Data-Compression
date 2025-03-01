package com.mycompany.compression.compression;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.imageio.ImageIO;

public class BitStuffing {

    private static final String DESKTOP_PATH = System.getProperty("user.home") + File.separator + "Desktop";

    // Compression for text and images (PDF removed)
    public static void compress(String inputFilePath, String outputFileName) throws IOException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(DESKTOP_PATH + File.separator + outputFileName);

        String fileType = Files.probeContentType(inputFile.toPath());

        if (fileType != null && fileType.startsWith("text")) {
            compressText(inputFile, outputFile);
        } else if (fileType != null && fileType.startsWith("image")) {
            compressImage(inputFile, outputFile);
        } else {
            throw new UnsupportedOperationException("Unsupported file type: " + fileType);
        }
    }

    // Text file compression
    private static void compressText(File inputFile, File outputFile) throws IOException {
        String content = readFileAsString(inputFile);
        ArrayList<String> key = loadDictionary("src/main/resources/ascii.txt");
        ArrayList<String> values = loadValues("src/main/resources/ascii.txt");

        StringBuilder compressedContent = new StringBuilder();
        compress(content, key, values, compressedContent);

        String corruptedData = corruptData(compressedContent.toString());
        saveToFile(corruptedData, outputFile);
    }

    // Image compression
    private static void compressImage(File inputFile, File outputFile) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);
        if (image == null) {
            throw new IOException("Invalid image file: " + inputFile.getName());
        }

        // Write the compressed image as JPEG (reducing quality)
        try (OutputStream os = new FileOutputStream(outputFile)) {
            ImageIO.write(image, "jpg", os);
        }
    }

    // Implement `readFileAsString`
    private static String readFileAsString(File inputFile) throws IOException {
        return new String(Files.readAllBytes(inputFile.toPath()));
    }

    // Implement `loadDictionary` and `loadValues`
    private static ArrayList<String> loadDictionary(String filePath) throws IOException {
        ArrayList<String> dictionary = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.split(",")[0]);
            }
        }
        return dictionary;
    }

    private static ArrayList<String> loadValues(String filePath) throws IOException {
        ArrayList<String> values = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                values.add(line.split(",")[1]);
            }
        }
        return values;
    }

    // Implement `compress`
    public static void compress(String content, ArrayList<String> key, ArrayList<String> values, StringBuilder compressedContent) {
        for (char c : content.toCharArray()) {
            int index = key.indexOf(String.valueOf(c));
            compressedContent.append(index != -1 ? values.get(index) : c);
        }
    }

    // Implement `corruptData`
    private static String corruptData(String data) {
        return data.replace("0", "2"); // Example: Introduce intentional corruption
    }

    // Implement `decompress`
    public static String decompress(String content, ArrayList<String> key, ArrayList<String> values) {
        StringBuilder decompressed = new StringBuilder();
        for (int i = 0; i < content.length(); ) {
            boolean found = false;
            for (String value : values) {
                if (content.startsWith(value, i)) {
                    decompressed.append(key.get(values.indexOf(value)));
                    i += value.length();
                    found = true;
                    break;
                }
            }
            if (!found) {
                decompressed.append(content.charAt(i));
                i++;
            }
        }
        return decompressed.toString();
    }

    // Save content to file
    private static void saveToFile(String content, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }
}
