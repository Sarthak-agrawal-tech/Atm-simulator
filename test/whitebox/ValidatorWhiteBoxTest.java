package test.whitebox;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import src.Validator;

public class ValidatorWhiteBoxTest {

    @Test
    void acceptsExactlyFourDigitsForPin() {
        assertTrue(Validator.isValidPin("4826"));
    }

    @Test
    void rejectsPinsWithWrongLengthOrLetters() {
        assertFalse(Validator.isValidPin("123"));
        assertFalse(Validator.isValidPin("12345"));
        assertFalse(Validator.isValidPin("12a4"));
    }

    @Test
    void acceptsPositiveAmounts() {
        assertTrue(Validator.isValidAmount("0.01"));
        assertTrue(Validator.isValidAmount("250"));
    }

    @Test
    void rejectsZeroNegativeAndNonNumericAmounts() {
        assertFalse(Validator.isValidAmount("0"));
        assertFalse(Validator.isValidAmount("-10"));
        assertFalse(Validator.isValidAmount("amount"));
    }
}
