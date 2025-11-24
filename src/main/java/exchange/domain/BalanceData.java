package exchange.domain;

public class BalanceData{
    public final String rawUserId;
    public final String rawKRWBalance;
    public final String rawBTCBalance;

    public BalanceData(String rawUserId, String rawKRWBalance, String rawBTCBalance) {
        this.rawUserId = rawUserId;
        this.rawKRWBalance = rawKRWBalance;
        this.rawBTCBalance = rawBTCBalance;
    }
}
