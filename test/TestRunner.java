package test;

public class TestRunner {
    public static void main(String[] args) {
        int failed = TestSuiteRunner.runSuite(
                "ATM positive, negative, and boundary tests",
                ATMTest.class,
                ATMNegativeTest.class,
                ATMBoundaryValueTest.class);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
