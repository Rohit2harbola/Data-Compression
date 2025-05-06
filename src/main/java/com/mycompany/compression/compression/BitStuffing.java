//package com.mycompany.compression.compression;
//
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.nio.file.*;
//import java.util.*;
//import javax.imageio.ImageIO;
//
//public class BitStuffing {
//
//    private static final String DESKTOP_PATH = System.getProperty("user.home") + File.separator + "Desktop";
//
//    // Compression for text and images
//    public static void compress(String inputFilePath, String outputFileName) throws IOException {
//        File inputFile = new File(inputFilePath);
//        File outputFile = new File(DESKTOP_PATH, new File(outputFileName).getName());
//
//        String fileType = Files.probeContentType(inputFile.toPath());
//
//        if (fileType != null && fileType.startsWith("text")) {
//            compressText(inputFile, outputFile);
//        } else if (fileType != null && fileType.startsWith("image")) {
//            compressImage(inputFile, outputFile);
//        } else {
//            throw new UnsupportedOperationException("Unsupported file type: " + fileType);
//        }
//    }
//
//    // Decompression for text and images
//    public static void decompress(String inputFilePath, String outputFileName) throws IOException {
//        File inputFile = new File(inputFilePath);
//        File outputFile = new File(DESKTOP_PATH, new File(outputFileName).getName());
//
//        String fileType = Files.probeContentType(inputFile.toPath());
//
//        if (fileType != null && fileType.startsWith("text")) {
//            decompressText(inputFile, outputFile);
//        } else if (fileType != null && fileType.startsWith("image")) {
//            decompressImage(inputFile, outputFile);
//        } else {
//            throw new UnsupportedOperationException("Unsupported file type: " + fileType);
//        }
//    }
//
//    // 🟢 TEXT FILE COMPRESSION FIXED
//    private static void compressText(File inputFile, File outputFile) throws IOException {
//        String content = readFileAsString(inputFile);
//
//        // Load dictionary (Ensure it's correctly formatted: "char,encoded_value")
//        Map<String, String> dictionary = loadDictionary("src/main/resources/ascii.txt");
//
//        if (dictionary.isEmpty()) {
//            throw new IOException("Dictionary file is empty or missing!");
//        }
//
//        StringBuilder compressedContent = new StringBuilder();
//        for (char c : content.toCharArray()) {
//            String encoded = dictionary.getOrDefault(String.valueOf(c), String.valueOf(c));
//            compressedContent.append(encoded);
//        }
//
//        String corruptedData = corruptData(compressedContent.toString());
//        saveToFile(corruptedData, outputFile);
//    }
//
//    // 🟢 IMAGE COMPRESSION FIXED
//    private static void compressImage(File inputFile, File outputFile) throws IOException {
//        BufferedImage image = ImageIO.read(inputFile);
//        if (image == null) {
//            throw new IOException("Invalid image file: " + inputFile.getName());
//        }
//
//        // Write the compressed image as JPEG (reducing quality)
//        try (OutputStream os = new FileOutputStream(outputFile)) {
//            ImageIO.write(image, "jpg", os);
//        }
//    }
//
//    // 🟢 TEXT FILE DECOMPRESSION
//    private static void decompressText(File inputFile, File outputFile) throws IOException {
//        String content = readFileAsString(inputFile);
//        Map<String, String> dictionary = loadDictionary("src/main/resources/ascii.txt");
//        
//        if (dictionary.isEmpty()) {
//            throw new IOException("Dictionary file is empty or missing!");
//        }
//
//        String decompressedContent = decompress(content, dictionary);
//        saveToFile(decompressedContent, outputFile);
//    }
//
//    // 🟢 IMAGE DECOMPRESSION
//  // ✅ IMAGE DECOMPRESSION USING ORIGINAL FORMAT STORED AS METADATA
//private static void decompressImage(File inputFile, File outputFile) throws IOException {
//    // This assumes the image is simply being copied back to simulate decompression.
//    // For actual decompression, you must store the original format or use reversible algorithms.
//    Files.copy(inputFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//}
//
//
//    // 🟢 READ FILE CONTENT AS STRING
//    private static String readFileAsString(File inputFile) throws IOException {
//        return new String(Files.readAllBytes(inputFile.toPath()));
//    }
//
//    // 🟢 LOAD DICTIONARY (CHARACTER → ENCODED VALUE MAPPING)
//    private static Map<String, String> loadDictionary(String filePath) throws IOException {
//        Map<String, String> dictionary = new HashMap<>();
//
//        File dictionaryFile = new File(filePath);
//        if (!dictionaryFile.exists()) {
//            throw new IOException("Dictionary file not found: " + filePath);
//        }
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length == 2) {
//                    dictionary.put(parts[0], parts[1]); // Key = Character, Value = Encoded Value
//                }
//            }
//        }
//        return dictionary;
//    }
//
//    // 🟢 INTENTIONAL CORRUPTION FUNCTION
//    private static String corruptData(String data) {
//        return data.replace("0", "2"); // Example: Introduce intentional corruption
//    }
//
//    // 🟢 DECOMPRESSION FUNCTION
//    public static String decompress(String content, Map<String, String> dictionary) {
//        // Reverse dictionary mapping (Encoded Value → Original Character)
//        Map<String, String> reverseDictionary = new HashMap<>();
//        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
//            reverseDictionary.put(entry.getValue(), entry.getKey());
//        }
//
//        StringBuilder decompressed = new StringBuilder();
//        StringBuilder currentCode = new StringBuilder();
//
//        for (char c : content.toCharArray()) {
//            currentCode.append(c);
//            if (reverseDictionary.containsKey(currentCode.toString())) {
//                decompressed.append(reverseDictionary.get(currentCode.toString()));
//                currentCode.setLength(0);
//            }
//        }
//
//        return decompressed.toString();
//    }
//
//    // 🟢 SAVE CONTENT TO FILE
//    public static void saveToFile(String content, File file) throws IOException {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//            writer.write(content);
//        }
//    }
//
//    public void decompress(String absolutePath, ArrayList<String> dictionary, ArrayList<String> outputList) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//}






//
//package com.mycompany.compression.compression;
//
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.nio.file.*;
//import java.util.*;
//import javax.imageio.*;
//import javax.imageio.stream.ImageOutputStream;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.Color;
//
//public class BitStuffing {
//    private static final String DESKTOP_PATH = System.getProperty("user.home") + File.separator + "Desktop";
//    private static final float COMPRESSION_QUALITY = 0.8f; // Increased compression quality
//
//    // Compression for text and images
//    public static void compress(String inputFilePath, String outputFileName) throws IOException {
//        File inputFile = new File(inputFilePath);
//        File outputFile = new File(DESKTOP_PATH, new File(outputFileName).getName());
//
//        String fileType = Files.probeContentType(inputFile.toPath());
//
//        if (fileType != null && fileType.startsWith("text")) {
//            compressText(inputFile, outputFile);
//        } else if (fileType != null && fileType.startsWith("image")) {
//            compressImage(inputFile, outputFile);
//        } else {
//            throw new UnsupportedOperationException("Unsupported file type: " + fileType);
//        }
//    }
//
//    // Decompression for text and images
//    public static void decompress(String inputFilePath, String outputFileName) throws IOException {
//        File inputFile = new File(inputFilePath);
//        File outputFile = new File(DESKTOP_PATH, new File(outputFileName).getName());
//
//        String fileType = Files.probeContentType(inputFile.toPath());
//
//        if (fileType != null && fileType.startsWith("text")) {
//            decompressText(inputFile, outputFile);
//        } else if (fileType != null && fileType.startsWith("image")) {
//            decompressImage(inputFile, outputFile);
//        } else {
//            throw new UnsupportedOperationException("Unsupported file type: " + fileType);
//        }
//    }
//
//    // Text file compression
//    private static void compressText(File inputFile, File outputFile) throws IOException {
//        String content = readFileAsString(inputFile);
//        Map<String, String> dictionary = loadDictionary("src/main/resources/ascii.txt");
//        
//        if (dictionary.isEmpty()) {
//            throw new IOException("Dictionary file is empty or missing!");
//        }
//
//        StringBuilder compressedContent = new StringBuilder();
//        for (char c : content.toCharArray()) {
//            String encoded = dictionary.getOrDefault(String.valueOf(c), String.valueOf(c));
//            compressedContent.append(encoded);
//        }
//
//        String corruptedData = corruptData(compressedContent.toString());
//        saveToFile(corruptedData, outputFile);
//    }
//
//    // Improved image compression
//    private static void compressImage(File inputFile, File outputFile) throws IOException {
//        // Read the original image
//        BufferedImage originalImage = ImageIO.read(inputFile);
//        if (originalImage == null) {
//            throw new IOException("Invalid image file: " + inputFile.getName());
//        }
//
//        // Create a new image with the same dimensions and type
//        BufferedImage compressedImage = new BufferedImage(
//            originalImage.getWidth(),
//            originalImage.getHeight(),
//            originalImage.getType() == 0 ? BufferedImage.TYPE_INT_RGB : originalImage.getType()
//        );
//
//        // Draw the original image onto the new image with better quality settings
//        Graphics2D g2d = compressedImage.createGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
//        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//        g2d.drawImage(originalImage, 0, 0, null);
//        g2d.dispose();
//
//        // Get JPEG image writer
//        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
//        if (!writers.hasNext()) {
//            throw new IOException("No JPEG image writer found");
//        }
//        ImageWriter writer = writers.next();
//
//        // Set up the output file
//        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile)) {
//            writer.setOutput(ios);
//
//            // Set compression quality
//            ImageWriteParam param = writer.getDefaultWriteParam();
//            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//            param.setCompressionQuality(COMPRESSION_QUALITY);
//
//            // Write the image
//            writer.write(null, new IIOImage(compressedImage, null, null), param);
//        }
//        writer.dispose();
//    }
//
//    // Text file decompression
//    private static void decompressText(File inputFile, File outputFile) throws IOException {
//        String content = readFileAsString(inputFile);
//        Map<String, String> dictionary = loadDictionary("src/main/resources/ascii.txt");
//        
//        if (dictionary.isEmpty()) {
//            throw new IOException("Dictionary file is empty or missing!");
//        }
//
//        String decompressedContent = decompress(content, dictionary);
//        saveToFile(decompressedContent, outputFile);
//    }
//
//    // Improved image decompression
//    private static void decompressImage(File inputFile, File outputFile) throws IOException {
//        // Read the compressed image
//        BufferedImage compressedImage = ImageIO.read(inputFile);
//        if (compressedImage == null) {
//            throw new IOException("Invalid compressed image file: " + inputFile.getName());
//        }
//
//        // Create a new image with the same dimensions and type
//        BufferedImage decompressedImage = new BufferedImage(
//            compressedImage.getWidth(),
//            compressedImage.getHeight(),
//            compressedImage.getType() == 0 ? BufferedImage.TYPE_INT_RGB : compressedImage.getType()
//        );
//
//        // Draw the compressed image onto the new image with better quality settings
//        Graphics2D g2d = decompressedImage.createGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
//        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//        g2d.drawImage(compressedImage, 0, 0, null);
//        g2d.dispose();
//
//        // Write the decompressed image with high quality
//        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
//        if (!writers.hasNext()) {
//            throw new IOException("No JPEG image writer found");
//        }
//        ImageWriter writer = writers.next();
//
//        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile)) {
//            writer.setOutput(ios);
//
//            // Set high quality for decompression
//            ImageWriteParam param = writer.getDefaultWriteParam();
//            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//            param.setCompressionQuality(0.95f); // Very high quality for decompressed image
//
//            // Write the image
//            writer.write(null, new IIOImage(decompressedImage, null, null), param);
//        }
//        writer.dispose();
//    }
//
//    // Read file content as string
//    private static String readFileAsString(File inputFile) throws IOException {
//        return new String(Files.readAllBytes(inputFile.toPath()));
//    }
//
//    // Load dictionary
//    private static Map<String, String> loadDictionary(String filePath) throws IOException {
//        Map<String, String> dictionary = new HashMap<>();
//        File dictionaryFile = new File(filePath);
//        
//        if (!dictionaryFile.exists()) {
//            throw new IOException("Dictionary file not found: " + filePath);
//        }
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length == 2) {
//                    dictionary.put(parts[0], parts[1]);
//                }
//            }
//        }
//        return dictionary;
//    }
//
//    // Corruption function
//    private static String corruptData(String data) {
//        return data.replace("0", "2");
//    }
//
//    // Decompression function
//    public static String decompress(String content, Map<String, String> dictionary) {
//        Map<String, String> reverseDictionary = new HashMap<>();
//        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
//            reverseDictionary.put(entry.getValue(), entry.getKey());
//        }
//
//        StringBuilder decompressed = new StringBuilder();
//        StringBuilder currentCode = new StringBuilder();
//
//        for (char c : content.toCharArray()) {
//            currentCode.append(c);
//            if (reverseDictionary.containsKey(currentCode.toString())) {
//                decompressed.append(reverseDictionary.get(currentCode.toString()));
//                currentCode.setLength(0);
//            }
//        }
//
//        return decompressed.toString();
//    }
//
//    // Save content to file
//    public static void saveToFile(String content, File file) throws IOException {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//            writer.write(content);
//        }
//    }
//} 


//zip method which can compress and decompress the imagefile (.png,.jpg etc)

//
//package com.mycompany.compression.compression;
//
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.nio.file.*;
//import java.util.*;
//import java.util.zip.*;
//import javax.imageio.*;
//import javax.imageio.stream.ImageOutputStream;
//
//public class BitStuffing {
//
//    private static final String DESKTOP_PATH = System.getProperty("user.home") + File.separator + "Desktop";
//
//    public static void compress(String inputFilePath, String outputFileName) throws IOException {
//        File inputFile = new File(inputFilePath);
//        File outputFile = new File(DESKTOP_PATH, new File(outputFileName).getName() + ".zip");
//
//        String fileType = Files.probeContentType(inputFile.toPath());
//
//        if (fileType != null && fileType.startsWith("text")) {
//            compressText(inputFile, outputFile);
//        } else if (fileType != null && fileType.startsWith("image")) {
//            compressImage(inputFile, outputFile);
//        } else {
//            throw new UnsupportedOperationException("Unsupported file type: " + fileType);
//        }
//    }
//
//    public static void decompress(String inputFilePath, String outputFileName) throws IOException {
//        File inputFile = new File(inputFilePath);
//        File outputFile = new File(DESKTOP_PATH, new File(outputFileName).getName());
//
//        String fileType = guessContentTypeFromZip(inputFile);
//
//        if (fileType != null && fileType.startsWith("text")) {
//            decompressText(inputFile, outputFile);
//        } else if (fileType != null && fileType.startsWith("image")) {
//            decompressImage(inputFile, outputFile);
//        } else {
//            throw new UnsupportedOperationException("Unsupported file type for decompression: " + fileType);
//        }
//    }
//
//    // TEXT COMPRESSION
//    private static void compressText(File inputFile, File outputZipFile) throws IOException {
//        String content = readFileAsString(inputFile);
//        Map<String, String> dictionary = loadDictionary("src/main/resources/ascii.txt");
//
//        if (dictionary.isEmpty()) {
//            throw new IOException("Dictionary file is empty or missing!");
//        }
//
//        StringBuilder compressedContent = new StringBuilder();
//        for (char c : content.toCharArray()) {
//            String encoded = dictionary.getOrDefault(String.valueOf(c), String.valueOf(c));
//            compressedContent.append(encoded);
//        }
//
//        String corruptedData = corruptData(compressedContent.toString());
//
//        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputZipFile))) {
//            ZipEntry entry = new ZipEntry(inputFile.getName());
//            zos.putNextEntry(entry);
//            zos.write(corruptedData.getBytes());
//            zos.closeEntry();
//        }
//    }
//
//    // IMAGE COMPRESSION (JPEG re-encoding)
//    private static void compressImage(File inputFile, File outputZipFile) throws IOException {
//        BufferedImage image = ImageIO.read(inputFile);
//        if (image == null) {
//            throw new IOException("Invalid image file: " + inputFile.getName());
//        }
//
//        File tempJpegFile = File.createTempFile("compressed_", ".jpg");
//        try (FileOutputStream fos = new FileOutputStream(tempJpegFile)) {
//            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
//            if (!writers.hasNext()) throw new IllegalStateException("No JPEG writers found");
//
//            ImageWriter writer = writers.next();
//            ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
//            writer.setOutput(ios);
//
//            ImageWriteParam param = writer.getDefaultWriteParam();
//            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//            param.setCompressionQuality(0.3f); // 0 = max compression, 1 = best quality
//
//            writer.write(null, new IIOImage(image, null, null), param);
//            ios.close();
//            writer.dispose();
//        }
//
//        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputZipFile))) {
//            ZipEntry entry = new ZipEntry("compressed_image.jpg");
//            zos.putNextEntry(entry);
//            Files.copy(tempJpegFile.toPath(), zos);
//            zos.closeEntry();
//        }
//
//        tempJpegFile.delete();
//    }
//
//    // TEXT DECOMPRESSION
//    private static void decompressText(File zipFile, File outputFile) throws IOException {
//        Map<String, String> dictionary = loadDictionary("src/main/resources/ascii.txt");
//
//        if (dictionary.isEmpty()) {
//            throw new IOException("Dictionary file is empty or missing!");
//        }
//
//        String compressedContent = extractZipContentAsString(zipFile);
//        String decompressedContent = decompress(compressedContent, dictionary);
//        saveToFile(decompressedContent, outputFile);
//    }
//
//    // IMAGE DECOMPRESSION
//    private static void decompressImage(File zipFile, File outputFile) throws IOException {
//        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
//            ZipEntry entry = zis.getNextEntry();
//            if (entry != null) {
//                Files.copy(zis, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//            } else {
//                throw new IOException("No entry found in ZIP for image.");
//            }
//        }
//    }
//
//    // LOAD DICTIONARY
//    private static Map<String, String> loadDictionary(String filePath) throws IOException {
//        Map<String, String> dictionary = new HashMap<>();
//        File dictionaryFile = new File(filePath);
//
//        if (!dictionaryFile.exists()) {
//            throw new IOException("Dictionary file not found: " + filePath);
//        }
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length == 2) {
//                    dictionary.put(parts[0], parts[1]);
//                }
//            }
//        }
//        return dictionary;
//    }
//
//    // CORRUPT DATA
//    private static String corruptData(String data) {
//        return data.replace("0", "2");
//    }
//
//    // DECOMPRESSION
//    public static String decompress(String content, Map<String, String> dictionary) {
//        Map<String, String> reverseDict = new HashMap<>();
//        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
//            reverseDict.put(entry.getValue(), entry.getKey());
//        }
//
//        StringBuilder decompressed = new StringBuilder();
//        StringBuilder currentCode = new StringBuilder();
//
//        for (char c : content.toCharArray()) {
//            currentCode.append(c);
//            if (reverseDict.containsKey(currentCode.toString())) {
//                decompressed.append(reverseDict.get(currentCode.toString()));
//                currentCode.setLength(0);
//            }
//        }
//
//        return decompressed.toString();
//    }
//
//    private static String readFileAsString(File inputFile) throws IOException {
//        return new String(Files.readAllBytes(inputFile.toPath()));
//    }
//
//    private static String extractZipContentAsString(File zipFile) throws IOException {
//        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
//            ZipEntry entry = zis.getNextEntry();
//            if (entry != null) {
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = zis.read(buffer)) > 0) {
//                    baos.write(buffer, 0, length);
//                }
//                return baos.toString();
//            }
//        }
//        return "";
//    }
//
//    private static String guessContentTypeFromZip(File zipFile) throws IOException {
//        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
//            ZipEntry entry = zis.getNextEntry();
//            if (entry != null) {
//                String name = entry.getName();
//                return Files.probeContentType(Paths.get(name));
//            }
//        }
//        return null;
//    }
//
//    public static void saveToFile(String content, File file) throws IOException {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//            writer.write(content);
//        }
//    }
//
//    public void decompress(String absolutePath, ArrayList<String> dictionary, ArrayList<String> outputList) {
//        throw new UnsupportedOperationException("This overload is no longer used.");
//    }
//}



//trial212

package com.mycompany.compression.compression;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;
import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;

public class BitStuffing {

    private static final String DESKTOP_PATH = System.getProperty("user.home") + File.separator + "Desktop";

    public static void compress(String inputFilePath, String outputFileName) throws IOException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(DESKTOP_PATH, new File(outputFileName).getName() + ".zip");

        String fileType = Files.probeContentType(inputFile.toPath());

        if (fileType != null && fileType.startsWith("text")) {
            compressText(inputFile, outputFile);
        } else if (fileType != null && fileType.startsWith("image")) {
            compressImage(inputFile, outputFile);
        } else {
            throw new UnsupportedOperationException("Unsupported file type: " + fileType);
        }
    }

    public static void decompress(String inputFilePath, String outputFileName) throws IOException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(DESKTOP_PATH, new File(outputFileName).getName());

        String fileType = guessContentTypeFromZip(inputFile);

        if (fileType != null && fileType.startsWith("text")) {
            decompressText(inputFile, outputFile);
        } else if (fileType != null && fileType.startsWith("image")) {
            decompressImage(inputFile, outputFile);
        } else {
            throw new UnsupportedOperationException("Unsupported file type for decompression: " + fileType);
        }
    }

    // TEXT COMPRESSION
    private static void compressText(File inputFile, File outputZipFile) throws IOException {
        String content = readFileAsString(inputFile);
        Map<String, String> dictionary = loadDictionary("src/main/resources/ascii.txt");

        if (dictionary.isEmpty()) {
            throw new IOException("Dictionary file is empty or missing!");
        }

        StringBuilder compressedContent = new StringBuilder();
        for (char c : content.toCharArray()) {
            String encoded = dictionary.getOrDefault(String.valueOf(c), String.valueOf(c));
            compressedContent.append(encoded);
        }

        String corruptedData = corruptData(compressedContent.toString());

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputZipFile))) {
            ZipEntry entry = new ZipEntry(inputFile.getName());
            zos.putNextEntry(entry);
            zos.write(corruptedData.getBytes());
            zos.closeEntry();
        }
    }

    // IMAGE COMPRESSION (JPEG re-encoding)
    private static void compressImage(File inputFile, File outputZipFile) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);
        if (image == null) {
            throw new IOException("Invalid image file: " + inputFile.getName());
        }

        File tempJpegFile = File.createTempFile("compressed_", ".jpg");
        try (FileOutputStream fos = new FileOutputStream(tempJpegFile)) {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) throw new IllegalStateException("No JPEG writers found");

            ImageWriter writer = writers.next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.3f); // 0 = max compression, 1 = best quality

            writer.write(null, new IIOImage(image, null, null), param);
            ios.close();
            writer.dispose();
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputZipFile))) {
            ZipEntry entry = new ZipEntry("compressed_image.jpg");
            zos.putNextEntry(entry);
            Files.copy(tempJpegFile.toPath(), zos);
            zos.closeEntry();
        }

        tempJpegFile.delete();
    }

    // TEXT DECOMPRESSION
    private static void decompressText(File zipFile, File outputFile) throws IOException {
        Map<String, String> dictionary = loadDictionary("src/main/resources/ascii.txt");

        if (dictionary.isEmpty()) {
            throw new IOException("Dictionary file is empty or missing!");
        }

        String compressedContent = extractZipContentAsString(zipFile);
        String decompressedContent = decompress(compressedContent, dictionary);
        saveToFile(decompressedContent, outputFile);
    }

    // FIXED IMAGE DECOMPRESSION
   private static void decompressImage(File zipFile, File outputFile) throws IOException {
    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
        ZipEntry entry = zis.getNextEntry();
        if (entry != null) {
            // Ensure .jpg extension
            if (!outputFile.getName().toLowerCase().endsWith(".jpg")) {
                outputFile = new File(outputFile.getParent(), outputFile.getName() + ".jpg");
            }

            // Read from ZIP and write to file properly
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = zis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("Image decompressed successfully: " + outputFile.getAbsolutePath());
        } else {
            throw new IOException("No entries found in the ZIP file.");
        }
    }
}


    // LOAD DICTIONARY
    private static Map<String, String> loadDictionary(String filePath) throws IOException {
        Map<String, String> dictionary = new HashMap<>();
        File dictionaryFile = new File(filePath);

        if (!dictionaryFile.exists()) {
            throw new IOException("Dictionary file not found: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    dictionary.put(parts[0], parts[1]);
                }
            }
        }
        return dictionary;
    }

    // CORRUPT DATA
    private static String corruptData(String data) {
        return data.replace("0", "2");
    }

    // DECOMPRESSION
    public static String decompress(String content, Map<String, String> dictionary) {
        Map<String, String> reverseDict = new HashMap<>();
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            reverseDict.put(entry.getValue(), entry.getKey());
        }

        StringBuilder decompressed = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();

        for (char c : content.toCharArray()) {
            currentCode.append(c);
            if (reverseDict.containsKey(currentCode.toString())) {
                decompressed.append(reverseDict.get(currentCode.toString()));
                currentCode.setLength(0);
            }
        }

        return decompressed.toString();
    }

    private static String readFileAsString(File inputFile) throws IOException {
        return new String(Files.readAllBytes(inputFile.toPath()));
    }

    private static String extractZipContentAsString(File zipFile) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = zis.getNextEntry();
            if (entry != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, length);
                }
                return baos.toString();
            }
        }
        return "";
    }

    private static String guessContentTypeFromZip(File zipFile) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = zis.getNextEntry();
            if (entry != null) {
                String name = entry.getName();
                return Files.probeContentType(Paths.get(name));
            }
        }
        return null;
    }

    public static void saveToFile(String content, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }

    public void decompress(String absolutePath, ArrayList<String> dictionary, ArrayList<String> outputList) {
        throw new UnsupportedOperationException("This overload is no longer used.");
    }
}
