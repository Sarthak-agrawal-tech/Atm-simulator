import java.util.*;

public class AccountController {

    private Account user;

    public AccountController(Account user) {
        this.user = user;
    }

    public void checkBalance() {
        System.out.println("Balance: Rs " + user.getBalance());
    }

    public void withdraw(Scanner sc) {

        System.out.print("Enter amount: ");
        String input = sc.nextLine();

        if (!Validator.isValidAmount(input)) {
            System.out.println("Invalid amount.");
            return;
        }

        double amount = Double.parseDouble(input);

        if (amount > user.getBalance()) {
            System.out.println("Insufficient balance.");
            return;
        }

        // Confirmation step (UML)
        System.out.print("Confirm withdrawal? (yes/no): ");
        if (!sc.nextLine().equalsIgnoreCase("yes")) {
            System.out.println("Cancelled.");
            return;
        }

        user.withdraw(amount);
        System.out.println("Withdraw successful.");
        System.out.println("New Balance: Rs " + user.getBalance());
    }

    public void deposit(Scanner sc) {

        System.out.print("Enter amount: ");
        String input = sc.nextLine();

        if (!Validator.isValidAmount(input)) {
            System.out.println("Invalid amount.");
            return;
        }

        double amount = Double.parseDouble(input);

        System.out.print("Confirm deposit? (yes/no): ");
        if (!sc.nextLine().equalsIgnoreCase("yes")) {
            System.out.println("Cancelled.");
            return;
        }

        user.deposit(amount);

        System.out.println("Deposit successful.");
        System.out.println("Updated Balance: Rs " + user.getBalance());
    }
}