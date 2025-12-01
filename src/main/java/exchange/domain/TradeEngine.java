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
        //todo: order 검증 로직 필요 (잔고가 없는데 주문하는 경우)
        validateOrder(incomingOrder);

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

    private void validateOrder(Order order) {
        if (order.isBuy()){
            validateBuyOrder(order);
        }
        if (order.isSell()){
            validateSellOrder(order);
        }
    }

    private void validateSellOrder(Order order) {
        String userId = order.getUserId();
        Balance userBalance = walletService.getUserBalance(userId);
        if (userBalance.getBTCBalance() < order.getQuantity()) {
            throw new IllegalArgumentException("[ERROR] BTC 잔고가 부족합니다");
        }
    }

    private void validateBuyOrder(Order order) {
        String userId = order.getUserId();
        Balance userBalance = walletService.getUserBalance(userId);
        if (userBalance.getKRWBalance() < order.getQuantity()) {
            throw new IllegalArgumentException("[ERROR] KRW 잔고가 부족합니다");
        }
    }
}
