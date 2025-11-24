package exchange.controller;

import exchange.service.WalletService;
import exchange.domain.BalanceData;
import exchange.view.InputView;

public class ExchangeController {
    private final InputView inputView;
    private final WalletService walletService;

    public ExchangeController(InputView inputView, WalletService walletService) {
        this.inputView = inputView;
        this.walletService = walletService;
    }

    public void run() {
        enrollUserWallets();
    }

    private void enrollUserWallets() {
        for (BalanceData balanceData: inputView.readBalance()){
            walletService.createWalletForUser(balanceData);
        }
    }
}
