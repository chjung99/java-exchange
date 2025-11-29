package exchange.domain;

public class Balance {
    private final String userId;
    private final double KRWBalance;
    private final double BTCBalance;

    public Balance(String userId, double KRWBalance, double BTCBalance) {
        this.userId = userId;
        this.KRWBalance = KRWBalance;
        this.BTCBalance = BTCBalance;
    }

    public String getUserId() {
        return userId;
    }

    public double getKRWBalance() {
        return KRWBalance;
    }

    public double getBTCBalance() {
        return BTCBalance;
    }
}
