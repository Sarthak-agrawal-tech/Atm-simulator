import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        List<Account> accounts = DataStore.loadAccounts();
        ATM atm = new ATM(accounts);

        while (true) {
            System.out.println("\n===== ATM SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");

            String choice = sc.nextLine();

            switch (choice) {

                case "1":

                    if (accounts.isEmpty()) {
                        System.out.println("No users exist. Create account first.");
                        break;
                    }

                    System.out.println("\nDo you want Practice Mode?");
                    System.out.println("1. Yes");
                    System.out.println("2. No");

                    String modeChoice = sc.nextLine();

                    if (modeChoice.equals("1")) {
                        atm.practiceMode();
                    }

                    // Always proceed to login after choice
                    if (atm.authenticate()) {
                        atm.menu();
                    }

                    break;

                case "2":
                    createAccount(accounts);
                    break;

                case "3":
                    System.out.println("Goodbye.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void createAccount(List<Account> accounts) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        String pin;
        while (true) {
            System.out.print("Set 4-digit PIN: ");
            pin = sc.nextLine().trim();

            if (!Validator.isValidPin(pin)) {
                System.out.println("Invalid PIN.");
                continue;
            }

            // Ensure unique PIN
            boolean exists = false;
            for (Account acc : accounts) {
                if (acc.getPin().equals(pin)) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                System.out.println("PIN already exists. Try another.");
            } else {
                break;
            }
        }

        System.out.print("Enter Initial Balance: ");
        double balance = Double.parseDouble(sc.nextLine());

        accounts.add(new Account(name, pin, balance));
        DataStore.saveAccounts(accounts);

        System.out.println("Account created successfully.");
    }
}