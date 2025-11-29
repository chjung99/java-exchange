package exchange;

import exchange.controller.ExchangeController;
import exchange.domain.TradeEngine;
import exchange.repository.InMemoryOrderBook;
import exchange.repository.InMemoryWalletBTCRepository;
import exchange.repository.InMemoryWalletKRWRepository;
import exchange.repository.OrderBook;
import exchange.service.OrderService;
import exchange.service.WalletService;
import exchange.view.InputView;
import exchange.view.OutputView;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        WalletService walletService = new WalletService(new InMemoryWalletKRWRepository(), new InMemoryWalletBTCRepository());
        OrderBook orderBook = new InMemoryOrderBook();
        TradeEngine tradeEngine = new TradeEngine(orderBook, walletService);
        OrderService orderService = new OrderService(tradeEngine);
        ExchangeController exchangeController = new ExchangeController(inputView, outputView, walletService, orderService);

        exchangeController.run();
    }
}
