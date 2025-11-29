package exchange.domain;

public class Wallet {
    private final String userId;
    private final WalletType type;
    private double balance;

    public Wallet(String userId, WalletType type, double balance) {
        this.userId = userId;
        this.type = type;
        this.balance = balance;
    }

    public synchronized boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public synchronized void deposit(double amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}
