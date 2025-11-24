package exchange.domain;

public class Wallet extends AbstractWallet{

    public Wallet(String userId, WalletType type, double balance) {
        super(userId, type, balance);
    }
}
