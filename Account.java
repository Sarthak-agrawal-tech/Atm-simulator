public class Account {
    private String pin;
    private double balance;

    public Account(String pin, double balance) {
        this.pin = pin;
        this.balance = balance;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}