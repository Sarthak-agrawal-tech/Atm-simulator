package test;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

public class TestSuiteRunner {

    public static int runSuite(String suiteName, Class<?>... testClasses) {
        LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();

        for (Class<?> testClass : testClasses) {
            builder.selectors(DiscoverySelectors.selectClass(testClass));
        }

        LauncherDiscoveryRequest request = builder.build();
        SummaryListener listener = new SummaryListener(suiteName);
        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        return listener.getFailedCount();
    }

    private static class SummaryListener implements TestExecutionListener {
        private final String suiteName;
        private int passed;
        private int failed;

        SummaryListener(String suiteName) {
            this.suiteName = suiteName;
        }

        int getFailedCount() {
            return failed;
        }

        @Override
        public void testPlanExecutionStarted(TestPlan testPlan) {
            System.out.println("Running " + suiteName + "...");
        }

        @Override
        public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            if (!testIdentifier.isTest()) {
                return;
            }

            String name = testIdentifier.getDisplayName();
            switch (testExecutionResult.getStatus()) {
                case SUCCESSFUL:
                    passed++;
                    System.out.println("PASS: " + name);
                    break;
                case FAILED:
                    failed++;
                    System.out.println("FAIL: " + name);
                    testExecutionResult.getThrowable()
                            .ifPresent(error -> System.out.println("  " + error.getMessage()));
                    break;
                default:
                    System.out.println("SKIP: " + name);
                    break;
            }
        }

        @Override
        public void testPlanExecutionFinished(TestPlan testPlan) {
            System.out.println("Passed: " + passed);
            System.out.println("Failed: " + failed);
        }
    }
}
