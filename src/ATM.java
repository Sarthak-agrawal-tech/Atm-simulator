package src;
import java.util.*;

public class ATM {

    private List<Account> accounts;
    private Account currentUser;
    private Scanner sc = new Scanner(System.in);

    public ATM(List<Account> accounts) {
        this.accounts = accounts;
    }

    // LOGIN
    public boolean authenticate() {
        int attempts = 3;

        while (attempts > 0) {
            System.out.print("Enter PIN: ");
            String pin = sc.nextLine();

            if (!Validator.isValidPin(pin)) {
                System.out.println("Invalid PIN format.");
                continue;
            }

            Account user = DataStore.findByPin(accounts, pin);

            if (user != null) {
                currentUser = user;
                System.out.println("Welcome, " + currentUser.getName());
                return true;
            } else {
                attempts--;
                System.out.println("Wrong PIN. Attempts left: " + attempts);
            }
        }

        System.out.println("Account blocked.");
        System.exit(0);
        return false;
    }

    public void practiceMode() {
        while (true) {
            System.out.print("Enter PIN (or exit): ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("exit")) break;

            if (Validator.isValidPin(input)) {
                System.out.println("Valid format OK");
            } else {
                System.out.println("Invalid format.");
            }
        }
    }

    public void menu() {
        while (true) {
            System.out.println("\n1. Check Balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Exit");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    checkBalance();
                    break;
                case "2":
                    withdraw();
                    break;
                case "3":
                    deposit();
                    break;
                case "4":
                    exit();
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void checkBalance() {
        System.out.println("Balance: Rs. " + currentUser.getBalance());
    }

    private void withdraw() {
        System.out.print("Enter amount: ");
        String input = sc.nextLine();

        if (!Validator.isValidAmount(input)) {
            System.out.println("Invalid amount.");
            return;
        }

        double amount = Double.parseDouble(input);

        if (currentUser.withdraw(amount)) {
            System.out.println("Success. New Balance: Rs" + currentUser.getBalance());
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    private void deposit() {
        System.out.print("Enter amount: ");
        String input = sc.nextLine();

        if (!Validator.isValidAmount(input)) {
            System.out.println("Invalid amount.");
            return;
        }

        double amount = Double.parseDouble(input);

        currentUser.deposit(amount);
        System.out.println("Deposited. New Balance: Rs." + currentUser.getBalance());
    }

    private void exit() {
        DataStore.saveAccounts(accounts);
        System.out.println("Data saved. Goodbye.");
    }
}