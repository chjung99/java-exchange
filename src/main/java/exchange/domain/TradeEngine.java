package exchange.domain;

import exchange.repository.OrderBook;
import exchange.service.WalletService;

import java.util.concurrent.locks.ReentrantLock;

public class TradeEngine {
    private final OrderBook orderBook;
    private final WalletService walletService;
    private final ReentrantLock engineLock = new ReentrantLock();

    public TradeEngine(OrderBook orderBook, WalletService walletService) {
        this.orderBook = orderBook;
        this.walletService = walletService;
    }

    public TradeResult execute(Order incomingOrder) {
        engineLock.lock();
        try {
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

            validateOrder(incomingOrder, counterOrder);

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
        } finally {
            engineLock.unlock();
        }
    }

    private void validateOrder(Order incomingOrder, Order counterOrder) {
        if (incomingOrder.isBuy()) {
            validateBuyOrder(incomingOrder, counterOrder);
        }
        if (incomingOrder.isSell()) {
            validateSellOrder(incomingOrder);
        }
    }

    private void validateSellOrder(Order incomingOrder) {
        String userId = incomingOrder.getUserId();
        Balance userBalance = walletService.getUserBalance(userId);
        if (userBalance.getBTCBalance() < incomingOrder.getQuantity()) {
            throw new IllegalArgumentException("[ERROR] BTC 잔고가 부족합니다");
        }
    }

    private void validateBuyOrder(Order incomingOrder, Order counterOrder) {
        String userId = incomingOrder.getUserId();
        Balance userBalance = walletService.getUserBalance(userId);
        if (userBalance.getKRWBalance() < incomingOrder.getQuantity() * counterOrder.getPrice()) {
            throw new IllegalArgumentException("[ERROR] KRW 잔고가 부족합니다");
        }
    }
}
