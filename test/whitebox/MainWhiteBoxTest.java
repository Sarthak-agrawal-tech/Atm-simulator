package test.whitebox;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import src.DataStore;
import src.Main;

public class MainWhiteBoxTest {

    @Test
    void mainBootstrapsInterfaceAndExitsCleanly() throws Exception {
        Path tempDir = Files.createTempDirectory("main-whitebox");
        InputStream originalIn = System.in;
        DataStore.setFileName(tempDir.resolve("data.txt").toString());
        System.setIn(new java.io.ByteArrayInputStream("4\n".getBytes()));

        try {
            String output = TestSupport.captureOutput(() -> Main.main(new String[0]));

            assertTrue(output.contains("===== ATM ====="));
            assertTrue(output.contains("Goodbye."));
            assertTrue(Files.exists(tempDir.resolve("data.txt")));
        } finally {
            DataStore.resetFileName();
            System.setIn(originalIn);
        }
    }
}
