import java.util.Scanner;

public class ATM {
    private Account account;
    private Scanner sc = new Scanner(System.in);
    private boolean accessibilityMode = true;

    public ATM(Account account) {
        this.account = account;
    }

    // AUTHENTICATION WITH BLOCK SYSTEM
    public boolean authenticate() {
        int attempts = 3;

        while (attempts > 0) {

            if (accessibilityMode)
                AccessibilityHelper.guide("Please enter your 4-digit PIN carefully.");

            System.out.print("PIN: ");
            String input = sc.nextLine();

            if (!Validator.isValidPin(input)) {
                System.out.println("Invalid format.");
                AccessibilityHelper.help();
                continue;
            }

            if (input.equals(account.getPin())) {
                System.out.println("Access Granted.");
                return true;
            } else {
                attempts--;
                System.out.println("Wrong PIN. Attempts left: " + attempts);
            }
        }

        System.out.println("Session Blocked. Exiting system.");
        System.exit(0);
        return false;
    }

    // PRACTICE MODE (UNCHANGED BUT ENHANCED)
    public void practiceMode() {
        System.out.println("\n--- Practice Mode ---");

        while (true) {
            System.out.print("Enter PIN (or exit): ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("exit")) break;

            if (Validator.isValidPin(input)) {
                System.out.println("Correct format ✔");
            } else {
                System.out.println("Invalid PIN format.");
                AccessibilityHelper.help();
            }
        }
    }

    public void menu() {
        while (true) {

            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Check Balance");
            System.out.println("2. Withdraw Cash");
            System.out.println("3. Deposit Cash");
            System.out.println("4. Help");
            System.out.println("5. Exit");

            System.out.print("Select: ");
            String choice = sc.nextLine();

            try {
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
                        AccessibilityHelper.help();
                        break;
                    case "5":
                        exit();
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (RuntimeException e) {
                // handles cancelled actions
            }
        }
    }

    // BALANCE
    private void checkBalance() {
        System.out.println("Balance: ₹" + account.getBalance());
    }

    // WITHDRAW WITH FULL VALIDATION + CONFIRMATION
    private void withdraw() {

        if (accessibilityMode)
            AccessibilityHelper.guide("Enter amount to withdraw.");

        System.out.print("Amount: ");
        String input = sc.nextLine();

        if (!Validator.isValidAmount(input)) {
            System.out.println("Invalid amount.");
            return;
        }

        double amount = Double.parseDouble(input);

        if (amount > account.getBalance()) {
            System.out.println("Insufficient funds.");
            return;
        }

        AccessibilityHelper.confirmStep("withdraw ₹" + amount);

        account.withdraw(amount);

        System.out.println("Withdrawal successful.");
        System.out.println("New Balance: ₹" + account.getBalance());
    }

    // DEPOSIT WITH FEEDBACK
    private void deposit() {

        if (accessibilityMode)
            AccessibilityHelper.guide("Enter amount to deposit.");

        System.out.print("Amount: ");
        String input = sc.nextLine();

        if (!Validator.isValidAmount(input)) {
            System.out.println("Invalid amount.");
            return;
        }

        double amount = Double.parseDouble(input);

        AccessibilityHelper.confirmStep("deposit ₹" + amount);

        account.deposit(amount);

        System.out.println("Deposit successful.");
        System.out.println("Updated Balance: ₹" + account.getBalance());
    }

    // CLEAN EXIT (SRS SCENARIO F)
    private void exit() {
        System.out.println("Thank you for using ATM.");
        DataStore.saveAccount(account);
    }
}