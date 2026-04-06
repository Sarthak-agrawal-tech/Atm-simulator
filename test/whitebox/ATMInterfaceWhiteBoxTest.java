package test.whitebox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import src.ATMInterface;
import src.Account;
import src.AuthController;
import src.DataStore;

public class ATMInterfaceWhiteBoxTest {

    @Test
    void startHandlesPracticeModeInvalidChoiceAndExit() throws Exception {
        Path tempDir = Files.createTempDirectory("atm-interface-whitebox");
        DataStore.setFileName(tempDir.resolve("data.txt").toString());

        try {
            ATMInterface atmInterface = new ATMInterface(
                    new ArrayList<>(),
                    new Scanner("9\n1\n1234\n12\nexit\n4\n"),
                    AuthController::new);

            String output = TestSupport.captureOutput(atmInterface::start);

            assertTrue(output.contains("Invalid choice."));
            assertTrue(output.contains("Valid PIN format"));
            assertTrue(output.contains("Invalid PIN format"));
            assertTrue(output.contains("Goodbye."));
            assertTrue(Files.exists(tempDir.resolve("data.txt")));
        } finally {
            DataStore.resetFileName();
        }
    }

    @Test
    void startCreatesAccountAfterRetryingInvalidPin() throws Exception {
        Path tempDir = Files.createTempDirectory("atm-interface-whitebox");
        DataStore.setFileName(tempDir.resolve("data.txt").toString());

        try {
            List<Account> accounts = new ArrayList<>();
            ATMInterface atmInterface = new ATMInterface(
                    accounts,
                    new Scanner("3\nMeera\n12\n4321\n250\n4\n"),
                    AuthController::new);

            String output = TestSupport.captureOutput(atmInterface::start);

            assertEquals(1, accounts.size());
            assertEquals("Meera", accounts.get(0).getName());
            assertTrue(output.contains("Invalid PIN."));
            assertTrue(output.contains("Account created."));
        } finally {
            DataStore.resetFileName();
        }
    }

    @Test
    void startRunsLoginFlowThenAllowsTransactionsAndLogout() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("Asha", "1234", 500));

        ATMInterface atmInterface = new ATMInterface(
                accounts,
                new Scanner("2\n1234\n3\n1\n2\n100\nyes\n3\n50\nyes\n9\n4\n4\n"),
                configuredAccounts -> new AuthController(configuredAccounts, () -> new int[] { 1, 2 }));

        String output = TestSupport.captureOutput(atmInterface::start);

        assertEquals(450, accounts.get(0).getBalance());
        assertTrue(output.contains("Login Successful. Welcome Asha"));
        assertTrue(output.contains("Withdraw successful."));
        assertTrue(output.contains("Deposit successful."));
        assertTrue(output.contains("Invalid"));
    }
}
