package src;
import java.io.*;
import java.util.*;

public class DataStore {

    private static final String FILE_NAME = "data.txt";

    public static List<Account> loadAccounts() {
        List<Account> accounts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 3) {
                    String name = parts[0];
                    String pin = parts[1];
                    double balance = Double.parseDouble(parts[2]);

                    accounts.add(new Account(name, pin, balance));
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading accounts.");
        }
        if (accounts.isEmpty()) {
            accounts.add(new Account("Admin", "1234", 5000));
            saveAccounts(accounts);
        }

        return accounts;
    }

    public static void saveAccounts(List<Account> accounts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Account acc : accounts) {
                bw.write(acc.toFileString());
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving accounts.");
        }
    }

    public static Account findByPin(List<Account> accounts, String pin) {
        for (Account acc : accounts) {
            if (acc.getPin().equals(pin)) {
                return acc;
            }
        }
        return null;
    }
}