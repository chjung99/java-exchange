package exchange.domain;

public abstract class AbstractWallet {
    private final String userId;
    private final WalletType type;
    private double balance;

    public AbstractWallet(String userId, WalletType type, double balance) {
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
}
