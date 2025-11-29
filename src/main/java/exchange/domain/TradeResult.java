package exchange.domain;

public class TradeResult {
    private final Order order;
    private final double tradeQuantity;
    private final double tradePrice;

    public TradeResult(Order order, double tradeQuantity, double tradePrice) {
        this.order = order;
        this.tradeQuantity = tradeQuantity;
        this.tradePrice = tradePrice;
    }

    public Order getOrder() {
        return order;
    }

    public double getTradeQuantity() {
        return tradeQuantity;
    }

    public double getTradePrice() {
        return tradePrice;
    }
}
