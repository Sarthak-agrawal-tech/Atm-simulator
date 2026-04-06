package test;

import test.whitebox.ATMInterfaceWhiteBoxTest;
import test.whitebox.ATMWhiteBoxTest;
import test.whitebox.AccessibilityHelperWhiteBoxTest;
import test.whitebox.AccountControllerWhiteBoxTest;
import test.whitebox.AccountWhiteBoxTest;
import test.whitebox.AuthControllerWhiteBoxTest;
import test.whitebox.DataStoreWhiteBoxTest;
import test.whitebox.MainWhiteBoxTest;
import test.whitebox.ValidatorWhiteBoxTest;

public class WhiteBoxTestRunner {
    public static void main(String[] args) {
        int failed = TestSuiteRunner.runSuite(
                "ATM white-box tests",
                System.getProperty("whitebox.resultsFile"),
                AccountWhiteBoxTest.class,
                ValidatorWhiteBoxTest.class,
                AccountControllerWhiteBoxTest.class,
                DataStoreWhiteBoxTest.class,
                AccessibilityHelperWhiteBoxTest.class,
                AuthControllerWhiteBoxTest.class,
                ATMWhiteBoxTest.class,
                ATMInterfaceWhiteBoxTest.class,
                MainWhiteBoxTest.class);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
