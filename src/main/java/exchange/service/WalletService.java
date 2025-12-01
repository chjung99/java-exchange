package exchange.service;

import exchange.domain.*;
import exchange.domain.parser.BalanceParser;
import exchange.domain.parser.UserIdParser;
import exchange.repository.UserRepository;
import exchange.repository.WalletRepository;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class WalletService {

    private final Map<WalletType, WalletRepository> walletRepositoryMap = new EnumMap<>(WalletType.class);
    private final UserRepository userRepository;
    private final ReentrantLock walletLock = new ReentrantLock();

    public WalletService(WalletRepository walletKRWRepository, WalletRepository walletBTCRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        walletRepositoryMap.put(WalletType.KRW, walletKRWRepository);
        walletRepositoryMap.put(WalletType.BTC, walletBTCRepository);
    }

    private void createWallet(String rawUserId, WalletType walletType, String rawBalance) {
        String userId = UserIdParser.parse(rawUserId);
        double balance = BalanceParser.parse(rawBalance);

        userRepository.save(userId);

        Wallet wallet = new Wallet(userId, walletType, balance);
        WalletRepository walletRepository = walletRepositoryMap.get(walletType);

        if (walletRepository == null) {
            throw new IllegalArgumentException("Unsupported wallet type: " + walletType);
        }

        walletRepository.save(userId, wallet);
    }

    public void createWalletForUser(BalanceData balanceData) {
        createWallet(balanceData.rawUserId, WalletType.KRW, balanceData.rawKRWBalance);
        createWallet(balanceData.rawUserId, WalletType.BTC, balanceData.rawBTCBalance);
    }

    public void updateBalance(Order incomingOrder, Order counterOrder, double tradePrice, double tradeQuantity) {
        walletLock.lock();
        try{
            WalletRepository BTCwalletRepository = walletRepositoryMap.get(WalletType.BTC);
            WalletRepository KRWwalletRepository = walletRepositoryMap.get(WalletType.KRW);

            String buyerId = incomingOrder.isBuy() ? incomingOrder.getUserId() : counterOrder.getUserId();
            String sellerId = incomingOrder.isSell() ? incomingOrder.getUserId() : counterOrder.getUserId();

            BTCwalletRepository.findByUserId(sellerId).withdraw(tradeQuantity);
            BTCwalletRepository.findByUserId(buyerId).deposit(tradeQuantity);

            KRWwalletRepository.findByUserId(buyerId).withdraw(tradeQuantity * tradePrice);
            KRWwalletRepository.findByUserId(sellerId).deposit(tradeQuantity * tradePrice);
        } finally {
            walletLock.unlock();
        }

    }

    public Balance getUserBalance(String userId) {
        WalletRepository BTCwalletRepository = walletRepositoryMap.get(WalletType.BTC);
        WalletRepository KRWwalletRepository = walletRepositoryMap.get(WalletType.KRW);

        Wallet KRWWallet = KRWwalletRepository.findByUserId(userId);
        Wallet BTCWallet = BTCwalletRepository.findByUserId(userId);

        //todo: 둘 다 double 형이라 바뀌어 입력 들어가도 컴파일 시 검증이 안됨 -> 별도의 타입 필요

        return new Balance(userId, KRWWallet.getBalance(), BTCWallet.getBalance());
    }

    public List<String> getAllUser() {
        return userRepository.getAllUser();
    }
}
