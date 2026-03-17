import java.util.*;

public class AuthController {

    private List<Account> accounts;
    private int attempts = 3;

    public AuthController(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Account login(Scanner sc) {

        while (attempts > 0) {

            System.out.print("Enter PIN: ");
            String pin = sc.nextLine().trim();

            if (!Validator.isValidPin(pin)) {
                System.out.println("Invalid PIN format.");
                continue;
            }

            // Simulated CAPTCHA (as per UML)
            if (!captcha(sc)) {
                System.out.println("Captcha failed.");
                continue;
            }

            Account user = DataStore.findByPin(accounts, pin);

            if (user != null) {
                System.out.println("Login Successful. Welcome " + user.getName());
                return user;
            } else {
                attempts--;
                System.out.println("Invalid PIN. Attempts left: " + attempts);
            }
        }

        System.out.println("Account Locked.");
        System.exit(0);
        return null;
    }

    private boolean captcha(Scanner sc) {
        int a = (int)(Math.random()*10);
        int b = (int)(Math.random()*10);

        System.out.print("CAPTCHA: " + a + " + " + b + " = ");
        int ans = Integer.parseInt(sc.nextLine());

        return ans == (a + b);
    }
}