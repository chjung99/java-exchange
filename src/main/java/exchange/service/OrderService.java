package exchange.service;

import exchange.domain.*;
import exchange.domain.parser.PriceParser;
import exchange.domain.parser.QuantityParser;
import exchange.domain.parser.UserIdParser;

public class OrderService {
    private final TradeEngine tradeEngine;

    public OrderService(TradeEngine tradeEngine) {
        this.tradeEngine = tradeEngine;
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
