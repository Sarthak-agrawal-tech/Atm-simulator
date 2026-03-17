package test;

public class PositiveNegativeTestRunner {
    public static void main(String[] args) {
        int failed = TestSuiteRunner.runSuite(
                "ATM positive and negative tests",
                ATMTest.class,
                ATMNegativeTest.class);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
