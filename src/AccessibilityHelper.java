package src;
import java.util.Scanner;

public class AccessibilityHelper {

    private static Scanner sc = new Scanner(System.in);

    public static void guide(String message) {
        System.out.println("\n[GUIDE] " + message);
    }

    public static void confirmStep(String action) {
        System.out.print("[CONFIRM] Do you want to " + action + "? (yes/no): ");
        String input = sc.nextLine();

        if (!input.equalsIgnoreCase("yes")) {
            System.out.println("Action cancelled.");
            throw new RuntimeException("User Cancelled");
        }
    }

    public static void help() {
        System.out.println("\n[HELP]");
        System.out.println("- Enter numbers only");
        System.out.println("- PIN must be 4 digits");
        System.out.println("- Amount must be positive");
    }
}