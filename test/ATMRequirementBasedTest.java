package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import src.Account;
import src.AccountController;
import src.DataStore;

public class ATMRequirementBasedTest {

    @Test
    void testRequirementCheckBalanceDisplaysCurrentBalance() {
        Account account = new Account("Test", "1234", 2500);
        AccountController controller = new AccountController(account);

        String output = captureOutput(controller::checkBalance);

        assertTrue(output.contains("Balance: Rs 2500.0"));
    }

    @Test
    void testRequirementDepositWithConfirmationUpdatesBalance() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.deposit(new Scanner("500\nyes\n")));

        assertEquals(1500, account.getBalance());
        assertTrue(output.contains("Deposit successful."));
        assertTrue(output.contains("Updated Balance: Rs 1500.0"));
    }

    @Test
    void testRequirementDepositCancellationKeepsBalanceUnchanged() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.deposit(new Scanner("500\nno\n")));

        assertEquals(1000, account.getBalance());
        assertTrue(output.contains("Cancelled."));
    }

    @Test
    void testRequirementWithdrawWithConfirmationUpdatesBalance() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.withdraw(new Scanner("300\nyes\n")));

        assertEquals(700, account.getBalance());
        assertTrue(output.contains("Withdraw successful."));
        assertTrue(output.contains("New Balance: Rs 700.0"));
    }

    @Test
    void testRequirementInsufficientBalancePreventsWithdrawal() {
        Account account = new Account("Test", "1234", 1000);
        AccountController controller = new AccountController(account);

        String output = captureOutput(() -> controller.withdraw(new Scanner("2000\n")));

        assertEquals(1000, account.getBalance());
        assertTrue(output.contains("Insufficient balance."));
    }

    @Test
    void testRequirementFindByPinReturnsMatchingAccount() {
        Account first = new Account("Asha", "1111", 500);
        Account second = new Account("Ravi", "2222", 900);
        List<Account> accounts = Arrays.asList(first, second);

        Account result = DataStore.findByPin(accounts, "2222");

        assertNotNull(result);
        assertEquals("Ravi", result.getName());
    }

    @Test
    void testRequirementToFileStringUsesStorageFormat() {
        Account account = new Account("Asha", "1111", 500);

        assertEquals("Asha,1111,500.0", account.toFileString());
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
