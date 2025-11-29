package exchange.domain;

public class Order {
    private final String userId;
    private final double price;
    private double quantity;
    private final ActionType actionType;
    private final String timestamp;
    private State state;

    public Order(String userId, double price, double quantity, ActionType actionType, String timestamp) {
        this.userId = userId;
        this.price = price;
        this.quantity = quantity;
        this.actionType = actionType;
        this.timestamp = timestamp;
        this.state = State.IN_PROGRESS;
    }

    public double getPrice() {
        return price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUserId() {
        return userId;
    }

    public State getState() {
        return state;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public boolean isBuy() {
        return actionType.equals(ActionType.BUY);
    }

    public boolean isSell() {
        return actionType.equals(ActionType.SELL);
    }

    public boolean isFullyExecuted() {
        return state.equals(State.FULLY_EXECUTED);
    }

    public void updateQuantity(double tradeQuantity) {
        quantity -= tradeQuantity;
        if (quantity == 0) state = State.FULLY_EXECUTED;
    }
}
