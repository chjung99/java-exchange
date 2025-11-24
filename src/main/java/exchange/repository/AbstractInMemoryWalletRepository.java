package exchange.repository;

import exchange.domain.Wallet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractInMemoryWalletRepository implements WalletRepository {
    private final Map<String, Wallet> walletMap = new ConcurrentHashMap<>();

    @Override
    public void save(String userId, Wallet wallet) {
        walletMap.put(userId, wallet);
    }

    @Override
    public Wallet findByUserId(String userId) {
        return walletMap.get(userId);
    }
}
