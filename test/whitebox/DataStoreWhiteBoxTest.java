package test.whitebox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import src.Account;
import src.DataStore;

public class DataStoreWhiteBoxTest {

    @Test
    void loadAccountsCreatesDefaultAdminWhenStorageIsMissing() throws Exception {
        Path tempDir = Files.createTempDirectory("datastore-whitebox");
        DataStore.setFileName(tempDir.resolve("data.txt").toString());

        try {
            List<Account> accounts = DataStore.loadAccounts();

            assertEquals(1, accounts.size());
            assertEquals("Admin", accounts.get(0).getName());
            assertTrue(Files.exists(tempDir.resolve("data.txt")));
        } finally {
            DataStore.resetFileName();
        }
    }

    @Test
    void saveAccountsAndFindByPinWorkTogether() throws Exception {
        Path tempDir = Files.createTempDirectory("datastore-whitebox");
        DataStore.setFileName(tempDir.resolve("data.txt").toString());

        try {
            List<Account> accounts = Arrays.asList(
                    new Account("Asha", "1111", 500),
                    new Account("Ravi", "2222", 900));

            DataStore.saveAccounts(accounts);
            List<Account> loadedAccounts = DataStore.loadAccounts();

            assertEquals(2, loadedAccounts.size());
            Account matched = DataStore.findByPin(loadedAccounts, "2222");
            assertNotNull(matched);
            assertEquals("Ravi", matched.getName());
            assertNull(DataStore.findByPin(loadedAccounts, "9999"));
        } finally {
            DataStore.resetFileName();
        }
    }

    @Test
    void loadAccountsFallsBackToDefaultWhenStoredDataIsMalformed() throws Exception {
        Path tempDir = Files.createTempDirectory("datastore-whitebox");
        DataStore.setFileName(tempDir.resolve("data.txt").toString());

        try {
            Files.writeString(tempDir.resolve("data.txt"), "broken,line");

            List<Account> accounts = DataStore.loadAccounts();

            assertEquals(1, accounts.size());
            assertEquals("Admin", accounts.get(0).getName());
        } finally {
            DataStore.resetFileName();
        }
    }
}
