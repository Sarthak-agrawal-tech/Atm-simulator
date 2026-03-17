package src;
import java.util.*;

public class ATMInterface {

    private List<Account> accounts;
    private Scanner sc = new Scanner(System.in);

    public ATMInterface(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void start() {

        while (true) {

            System.out.println("\n===== ATM =====");
            System.out.println("1. Practice PIN");
            System.out.println("2. Login");
            System.out.println("3. Create Account");
            System.out.println("4. Exit");

            String choice = sc.nextLine();

            switch (choice) {

                case "1":
                    practiceMode();
                    break;

                case "2":
                    loginFlow();
                    break;

                case "3":
                    createAccount();
                    break;

                case "4":
                    DataStore.saveAccounts(accounts);
                    System.out.println("Goodbye.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void practiceMode() {
        while (true) {
            System.out.print("Enter PIN (or exit): ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("exit")) break;

            if (Validator.isValidPin(input)) {
                System.out.println("Valid PIN format");
            } else {
                System.out.println("Invalid PIN format");
            }
        }
    }

    private void loginFlow() {
        AuthController auth = new AuthController(accounts);
        Account user = auth.login(sc);

        AccountController controller = new AccountController(user);

        while (true) {

            System.out.println("\n1. Balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Logout");

            String ch = sc.nextLine();

            switch (ch) {
                case "1": controller.checkBalance(); break;
                case "2": controller.withdraw(sc); break;
                case "3": controller.deposit(sc); break;
                case "4": return;
                default: System.out.println("Invalid");
            }
        }
    }

    private void createAccount() {
        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        String pin;
        while (true) {
            System.out.print("Set 4-digit PIN: ");
            pin = sc.nextLine();

            if (!Validator.isValidPin(pin)) {
                System.out.println("Invalid PIN.");
                continue;
            }
            break;
        }

        System.out.print("Initial Balance: ");
        double bal = Double.parseDouble(sc.nextLine());

        accounts.add(new Account(name, pin, bal));
        DataStore.saveAccounts(accounts);

        System.out.println("Account created.");
    }
}