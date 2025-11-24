package exchange;

import exchange.controller.ExchangeController;
import exchange.repository.InMemoryWalletBTCRepository;
import exchange.repository.InMemoryWalletKRWRepository;
import exchange.service.WalletService;
import exchange.view.InputView;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        InputView inputView = new InputView();
        WalletService walletService = new WalletService(new InMemoryWalletKRWRepository(), new InMemoryWalletBTCRepository());
        ExchangeController exchangeController = new ExchangeController(inputView, walletService);

        exchangeController.run();
    }
}
