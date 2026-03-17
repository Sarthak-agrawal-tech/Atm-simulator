import java.util.*;

public class Main {
    public static void main(String[] args) {

        List<Account> accounts = DataStore.loadAccounts();

        ATMInterface atm = new ATMInterface(accounts);
        atm.start();
    }
}