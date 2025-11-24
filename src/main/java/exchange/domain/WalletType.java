package exchange.domain;

public enum WalletType {
    KRW, BTC;

    public static WalletType from(String rawType) {
        if (rawType == null) throw  new IllegalArgumentException("WalletType이 비어있습니다.");
        try {
            return WalletType.valueOf(rawType.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid walletType: " + rawType);
        }
    }
}
