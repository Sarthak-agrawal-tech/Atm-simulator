package test;

public class EquivalencePartitioningTestRunner {
    public static void main(String[] args) {
        int failed = TestSuiteRunner.runSuite(
                "ATM equivalence partitioning tests",
                ATMEquivalencePartitioningTest.class);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
