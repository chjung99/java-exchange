package exchange.view;

import exchange.domain.ActionType;
import exchange.domain.Balance;
import exchange.domain.Order;
import exchange.domain.TradeResult;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OutputView {
    private static final String PRINT_TRADE_RESULT_MESSAGE = "개의 주문이 체결되었습니다.\n";
    private static final String PRINT_TRADE_MESSAGE = "[ORDER EXECUTED]";
    private static final String PRINT_BALANCE_MESSAGE = "[FINAL BALANCE]";
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    public void printExecutedTrade(List<TradeResult> results) {
        List<TradeResult> buyOrders = getBuyOrders(results);
        System.out.println(buyOrders.size() + PRINT_TRADE_RESULT_MESSAGE);

        for (TradeResult tradeResult : buyOrders) {
            Order order = tradeResult.getOrder();
            String userId = order.getUserId();
            double tradePrice = tradeResult.getTradePrice();
            double tradeQuantity = tradeResult.getTradeQuantity();
            System.out.println(PRINT_TRADE_MESSAGE + " " + userId + " BTC " +
                    decimalFormat.format(tradePrice) + " " +
                    tradeQuantity);
        }
    }

    private List<TradeResult> getBuyOrders(List<TradeResult> results) {
        List<TradeResult> buyOrders = new ArrayList<>();
        for (TradeResult tradeResult : results) {
            if (tradeResult.getOrder().getActionType().equals(ActionType.BUY)) {
                buyOrders.add(tradeResult);
            }
        }
        return buyOrders;
    }

    public void printBalanceSummary(List<Balance> userBalances) {
        System.out.println(PRINT_BALANCE_MESSAGE);

        for (Balance balance : userBalances) {
            System.out.println(balance.getUserId()+" - " + "KRW: " +
                    decimalFormat.format(balance.getKRWBalance())+ ", "+ "BTC: " +
                    balance.getBTCBalance());
        }
    }
}
