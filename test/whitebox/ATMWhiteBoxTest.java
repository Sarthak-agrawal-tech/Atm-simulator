package test.whitebox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import src.ATM;
import src.Account;
import src.DataStore;

public class ATMWhiteBoxTest {

    @Test
    void authenticateWelcomesMatchingUser() {
        List<Account> accounts = List.of(new Account("Asha", "1234", 500));
        ATM atm = new ATM(accounts, new Scanner("1234\n"));

        String output = TestSupport.captureOutput(() -> assertTrue(atm.authenticate()));

        assertTrue(output.contains("Welcome, Asha"));
        Account currentUser = (Account) TestSupport.getField(atm, "currentUser");
        assertEquals("Asha", currentUser.getName());
    }

    @Test
    void authenticateBlocksAfterThreeWrongPins() {
        List<Account> accounts = List.of(new Account("Asha", "1234", 500));
        ATM atm = new ATM(accounts, new Scanner("9999\n8888\n7777\n"));

        String output = TestSupport.captureOutput(() -> {
            int exitStatus = TestSupport.captureExitStatus(atm::authenticate);
            assertEquals(0, exitStatus);
        });

        assertTrue(output.contains("Wrong PIN. Attempts left: 0"));
        assertTrue(output.contains("Account blocked."));
    }

    @Test
    void practiceModeShowsValidationFeedbackUntilExit() {
        ATM atm = new ATM(List.of(), new Scanner("1234\n12\nexit\n"));

        String output = TestSupport.captureOutput(atm::practiceMode);

        assertTrue(output.contains("Valid format OK"));
        assertTrue(output.contains("Invalid format."));
    }

    @Test
    void menuHandlesBalanceDepositAndExit() throws Exception {
        Path tempDir = Files.createTempDirectory("atm-whitebox");
        DataStore.setFileName(tempDir.resolve("data.txt").toString());

        try {
            Account account = new Account("Asha", "1234", 500);
            List<Account> accounts = new ArrayList<>();
            accounts.add(account);
            ATM atm = new ATM(accounts, new Scanner("1\n3\n100\n4\n"));
            TestSupport.setField(atm, "currentUser", account);

            String output = TestSupport.captureOutput(atm::menu);

            assertEquals(600, account.getBalance());
            assertTrue(output.contains("Balance: Rs. 500.0"));
            assertTrue(output.contains("Deposited. New Balance: Rs.600.0"));
            assertTrue(output.contains("Data saved. Goodbye."));
            assertTrue(Files.exists(tempDir.resolve("data.txt")));
        } finally {
            DataStore.resetFileName();
        }
    }

    @Test
    void menuHandlesWithdrawSuccessInsufficientFundsAndInvalidChoice() {
        Account account = new Account("Asha", "1234", 500);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        ATM atm = new ATM(accounts, new Scanner("9\n2\n100\n2\n1000\n"));
        TestSupport.setField(atm, "currentUser", account);

        String output = TestSupport.captureOutput(() -> {
            try {
                atm.menu();
            } catch (Exception ignored) {
            }
        });

        assertEquals(400, account.getBalance());
        assertTrue(output.contains("Invalid choice"));
        assertTrue(output.contains("Success. New Balance: Rs400.0"));
        assertTrue(output.contains("Insufficient balance."));
    }
}
