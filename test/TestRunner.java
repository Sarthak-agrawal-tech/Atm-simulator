package test;

public class TestRunner {
    public static void main(String[] args) {
        int failed = TestSuiteRunner.runSuite(
                "ATM positive, negative, boundary, requirement-based, decision-table, and equivalence partitioning tests",
                ATMTest.class,
                ATMNegativeTest.class,
                ATMBoundaryValueTest.class,
                ATMRequirementBasedTest.class,
                ATMDecisionTableTest.class,
                ATMEquivalencePartitioningTest.class);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
