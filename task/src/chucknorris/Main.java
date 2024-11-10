package chucknorris;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please input operation (encode/decode/exit):");
            String operation = scanner.nextLine();

            switch (operation.toLowerCase()) {
                case "encode":
                    handleEncode(scanner);
                    break;
                case "decode":
                    handleDecode(scanner);
                    break;
                case "exit":
                    System.out.println("Bye!");
                    scanner.close();
                    return;
                default:
                    System.out.printf("There is no '%s' operation%n", operation);
            }
        }
    }

    private static void handleEncode(Scanner scanner) {
        System.out.println("Input string:");
        String input = scanner.nextLine();
        System.out.println("Encoded string:");
        System.out.println(encodeToChuckNorris(input));
    }

    private static void handleDecode(Scanner scanner) {
        System.out.println("Input encoded string:");
        String input = scanner.nextLine();

        if (!isValidEncoding(input)) {
            System.out.println("Encoded string is not valid.");
            return;
        }

        String decoded = decodeFromChuckNorris(input);
        System.out.println("Decoded string:");
        System.out.println(decoded);
    }

    private static boolean isValidEncoding(String encoded) {
        // Check if string contains only 0's and spaces
        if (!encoded.matches("^[0 ]+$")) {
            return false;
        }

        // Split into blocks and check if number of blocks is odd
        String[] blocks = encoded.split(" ");
        if (blocks.length % 2 != 0) {
            return false;
        }

        // Check if first block of each sequence is valid (0 or 00)
        for (int i = 0; i < blocks.length; i += 2) {
            if (!blocks[i].equals("0") && !blocks[i].equals("00")) {
                return false;
            }
        }

        // Convert to binary and check if length is multiple of 7
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < blocks.length; i += 2) {
            char bit = blocks[i].equals("0") ? '1' : '0';
            int count = blocks[i + 1].length();
            binary.append(String.valueOf(bit).repeat(count));
        }

        return binary.length() % 7 == 0;
    }

    public static String encodeToChuckNorris(String input) {
        // Convert string to binary (7 bits per character)
        StringBuilder binaryString = new StringBuilder();
        for (char c : input.toCharArray()) {
            String binary = String.format("%7s", Integer.toBinaryString(c)).replace(' ', '0');
            binaryString.append(binary);
        }

        // Convert binary to Chuck Norris code
        StringBuilder encoded = new StringBuilder();
        char currentBit = binaryString.charAt(0);
        int count = 1;

        for (int i = 1; i <= binaryString.length(); i++) {
            if (i == binaryString.length() || binaryString.charAt(i) != currentBit) {
                // Add prefix (0 for 1's, 00 for 0's)
                encoded.append(currentBit == '1' ? "0 " : "00 ");
                // Add sequence of zeros
                encoded.append("0".repeat(count)).append(" ");

                if (i < binaryString.length()) {
                    currentBit = binaryString.charAt(i);
                    count = 1;
                }
            } else {
                count++;
            }
        }

        return encoded.toString().trim();
    }

    public static String decodeFromChuckNorris(String encoded) {
        // Convert Chuck Norris code to binary
        StringBuilder binary = new StringBuilder();
        String[] blocks = encoded.split(" ");

        for (int i = 0; i < blocks.length; i += 2) {
            char bit = blocks[i].equals("0") ? '1' : '0';
            int count = blocks[i + 1].length();
            binary.append(String.valueOf(bit).repeat(count));
        }

        // Convert binary to text
        StringBuilder decoded = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 7) {
            String charBinary = binary.substring(i, i + 7);
            int charCode = Integer.parseInt(charBinary, 2);
            decoded.append((char) charCode);
        }

        return decoded.toString();
    }
}