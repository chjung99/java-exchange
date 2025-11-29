package exchange.repository;

import exchange.domain.Order;

import java.util.Comparator;
import java.util.PriorityQueue;

public class InMemoryOrderBook implements OrderBook{
    private final PriorityQueue<Order> sellOrders =
            new PriorityQueue<>(Comparator.comparing(Order::getPrice).thenComparing(Order::getTimestamp));

    private final PriorityQueue<Order> buyOrders =
            new PriorityQueue<>(Comparator.comparing(Order::getPrice).thenComparing(Order::getTimestamp));

    @Override
    public void addOrder(Order order) {
        if (order.isBuy()) {
            buyOrders.offer(order);
        }
        if (order.isSell()) {
            sellOrders.offer(order);
        }
    }

    @Override
    public Order getBestSellOrder() {
        return sellOrders.peek();
    }

    @Override
    public Order getBestBuyOrder() {
        return buyOrders.peek();
    }

    @Override
    public void removeIfFullyExecuted(Order order) {
        if (order.isFullyExecuted()) {
            if (order.isBuy()) {
                buyOrders.poll();
            }
            if (order.isSell()) {
                buyOrders.poll();
            }
        }
    }

    @Override
    public boolean hasMatch(Order order) {
        if (order.isBuy()) {
            return !sellOrders.isEmpty();
        }
        if (order.isSell()) {
            return !buyOrders.isEmpty();
        }
        return false;
    }
}
