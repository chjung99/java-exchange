package exchange.controller;

import exchange.domain.Balance;
import exchange.domain.OrderData;
import exchange.domain.TradeResult;
import exchange.service.OrderService;
import exchange.service.WalletService;
import exchange.domain.BalanceData;
import exchange.view.InputView;
import exchange.view.OutputView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class ExchangeController {
    private final InputView inputView;
    private final OutputView outputView;
    private final WalletService walletService;
    private final OrderService orderService;

    public ExchangeController(InputView inputView, OutputView outputView, WalletService walletService, OrderService orderService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.walletService = walletService;
        this.orderService = orderService;
    }

    public void run() {
        enrollUserWallets();
        executeOrders();
        showFinalBalanceSummary();
    }

    private void enrollUserWallets() {
        for (BalanceData balanceData: inputView.readBalance()){
            walletService.createWalletForUser(balanceData);
        }
    }

    private void executeOrders() {
        List<Future<TradeResult>> futures = new ArrayList<>();

        for (OrderData orderData : inputView.readOrder()) {
            futures.add(orderService.processOrderAsync(orderData));
        }

        List<TradeResult> results = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        outputView.printExecutedTrade(results);
    }

    private void showFinalBalanceSummary() {
        outputView.printBalanceSummary(getUserBalances());
    }

    private List<Balance> getUserBalances() {
        List< Balance> userBalances = new ArrayList<>();

        List<String> users = walletService.getAllUser();
        for (String userId : users) {
            userBalances.add(walletService.getUserBalance(userId));
        }

        return userBalances;
    }
}
