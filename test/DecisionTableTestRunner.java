package test;

public class DecisionTableTestRunner {
    public static void main(String[] args) {
        int failed = TestSuiteRunner.runSuite(
                "ATM decision table tests",
                ATMDecisionTableTest.class);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
