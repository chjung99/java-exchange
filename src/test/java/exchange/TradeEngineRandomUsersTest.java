package exchange;

import exchange.domain.*;
import exchange.repository.*;
import exchange.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TradeEngineRandomUsersTest {

    private WalletService walletService;
    private TradeEngine tradeEngine;
    private OrderBook orderBook;
    private List<String> userList;

    @BeforeEach
    void setUp() {
        // Repository 초기화
        InMemoryWalletKRWRepository krwRepo = new InMemoryWalletKRWRepository();
        InMemoryWalletBTCRepository btcRepo = new InMemoryWalletBTCRepository();
        InMemoryUserRepository userRepo = new InMemoryUserRepository();

        walletService = new WalletService(krwRepo, btcRepo, userRepo);

        // 유저 생성 및 초기 잔고 세팅
        userList = Arrays.asList("userA", "userB", "userC", "userD", "userE");
        Random random = new Random();
        for (String userId : userList) {
            double krw = 1_000_000 + random.nextInt(9_000_000); // 100만 ~ 1000만 KRW
            double btc = random.nextDouble() * 10;               // 0 ~ 10 BTC
            walletService.createWalletForUser(new BalanceData(userId, String.valueOf(krw), String.valueOf(btc)));
        }

        // OrderBook, TradeEngine 초기화
        orderBook = new InMemoryOrderBook();
        tradeEngine = new TradeEngine(orderBook, walletService);

        // 초기 SELL 주문 등록 (OrderBook에 체결용)
        Order initialSell = new Order("userC", 50_000, 5.0, ActionType.SELL, "2025-12-01T19:59:00");
        orderBook.addOrder(initialSell);

        // 🔹 초기 상태 출력
        System.out.println("=== 초기 잔고 ===");
        for (String userId : userList) {
            Balance balance = walletService.getUserBalance(userId);
            System.out.println(userId + " KRW: " + balance.getKRWBalance() + ", BTC: " + balance.getBTCBalance());
        }

        System.out.println("=== 초기 OrderBook ===");
        orderBook.getAllOrders().forEach(o ->
                System.out.println(o.getUserId() + " " + o.getActionType() + " " + o.getQuantity() + " BTC @ " + o.getPrice())
        );
    }

    @Test
    void multiThreadRandomUsersTestWithValidation() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Random random = new Random();

        // 거래 전 총액
        double totalKRWBefore = userList.stream()
                .mapToDouble(u -> walletService.getUserBalance(u).getKRWBalance())
                .sum();
        double totalBTCBefore = userList.stream()
                .mapToDouble(u -> walletService.getUserBalance(u).getBTCBalance())
                .sum();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    String userId = userList.get(random.nextInt(userList.size()));
                    ActionType action = random.nextBoolean() ? ActionType.BUY : ActionType.SELL;
                    double quantity = 0.01 + random.nextDouble() * 0.5;
                    double price = 49_000 + random.nextInt(2000);

                    Order order = new Order(userId, price, quantity, action, Long.toString(System.currentTimeMillis()));
                    TradeResult result = tradeEngine.execute(order);

                    synchronized (System.out) { // 로그 꼬임 방지
                        if (result.getTradeQuantity() > 0) {
                            System.out.println(Thread.currentThread().getName() + " - " +
                                    userId + " " + action + " " + quantity + " BTC @ " + price +
                                    " → 체결 " + result.getTradeQuantity() + " BTC @ " + result.getTradePrice());
                        } else {
                            System.out.println(Thread.currentThread().getName() + " - " +
                                    userId + " " + action + " " + quantity + " BTC @ " + price +
                                    " → 체결 없음, OrderBook 등록됨");
                        }
                    }

                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + " - Order 실행 중 에러: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // 최종 잔고 확인
        System.out.println("=== 최종 잔고 ===");
        double totalKRWAfter = 0;
        double totalBTCAfter = 0;
        for (String userId : userList) {
            Balance balance = walletService.getUserBalance(userId);
            System.out.println(userId + " KRW: " + balance.getKRWBalance() + ", BTC: " + balance.getBTCBalance());
            totalKRWAfter += balance.getKRWBalance();
            totalBTCAfter += balance.getBTCBalance();
        }

        // 자산 보존 검증
        System.out.println("=== 총 자산 검증 ===");
        System.out.println("KRW Before: " + totalKRWBefore + ", After: " + totalKRWAfter);
        System.out.println("BTC Before: " + totalBTCBefore + ", After: " + totalBTCAfter);

        if (Math.abs(totalKRWBefore - totalKRWAfter) > 1e-6 || Math.abs(totalBTCBefore - totalBTCAfter) > 1e-6) {
            System.out.println("[ERROR] 자산 불일치 발생! 멀티스레드 동시성 문제 의심");
        } else {
            System.out.println("[OK] 자산 일치, 멀티스레드 동시성 정상");
        }

        // 남은 OrderBook 검증
        System.out.println("=== OrderBook 남은 주문 ===");
        orderBook.getAllOrders().forEach(o ->
                System.out.println(o.getUserId() + " " + o.getActionType() + " " + o.getQuantity() + " BTC @ " + o.getPrice())
        );
    }
}