package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import src.Account;
import src.AccountController;

public class ATMDecisionTableTest {

    @Test
    void testWithdrawInvalidAmountDecision() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.withdraw(new Scanner("abc\n")));

        assertEquals(1000, account.getBalance());
        assertTrue(output.contains("Invalid amount."));
    }

    @Test
    void testWithdrawInsufficientBalanceDecision() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.withdraw(new Scanner("2000\n")));

        assertEquals(1000, account.getBalance());
        assertTrue(output.contains("Insufficient balance."));
    }

    @Test
    void testWithdrawCancelledDecision() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.withdraw(new Scanner("300\nno\n")));

        assertEquals(1000, account.getBalance());
        assertTrue(output.contains("Cancelled."));
    }

    @Test
    void testWithdrawSuccessfulDecision() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.withdraw(new Scanner("300\nyes\n")));

        assertEquals(700, account.getBalance());
        assertTrue(output.contains("Withdraw successful."));
    }

    @Test
    void testDepositInvalidAmountDecision() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.deposit(new Scanner("abc\n")));

        assertEquals(1000, account.getBalance());
        assertTrue(output.contains("Invalid amount."));
    }

    @Test
    void testDepositCancelledDecision() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.deposit(new Scanner("500\nno\n")));

        assertEquals(1000, account.getBalance());
        assertTrue(output.contains("Cancelled."));
    }

    @Test
    void testDepositSuccessfulDecision() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.deposit(new Scanner("500\nyes\n")));

        assertEquals(1500, account.getBalance());
        assertTrue(output.contains("Deposit successful."));
    }

    private String captureOutput(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(captured));
            action.run();
        } finally {
            System.setOut(originalOut);
        }

        return captured.toString();
    }
}
