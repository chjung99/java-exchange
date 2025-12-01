package exchange.repository;

import exchange.domain.Order;

import java.util.Comparator;
import java.util.List;
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

    @Override
    public List<Order> getAllOrders() {
        List<Order> allOrders = new java.util.ArrayList<>();
        allOrders.addAll(buyOrders);
        allOrders.addAll(sellOrders);

        // 정렬: BUY는 가격 내림차순, SELL은 가격 오름차순
        allOrders.sort((o1, o2) -> {
            if (o1.isBuy() && o2.isBuy()) {
                return Double.compare(o2.getPrice(), o1.getPrice()); // BUY: 높은 가격 우선
            } else if (o1.isSell() && o2.isSell()) {
                return Double.compare(o1.getPrice(), o2.getPrice()); // SELL: 낮은 가격 우선
            } else {
                return o1.getTimestamp().compareTo(o2.getTimestamp()); // BUY vs SELL: 시간 순서
            }
        });

        return allOrders;
    }
}
