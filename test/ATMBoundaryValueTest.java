package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import src.Account;
import src.Validator;

public class ATMBoundaryValueTest {

    @Test
    void testPinLengthBelowBoundary() {
        assertFalse(Validator.isValidPin("123"));
    }

    @Test
    void testPinLengthAtBoundary() {
        assertTrue(Validator.isValidPin("1234"));
    }

    @Test
    void testPinLengthAboveBoundary() {
        assertFalse(Validator.isValidPin("12345"));
    }

    @Test
    void testAmountBelowMinimumBoundary() {
        assertFalse(Validator.isValidAmount("-0.01"));
    }

    @Test
    void testAmountAtMinimumBoundary() {
        assertFalse(Validator.isValidAmount("0"));
    }

    @Test
    void testAmountJustAboveMinimumBoundary() {
        assertTrue(Validator.isValidAmount("0.01"));
    }

    @Test
    void testWithdrawJustBelowBalanceBoundary() {
        Account acc = new Account("Test", "1234", 1000);
        assertTrue(acc.withdraw(999.99));
        assertEquals(0.01, acc.getBalance(), 0.0001);
    }

    @Test
    void testWithdrawAtBalanceBoundary() {
        Account acc = new Account("Test", "1234", 1000);
        assertTrue(acc.withdraw(1000));
        assertEquals(0, acc.getBalance(), 0.0001);
    }

    @Test
    void testWithdrawAboveBalanceBoundary() {
        Account acc = new Account("Test", "1234", 1000);
        assertFalse(acc.withdraw(1000.01));
        assertEquals(1000, acc.getBalance(), 0.0001);
    }

    @Test
    void testDepositJustAboveMinimumBoundary() {
        Account acc = new Account("Test", "1234", 1000);
        acc.deposit(0.01);
        assertEquals(1000.01, acc.getBalance(), 0.0001);
    }
}
