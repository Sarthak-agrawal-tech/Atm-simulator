package test.whitebox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import src.Account;
import src.AuthController;

public class AuthControllerWhiteBoxTest {

    @Test
    void loginRejectsInvalidPinFormatBeforeCheckingCaptcha() {
        AuthController controller = new AuthController(List.of(new Account("Asha", "1234", 500)),
                () -> new int[] { 1, 2 });

        String output = TestSupport.captureOutput(() -> {
            Account account = controller.login(new Scanner("12\n1234\n3\n"));
            assertNotNull(account);
            assertEquals("Asha", account.getName());
        });

        assertTrue(output.contains("Invalid PIN format."));
        assertTrue(output.contains("Login Successful."));
    }

    @Test
    void loginRetriesAfterCaptchaFailure() {
        AuthController controller = new AuthController(List.of(new Account("Asha", "1234", 500)),
                () -> new int[] { 1, 2 });

        String output = TestSupport.captureOutput(() -> {
            Account account = controller.login(new Scanner("1234\n0\n1234\n3\n"));
            assertNotNull(account);
        });

        assertTrue(output.contains("Captcha failed."));
        assertTrue(output.contains("Login Successful."));
    }

    @Test
    void loginDecrementsAttemptsAfterWrongPin() {
        AuthController controller = new AuthController(List.of(new Account("Asha", "1234", 500)),
                () -> new int[] { 1, 2 });

        String output = TestSupport.captureOutput(() -> {
            Account account = controller.login(new Scanner("9999\n3\n1234\n3\n"));
            assertNotNull(account);
        });

        assertTrue(output.contains("Attempts left: 2"));
    }

    @Test
    void loginLocksAccountAfterThreeWrongPins() {
        AuthController controller = new AuthController(List.of(new Account("Asha", "1234", 500)),
                () -> new int[] { 1, 2 });

        String output = TestSupport.captureOutput(() -> {
            int exitStatus = TestSupport.captureExitStatus(
                    () -> controller.login(new Scanner("9999\n3\n8888\n3\n7777\n3\n")));
            assertEquals(0, exitStatus);
        });

        assertTrue(output.contains("Account Locked."));
    }
}
