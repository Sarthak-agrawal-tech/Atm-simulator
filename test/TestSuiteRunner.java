package test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
        return runSuite(suiteName, null, testClasses);
    }

    public static int runSuite(String suiteName, String resultsFile, Class<?>... testClasses) {
        LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();

        for (Class<?> testClass : testClasses) {
            builder.selectors(DiscoverySelectors.selectClass(testClass));
        }

        LauncherDiscoveryRequest request = builder.build();
        SummaryListener listener = new SummaryListener(suiteName, resultsFile);
        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        return listener.getFailedCount();
    }

    private static class SummaryListener implements TestExecutionListener {
        private final String suiteName;
        private final String resultsFile;
        private final List<String> results = new ArrayList<>();
        private int passed;
        private int failed;

        SummaryListener(String suiteName, String resultsFile) {
            this.suiteName = suiteName;
            this.resultsFile = resultsFile;
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
                    results.add(toCsvLine(name, "SUCCESSFUL", ""));
                    System.out.println("PASS: " + name);
                    break;
                case FAILED:
                    failed++;
                    String message = testExecutionResult.getThrowable()
                            .map(Throwable::getMessage)
                            .orElse("");
                    results.add(toCsvLine(name, "FAILED", message));
                    System.out.println("FAIL: " + name);
                    testExecutionResult.getThrowable()
                            .ifPresent(error -> System.out.println("  " + error.getMessage()));
                    break;
                default:
                    results.add(toCsvLine(name, "SKIPPED", ""));
                    System.out.println("SKIP: " + name);
                    break;
            }
        }

        @Override
        public void testPlanExecutionFinished(TestPlan testPlan) {
            System.out.println("Passed: " + passed);
            System.out.println("Failed: " + failed);
            writeResultsFile();
        }

        private String toCsvLine(String testName, String status, String message) {
            return escape(testName) + "," + escape(status) + "," + escape(message);
        }

        private String escape(String value) {
            String safeValue = value == null ? "" : value.replace("\"", "\"\"");
            return "\"" + safeValue + "\"";
        }

        private void writeResultsFile() {
            if (resultsFile == null || resultsFile.isBlank()) {
                return;
            }

            List<String> lines = new ArrayList<>();
            lines.add("\"testName\",\"status\",\"message\"");
            lines.addAll(results);

            try {
                Path path = Path.of(resultsFile);
                if (path.getParent() != null) {
                    Files.createDirectories(path.getParent());
                }
                Files.write(path, lines, StandardCharsets.UTF_8);
            } catch (IOException error) {
                throw new RuntimeException("Unable to write test results file.", error);
            }
        }
    }
}
