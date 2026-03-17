public class Validator {

    public static boolean isValidPin(String input) {
        return input.matches("\\d{4}");
    }

    public static boolean isValidAmount(String input) {
        try {
            double val = Double.parseDouble(input);
            return val > 0;
        } catch (Exception e) {
            return false;
        }
    }
}