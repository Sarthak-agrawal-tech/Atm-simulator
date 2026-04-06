package src;
import java.util.*;

public class AuthController {

    public interface CaptchaGenerator {
        int[] nextChallenge();
    }

    private List<Account> accounts;
    private int attempts = 3;
    private CaptchaGenerator captchaGenerator;

    public AuthController(List<Account> accounts) {
        this(accounts, () -> new int[] { (int) (Math.random() * 10), (int) (Math.random() * 10) });
    }

    public AuthController(List<Account> accounts, CaptchaGenerator captchaGenerator) {
        this.accounts = accounts;
        this.captchaGenerator = captchaGenerator;
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
        int[] challenge = captchaGenerator.nextChallenge();
        int a = challenge[0];
        int b = challenge[1];

        System.out.print("CAPTCHA: " + a + " + " + b + " = ");
        int ans = Integer.parseInt(sc.nextLine());

        return ans == (a + b);
    }
}
