package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import src.Account;
import src.Validator;

public class ATMNegativeTest {

    @Test
    void testWithdrawFail() {
        Account acc = new Account("Test", "1234", 1000);
        assertFalse(acc.withdraw(2000));
    }

    @Test
    void testInvalidPin() {
        assertFalse(Validator.isValidPin("12a4"));
        assertFalse(Validator.isValidPin("123"));
    }

    @Test
    void testNegativeAmount() {
        assertFalse(Validator.isValidAmount("-100"));
    }

    @Test
    void testNonNumericAmount() {
        assertFalse(Validator.isValidAmount("abc"));
    }

    @Test
    void testZeroAmount() {
        assertFalse(Validator.isValidAmount("0"));
    }
}
