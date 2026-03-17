import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Account account = DataStore.loadAccount();
        ATM atm = new ATM(account);

        System.out.println("===== ATM Simulator =====");
        System.out.println("1. Practice PIN Mode");
        System.out.println("2. Continue to Login");

        String choice = sc.nextLine();

        if (choice.equals("1")) {
            atm.practiceMode();
        }

        if (atm.authenticate()) {
            atm.menu();
        }
    }
}