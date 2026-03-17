package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import src.Account;
import src.AccountController;
import src.Validator;

public class ATMEquivalencePartitioningTest {

    @Test
    void testPinValidPartition() {
        assertTrue(Validator.isValidPin("4826"));
    }

    @Test
    void testPinInvalidNonNumericPartition() {
        assertFalse(Validator.isValidPin("12a4"));
    }

    @Test
    void testPinInvalidWrongLengthPartition() {
        assertFalse(Validator.isValidPin("12345"));
    }

    @Test
    void testAmountValidPositivePartition() {
        assertTrue(Validator.isValidAmount("250"));
    }

    @Test
    void testAmountInvalidNegativePartition() {
        assertFalse(Validator.isValidAmount("-250"));
    }

    @Test
    void testAmountInvalidNonNumericPartition() {
        assertFalse(Validator.isValidAmount("amount"));
    }

    @Test
    void testDepositValidPartition() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.deposit(new Scanner("250\nyes\n")));

        assertEquals(1250, account.getBalance());
        assertTrue(output.contains("Deposit successful."));
    }

    @Test
    void testDepositInvalidPartition() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.deposit(new Scanner("invalid\n")));

        assertEquals(1000, account.getBalance());
        assertTrue(output.contains("Invalid amount."));
    }

    @Test
    void testWithdrawValidPartition() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.withdraw(new Scanner("250\nyes\n")));

        assertEquals(750, account.getBalance());
        assertTrue(output.contains("Withdraw successful."));
    }

    @Test
    void testWithdrawInvalidOverBalancePartition() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.withdraw(new Scanner("1500\n")));

        assertEquals(1000, account.getBalance());
        assertTrue(output.contains("Insufficient balance."));
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
