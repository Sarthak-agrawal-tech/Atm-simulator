package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import src.Account;
import src.Validator;

public class ATMTest {

    @Test
    void testDeposit() {
        Account acc = new Account("Test", "1234", 1000);
        acc.deposit(500);
        assertEquals(1500, acc.getBalance());
    }

    @Test
    void testWithdraw() {
        Account acc = new Account("Test", "1234", 1000);
        boolean result = acc.withdraw(300);
        assertTrue(result);
        assertEquals(700, acc.getBalance());
    }

    @Test
    void testValidPin() {
        assertTrue(Validator.isValidPin("1234"));
    }

    @Test
    void testValidAmount() {
        assertTrue(Validator.isValidAmount("500"));
    }
}
