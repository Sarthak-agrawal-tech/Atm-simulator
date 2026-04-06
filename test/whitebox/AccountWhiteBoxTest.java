package test.whitebox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import src.Account;

public class AccountWhiteBoxTest {

    @Test
    void depositAddsAmountToBalance() {
        Account account = new Account("Asha", "1234", 1000);

        account.deposit(250);

        assertEquals("Asha", account.getName());
        assertEquals("1234", account.getPin());
        assertEquals(1250, account.getBalance());
    }

    @Test
    void withdrawSucceedsWhenAmountEqualsBalance() {
        Account account = new Account("Ravi", "4567", 300);

        boolean result = account.withdraw(300);

        assertTrue(result);
        assertEquals(0, account.getBalance());
    }

    @Test
    void withdrawFailsWhenAmountExceedsBalance() {
        Account account = new Account("Ravi", "4567", 300);

        boolean result = account.withdraw(301);

        assertFalse(result);
        assertEquals(300, account.getBalance());
    }

    @Test
    void toFileStringUsesCommaSeparatedFormat() {
        Account account = new Account("Meera", "7890", 450.5);

        assertEquals("Meera,7890,450.5", account.toFileString());
    }
}
