package test;

public class RequirementBasedTestRunner {
    public static void main(String[] args) {
        int failed = TestSuiteRunner.runSuite(
                "ATM requirement-based tests",
                ATMRequirementBasedTest.class);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
