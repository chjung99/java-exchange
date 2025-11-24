package exchange.service;

import exchange.domain.Wallet;
import exchange.domain.WalletType;
import exchange.domain.parser.BalanceParser;
import exchange.domain.parser.UserIdParser;
import exchange.repository.WalletRepository;
import exchange.domain.BalanceData;

import java.util.EnumMap;
import java.util.Map;

public class WalletService {

    private final Map<WalletType, WalletRepository> walletRepositoryMap = new EnumMap<>(WalletType.class);

    public WalletService(WalletRepository walletKRWRepository, WalletRepository walletBTCRepository) {
        walletRepositoryMap.put(WalletType.KRW, walletKRWRepository);
        walletRepositoryMap.put(WalletType.BTC, walletKRWRepository);
    }

    private void createWallet(String rawUserId, WalletType walletType, String rawBalance) {
        String userId = UserIdParser.parse(rawUserId);
        double balance = BalanceParser.parse(rawBalance);

        Wallet wallet = new Wallet(userId, walletType, balance);
        WalletRepository walletRepository = walletRepositoryMap.get(walletType);

        if (walletRepository == null) {
            throw new IllegalArgumentException("Unsupported wallet type: " + walletType);
        }

        walletRepository.save(userId, wallet);
    }

    public void createWalletForUser(BalanceData balanceData) {
        createWallet(balanceData.rawUserId, WalletType.KRW, balanceData.rawKRWBalance);
        createWallet(balanceData.rawUserId, WalletType.KRW, balanceData.rawBTCBalance);
    }
}
