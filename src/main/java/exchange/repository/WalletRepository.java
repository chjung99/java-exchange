package exchange.repository;

import exchange.domain.Wallet;

public interface WalletRepository {
    public void save(String userId, Wallet wallet);
    public Wallet findByUserId(String userId);
}
