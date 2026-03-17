package test;

public class BoundaryValueTestRunner {
    public static void main(String[] args) {
        int failed = TestSuiteRunner.runSuite(
                "ATM boundary value tests",
                ATMBoundaryValueTest.class);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
