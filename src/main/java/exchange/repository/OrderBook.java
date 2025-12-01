package exchange.repository;

import exchange.domain.Order;

import java.util.List;

public interface OrderBook {
    public void addOrder(Order order);

    public Order getBestSellOrder();
    public Order getBestBuyOrder();

    public void removeIfFullyExecuted(Order order);

    public boolean hasMatch(Order incomingOrder);

    public List<Order> getAllOrders();
}
