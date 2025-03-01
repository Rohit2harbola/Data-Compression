package com.mycompany.compression.compression;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.PriorityQueue;

public class HuffmanCompression {

    private static final int R = 256; // Number of possible values (grayscale or ASCII characters)
    private static int[] freq;
    private static Node root;
    private static String[] codes;

    // Compress text using Huffman coding
    public static void compressText(String inputFilePath, String outputFilePath) throws IOException {
        String input = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(inputFilePath)));

        // Step 1: Calculate frequency
        freq = new int[R];
        for (char c : input.toCharArray()) freq[c]++;
        root = buildTrie();
        codes = new String[R];
        constructCodeTable(root, "");

        // Step 2: Encode input
        StringBuilder encodedData = new StringBuilder();
        for (char c : input.toCharArray()) encodedData.append(codes[c]);

        // Step 3: Write to file (frequency table + encoded data)
        try (FileOutputStream fos = new FileOutputStream(outputFilePath);
             DataOutputStream dos = new DataOutputStream(fos)) {
            for (int i = 0; i < R; i++) {
                dos.writeInt(freq[i]);
            }

            int encodedLength = encodedData.length();
            dos.writeInt(encodedLength);

            byte[] byteArray = new byte[(encodedLength + 7) / 8];
            for (int i = 0; i < encodedLength; i++) {
                int byteIndex = i / 8;
                int bitIndex = 7 - (i % 8);
                byteArray[byteIndex] |= (encodedData.charAt(i) == '1' ? 1 : 0) << bitIndex;
            }
            dos.write(byteArray);
        }
    }

    // Decompress text using Huffman coding
    public static void decompressText(String inputFilePath, String outputFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputFilePath);
             DataInputStream dis = new DataInputStream(fis)) {

            // Step 1: Read frequency table
            freq = new int[R];
            for (int i = 0; i < R; i++) {
                freq[i] = dis.readInt();
            }
            root = buildTrie();

            // Step 2: Read encoded data
            int encodedLength = dis.readInt();
            byte[] byteArray = dis.readNBytes((encodedLength + 7) / 8);
            StringBuilder encodedData = new StringBuilder();
            for (byte b : byteArray) {
                for (int i = 7; i >= 0; i--) {
                    if (encodedData.length() < encodedLength) {
                        encodedData.append(((b >> i) & 1) == 1 ? '1' : '0');
                    }
                }
            }

            // Step 3: Decode and write output
            String decompressedData = decompress(encodedData.toString());
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(decompressedData.getBytes());
            }
        }
    }

    // Compress image using Huffman coding
    public static void compressImage(String inputImagePath, String outputImagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(inputImagePath));
        if (image == null) throw new IOException("Failed to read image.");

        BufferedImage grayscaleImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int gray = (int) (0.2989 * ((pixel >> 16) & 0xFF) + 0.587 * ((pixel >> 8) & 0xFF) + 0.114 * (pixel & 0xFF));
                grayscaleImage.setRGB(x, y, (gray << 16) | (gray << 8) | gray);
            }
        }

        freq = new int[R];
        for (int y = 0; y < grayscaleImage.getHeight(); y++) {
            for (int x = 0; x < grayscaleImage.getWidth(); x++) {
                int pixel = grayscaleImage.getRGB(x, y) & 0xFF;
                freq[pixel]++;
            }
        }

        root = buildTrie();
        codes = new String[R];
        constructCodeTable(root, "");

        StringBuilder encodedData = new StringBuilder();
        for (int y = 0; y < grayscaleImage.getHeight(); y++) {
            for (int x = 0; x < grayscaleImage.getWidth(); x++) {
                int pixel = grayscaleImage.getRGB(x, y) & 0xFF;
                encodedData.append(codes[pixel]);
            }
        }

        try (FileOutputStream fos = new FileOutputStream(outputImagePath);
             DataOutputStream dos = new DataOutputStream(fos)) {
            for (int i = 0; i < R; i++) dos.writeInt(freq[i]);

            int encodedLength = encodedData.length();
            dos.writeInt(encodedLength);

            byte[] byteArray = new byte[(encodedLength + 7) / 8];
            for (int i = 0; i < encodedLength; i++) {
                int byteIndex = i / 8;
                int bitIndex = 7 - (i % 8);
                byteArray[byteIndex] |= (encodedData.charAt(i) == '1' ? 1 : 0) << bitIndex;
            }
            dos.write(byteArray);
        }
    }

    // Decompress image using Huffman coding
    public static void decompressImage(String inputImagePath, String outputImagePath, int width, int height) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputImagePath);
             DataInputStream dis = new DataInputStream(fis)) {

            freq = new int[R];
            for (int i = 0; i < R; i++) freq[i] = dis.readInt();
            root = buildTrie();

            int encodedLength = dis.readInt();
            byte[] byteArray = dis.readNBytes((encodedLength + 7) / 8);
            StringBuilder encodedData = new StringBuilder();
            for (byte b : byteArray) {
                for (int i = 7; i >= 0; i--) {
                    if (encodedData.length() < encodedLength) {
                        encodedData.append(((b >> i) & 1) == 1 ? '1' : '0');
                    }
                }
            }

            String decompressedData = decompress(encodedData.toString());
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            int index = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixelValue = decompressedData.charAt(index++) & 0xFF;
                    image.setRGB(x, y, (pixelValue << 16) | (pixelValue << 8) | pixelValue);
                }
            }

            ImageIO.write(image, "jpg", new File(outputImagePath));
        }
    }

    private static Node buildTrie() {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (int i = 0; i < R; ++i) {
            if (freq[i] > 0) pq.add(new Node((char) i, freq[i], null, null));
        }
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.add(parent);
        }
        return pq.poll();
    }

    private static void constructCodeTable(Node node, String s) {
        if (node.isLeaf()) {
            codes[node.ch] = s;
            return;
        }
        constructCodeTable(node.left, s + '0');
        constructCodeTable(node.right, s + '1');
    }

    private static String decompress(String input) {
        StringBuilder out = new StringBuilder();
        Node traveler = root;
        for (char c : input.toCharArray()) {
            traveler = (c == '1') ? traveler.right : traveler.left;
            if (traveler.isLeaf()) {
                out.append(traveler.ch);
                traveler = root;
            }
        }
        return out.toString();
    }

    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        public Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    public static void main(String[] args) {
        try {
            String inputImagePath = "resources/input_image.png";
            String compressedImagePath = "resources/compressed_image.bin";
            String decompressedImagePath = "resources/decompressed_image.jpg";

            compressImage(inputImagePath, compressedImagePath);
            decompressImage(compressedImagePath, decompressedImagePath, 300, 300);

            System.out.println("Image compression and decompression complete.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
