package com.pluralsight;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionService {

    private static final String FILE_PATH = "src/main/resources/transactions.csv";
    private static Scanner scanner = new Scanner(System.in);

    public static void homeScreen() {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Home Screen ---");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
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
                    ledgerScreen();
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
        double amount = -Math.abs(Double.parseDouble(scanner.nextLine())); // negatif

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

    private static List<Transaction> readTransactions() {
        List<Transaction> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    Transaction t = new Transaction(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4]));
                    list.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }
        Collections.reverse(list); // en yeni en üstte
        return list;
    }

    private static void ledgerScreen() {
        boolean inLedger = true;
        while (inLedger) {
            System.out.println("\n--- Ledger ---");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Select an option: ");
            String option = scanner.nextLine().toUpperCase();

            List<Transaction> transactions = readTransactions();

            switch (option) {
                case "A":
                    printTransactions(transactions);
                    break;
                case "D":
                    printTransactions(filterByType(transactions, true));
                    break;
                case "P":
                    printTransactions(filterByType(transactions, false));
                    break;
                case "R":
                    reportScreen();
                    break;
                case "H":
                    inLedger = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static List<Transaction> filterByType(List<Transaction> transactions, boolean isDeposit) {
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : transactions) {
            if (isDeposit && t.getAmount() > 0) {
                filtered.add(t);
            } else if (!isDeposit && t.getAmount() < 0) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    private static void printTransactions(List<Transaction> transactions) {
        System.out.println("Date       | Time     | Description       | Vendor         | Amount");
        System.out.println("---------------------------------------------------------------------");
        for (Transaction t : transactions) {
            System.out.printf("%s | %s | %-17s | %-14s | %.2f%n",
                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
    }

    private static void reportScreen() {
        List<Transaction> all = readTransactions();
        boolean inReports = true;

        while (inReports) {
            System.out.println("\n--- Reports ---");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Select an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    printTransactions(filterByDate(all, "monthToDate"));
                    break;
                case "2":
                    printTransactions(filterByDate(all, "previousMonth"));
                    break;
                case "3":
                    printTransactions(filterByDate(all, "yearToDate"));
                    break;
                case "4":
                    printTransactions(filterByDate(all, "previousYear"));
                    break;
                case "5":
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine().toLowerCase();
                    printTransactions(all.stream()
                            .filter(t -> t.getVendor().toLowerCase().contains(vendor))
                            .toList());
                    break;
                case "0":
                    inReports = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static List<Transaction> filterByDate(List<Transaction> list, String type) {
        List<Transaction> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Transaction t : list) {
            LocalDate date = LocalDate.parse(t.getDate());
            switch (type) {
                case "monthToDate":
                    if (date.getMonth() == today.getMonth() && date.getYear() == today.getYear())
                        result.add(t);
                    break;
                case "previousMonth":
                    LocalDate prevMonth = today.minusMonths(1);
                    if (date.getMonth() == prevMonth.getMonth() && date.getYear() == prevMonth.getYear())
                        result.add(t);
                    break;
                case "yearToDate":
                    if (date.getYear() == today.getYear())
                        result.add(t);
                    break;
                case "previousYear":
                    if (date.getYear() == today.getYear() - 1)
                        result.add(t);
                    break;
            }
        }
        return result;
    }
}