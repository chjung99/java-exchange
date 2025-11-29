package exchange.repository;

import exchange.domain.Order;

public interface OrderBook {
    public void addOrder(Order order);

    public Order getBestSellOrder();
    public Order getBestBuyOrder();

    public void removeIfFullyExecuted(Order order);

    public boolean hasMatch(Order incomingOrder);
}
