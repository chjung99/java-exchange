package exchange.service;

import exchange.domain.*;
import exchange.domain.parser.PriceParser;
import exchange.domain.parser.QuantityParser;
import exchange.domain.parser.UserIdParser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OrderService {
    private final TradeEngine tradeEngine;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public OrderService(TradeEngine tradeEngine) {
        this.tradeEngine = tradeEngine;
    }

    public Future<TradeResult> processOrderAsync(OrderData orderData) {
        return executor.submit(() -> processOrder(orderData));
    }

    public TradeResult processOrder(OrderData orderData) {
        String userId = UserIdParser.parse(orderData.rawUserId);
        double price = PriceParser.parse(orderData.rawPrice);
        double quantity = QuantityParser.parse(orderData.rawQuantity);
        ActionType actionType = ActionType.valueOf(orderData.rawAction);
        String timestamp = orderData.rawTimestamp;

        return tradeEngine.execute(new Order(userId, price, quantity, actionType, timestamp));
    }
}
