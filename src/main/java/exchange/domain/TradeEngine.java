package exchange.domain;

import exchange.repository.OrderBook;
import exchange.service.WalletService;

public class TradeEngine {
    private final OrderBook orderBook;
    private final WalletService walletService;

    public TradeEngine(OrderBook orderBook, WalletService walletService) {
        this.orderBook = orderBook;
        this.walletService = walletService;
    }

    public TradeResult execute(Order incomingOrder) {
        if (!orderBook.hasMatch(incomingOrder)) {
            if (incomingOrder.isSell()) {
                orderBook.addOrder(incomingOrder);
                return new TradeResult(incomingOrder, 0, 0);
            }
            throw new IllegalArgumentException("[ERROR] 체결 가능한 주문이 없습니다");
        }

        Order counterOrder = incomingOrder.isBuy() ?
                orderBook.getBestSellOrder() :
                orderBook.getBestBuyOrder();

        double tradeQuantity = Math.min(incomingOrder.getQuantity(), counterOrder.getQuantity());
        double tradePrice = counterOrder.getPrice();

        walletService.updateBalance(incomingOrder, counterOrder, tradePrice, tradeQuantity);

        incomingOrder.updateQuantity(tradeQuantity);
        counterOrder.updateQuantity(tradeQuantity);

        orderBook.removeIfFullyExecuted(counterOrder);

        if (incomingOrder.getQuantity() > 0) {
            orderBook.addOrder(incomingOrder);
        }

        return new TradeResult(incomingOrder, tradeQuantity, tradePrice);
    }
}
