package exchange.domain;

public class OrderData {
    public final String rawUserId;
    public final String rawPrice;
    public final String rawQuantity;
    public final String rawAction;
    public final String rawTimestamp;

    public OrderData(String rawUserId, String rawPrice, String rawQuantity, String rawAction, String rawTimestamp) {
        this.rawUserId = rawUserId;
        this.rawPrice = rawPrice;
        this.rawQuantity = rawQuantity;
        this.rawAction = rawAction;
        this.rawTimestamp = rawTimestamp;
    }
}
