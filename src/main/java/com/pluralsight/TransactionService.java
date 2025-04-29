package com.pluralsight;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionService {

    private static final String FILE_PATH = "transactions.csv";
    private static Scanner scanner = new Scanner(System.in);

    public static void homeScreen() {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Home Screen ---");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger (Coming soon)");
            System.out.println("X) Exit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    System.out.println("Ledger screen coming soon...");
                    break;
                case "X":
                    running = false;
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    private static void addDeposit() {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        String date = LocalDate.now().toString();
        String time = LocalTime.now().withNano(0).toString();

        Transaction transaction = new Transaction(date, time, description, vendor, amount);
        saveTransaction(transaction);

        System.out.println("Deposit added successfully!");
    }

    private static void makePayment() {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        amount = -Math.abs(amount); // Payment = negatif değer

        String date = LocalDate.now().toString();
        String time = LocalTime.now().withNano(0).toString();

        Transaction transaction = new Transaction(date, time, description, vendor, amount);
        saveTransaction(transaction);

        System.out.println("Payment added successfully!");
    }

    private static void saveTransaction(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(transaction.toCSV());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }
}