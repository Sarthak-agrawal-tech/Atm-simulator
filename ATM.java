import java.util.Scanner;

public class ATM {
    private Account account;
    private Scanner sc = new Scanner(System.in);

    public ATM(Account account) {
        this.account = account;
    }

    // FR-02 Authentication
    public boolean authenticate() {
        int attempts = 3;

        while (attempts > 0) {
            System.out.print("Enter your 4-digit PIN: ");
            String input = sc.nextLine();

            if (!input.matches("\\d{4}")) {
                System.out.println("Invalid format. Enter exactly 4 digits.");
                continue;
            }

            if (input.equals(account.getPin())) {
                System.out.println("Access Granted.");
                return true;
            } else {
                attempts--;
                System.out.println("Incorrect PIN. Attempts left: " + attempts);
            }
        }

        System.out.println("Account blocked. Exiting...");
        return false;
    }

    // FR-06 Practice Mode
    public void practiceMode() {
        System.out.println("\n--- Practice PIN Mode ---");

        while (true) {
            System.out.print("Enter a 4-digit PIN (or type exit): ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("exit")) break;

            if (input.matches("\\d{4}")) {
                System.out.println("Correct format ✔");
            } else {
                System.out.println("Invalid. Must be exactly 4 digits.");
            }
        }
    }

    public void menu() {
        while (true) {
            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Check Balance");
            System.out.println("2. Withdraw Cash");
            System.out.println("3. Deposit Cash");
            System.out.println("4. Exit");

            System.out.print("Choose option: ");
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
                    System.out.println("Thank you. Goodbye.");
                    DataStore.saveAccount(account);
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // FR-05 Balance Inquiry
    private void checkBalance() {
        System.out.println("Your balance: ₹" + account.getBalance());
    }

    // FR-03 Withdrawal
    private void withdraw() {
        System.out.print("Enter amount to withdraw: ");
        String input = sc.nextLine();

        try {
            double amount = Double.parseDouble(input);

            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }

            if (account.withdraw(amount)) {
                System.out.println("Withdrawal successful.");
                System.out.println("New balance: ₹" + account.getBalance());
            } else {
                System.out.println("Insufficient balance.");
            }

        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    // FR-04 Deposit
    private void deposit() {
        System.out.print("Enter amount to deposit: ");
        String input = sc.nextLine();

        try {
            double amount = Double.parseDouble(input);

            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }

            account.deposit(amount);
            System.out.println("Deposit successful.");
            System.out.println("New balance: ₹" + account.getBalance());

        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
}