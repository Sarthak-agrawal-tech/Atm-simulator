package test.whitebox;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import src.AccessibilityHelper;

public class AccessibilityHelperWhiteBoxTest {

    @Test
    void guidePrintsGuidancePrefix() {
        String output = TestSupport.captureOutput(() -> AccessibilityHelper.guide("Insert your card."));

        assertTrue(output.contains("[GUIDE] Insert your card."));
    }

    @Test
    void confirmStepAcceptsYesInput() {
        TestSupport.setStaticField(AccessibilityHelper.class, "sc", new Scanner("yes\n"));

        assertDoesNotThrow(() -> AccessibilityHelper.confirmStep("continue"));
    }

    @Test
    void confirmStepRejectsNonYesInput() {
        TestSupport.setStaticField(AccessibilityHelper.class, "sc", new Scanner("no\n"));

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> TestSupport.captureOutput(() -> AccessibilityHelper.confirmStep("continue")));

        assertEquals("User Cancelled", error.getMessage());
    }

    @Test
    void helpPrintsAllSupportInstructions() {
        String output = TestSupport.captureOutput(AccessibilityHelper::help);

        assertTrue(output.contains("- Enter numbers only"));
        assertTrue(output.contains("- PIN must be 4 digits"));
        assertTrue(output.contains("- Amount must be positive"));
    }
}
