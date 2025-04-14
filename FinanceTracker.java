import java.util.*;
import java.io.*;

class Transaction {
    String type;
    String category;
    double amount;
    String date;

    Transaction(String type, String category, double amount, String date) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String toString() {
        return type + " | " + category + " | " + amount + " | " + date;
    }
}

class Reminder {
    String message;
    String dueDate;

    Reminder(String message, String dueDate) {
        this.message = message;
        this.dueDate = dueDate;
    }

    public String toString() {
        return message + " (Due: " + dueDate + ")";
    }
}

public class FinanceTracker {
    static ArrayList<Transaction> transactions = new ArrayList<>();
    static ArrayList<Reminder> reminders = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int choice;

        loadTransactions();
        loadReminders();

        System.out.println("📌 Welcome to Personal Finance Tracker 📌");
        checkDueReminders();

        do {
            System.out.println("\n------ MENU ------");
            System.out.println("1. Add Transaction");
            System.out.println("2. View All Transactions");
            System.out.println("3. Show Current Balance");
            System.out.println("4. Add Reminder");
            System.out.println("5. View Reminders");
            System.out.println("6. Save & Exit");
            System.out.print("Choose any option: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Type (Income/Expense): ");
                    String type = sc.nextLine();
                    System.out.print("Enter Category: ");
                    String category = sc.nextLine();
                    System.out.print("Enter Amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter Date (dd-mm-yyyy): ");
                    String date = sc.nextLine();
                    transactions.add(new Transaction(type, category, amount, date));
                    System.out.println("✅ Transaction added!");
                    break;

                case 2:
                    System.out.println("\nAll Transactions:");
                    for (Transaction t : transactions)
                        System.out.println(t);
                    break;

                case 3:
                    double balance = calculateBalance();
                    System.out.println("💰 Current Balance: ₹" + balance);
                    break;

                case 4:
                    System.out.print("Enter Reminder Message: ");
                    String msg = sc.nextLine();
                    System.out.print("Enter Due Date (dd-mm-yyyy): ");
                    String due = sc.nextLine();
                    reminders.add(new Reminder(msg, due));
                    System.out.println("📅 Reminder added!");
                    break;

                case 5:
                    System.out.println("\nAll Reminders:");
                    for (Reminder r : reminders)
                        System.out.println("🔔 " + r);
                    break;

                case 6:
                    saveTransactions();
                    saveReminders();
                    System.out.println("✅ Data saved. Exiting...");
                    break;

                default:
                    System.out.println("⚠️ Invalid choice.");
            }

        } while (choice != 6);

        sc.close();
    }

    static double calculateBalance() {
        double balance = 0;
        for (Transaction t : transactions) {
            if (t.type.equalsIgnoreCase("Income"))
                balance += t.amount;
            else if (t.type.equalsIgnoreCase("Expense"))
                balance -= t.amount;
        }
        return balance;
    }

    static void saveTransactions() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.txt"));
        for (Transaction t : transactions)
            writer.write(t + "\n");
        writer.close();
    }

    static void loadTransactions() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                transactions.add(new Transaction(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]));
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("📂 No previous transaction data found.");
        }
    }

    static void saveReminders() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("reminders.txt"));
        for (Reminder r : reminders)
            writer.write(r.message + " | " + r.dueDate + "\n");
        writer.close();
    }

    static void loadReminders() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("reminders.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                reminders.add(new Reminder(parts[0], parts[1]));
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("📂 No previous reminder data found.");
        }
    }

    static void checkDueReminders() {
        String today = new java.text.SimpleDateFormat("dd-MM-yyyy").format(new Date());
        for (Reminder r : reminders) {
            if (r.dueDate.equals(today)) {
                System.out.println("🔔 Today’s Reminder: " + r.message + " (Due: " + r.dueDate + ")");
            }
        }
    }
}
