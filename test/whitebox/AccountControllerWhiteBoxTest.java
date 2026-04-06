package test.whitebox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import src.Account;
import src.AccountController;

public class AccountControllerWhiteBoxTest {

    @Test
    void checkBalancePrintsCurrentBalance() {
        AccountController controller = new AccountController(new Account("Asha", "1234", 1500));

        String output = TestSupport.captureOutput(controller::checkBalance);

        assertTrue(output.contains("Balance: Rs 1500.0"));
    }

    @Test
    void withdrawStopsOnInvalidAmount() {
        Account account = new Account("Asha", "1234", 1500);
        AccountController controller = new AccountController(account);

        String output = TestSupport.captureOutput(() -> controller.withdraw(new Scanner("abc\n")));

        assertEquals(1500, account.getBalance());
        assertTrue(output.contains("Invalid amount."));
    }

    @Test
    void withdrawStopsOnInsufficientBalance() {
        Account account = new Account("Asha", "1234", 1500);
        AccountController controller = new AccountController(account);

        String output = TestSupport.captureOutput(() -> controller.withdraw(new Scanner("2000\n")));

        assertEquals(1500, account.getBalance());
        assertTrue(output.contains("Insufficient balance."));
    }

    @Test
    void withdrawStopsWhenUserCancelsConfirmation() {
        Account account = new Account("Asha", "1234", 1500);
        AccountController controller = new AccountController(account);

        String output = TestSupport.captureOutput(() -> controller.withdraw(new Scanner("200\nno\n")));

        assertEquals(1500, account.getBalance());
        assertTrue(output.contains("Cancelled."));
    }

    @Test
    void withdrawCompletesWhenInputIsValidAndConfirmed() {
        Account account = new Account("Asha", "1234", 1500);
        AccountController controller = new AccountController(account);

        String output = TestSupport.captureOutput(() -> controller.withdraw(new Scanner("200\nyes\n")));

        assertEquals(1300, account.getBalance());
        assertTrue(output.contains("Withdraw successful."));
        assertTrue(output.contains("New Balance: Rs 1300.0"));
    }

    @Test
    void depositStopsOnInvalidAmount() {
        Account account = new Account("Asha", "1234", 1500);
        AccountController controller = new AccountController(account);

        String output = TestSupport.captureOutput(() -> controller.deposit(new Scanner("invalid\n")));

        assertEquals(1500, account.getBalance());
        assertTrue(output.contains("Invalid amount."));
    }

    @Test
    void depositStopsWhenUserCancelsConfirmation() {
        Account account = new Account("Asha", "1234", 1500);
        AccountController controller = new AccountController(account);

        String output = TestSupport.captureOutput(() -> controller.deposit(new Scanner("200\nno\n")));

        assertEquals(1500, account.getBalance());
        assertTrue(output.contains("Cancelled."));
    }

    @Test
    void depositCompletesWhenInputIsValidAndConfirmed() {
        Account account = new Account("Asha", "1234", 1500);
        AccountController controller = new AccountController(account);

        String output = TestSupport.captureOutput(() -> controller.deposit(new Scanner("200\nyes\n")));

        assertEquals(1700, account.getBalance());
        assertTrue(output.contains("Deposit successful."));
        assertTrue(output.contains("Updated Balance: Rs 1700.0"));
    }
}
