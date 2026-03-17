import java.io.*;

public class DataStore {
    private static final String FILE_NAME = "data.txt";

    public static Account loadAccount() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String pin = br.readLine();
            double balance = Double.parseDouble(br.readLine());
            return new Account(pin, balance);
        } catch (Exception e) {
            // default account
            return new Account("1234", 1000);
        }
    }

    public static void saveAccount(Account account) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write(account.getPin());
            bw.newLine();
            bw.write(String.valueOf(account.getBalance()));
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }
}