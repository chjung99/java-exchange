package exchange.domain;

public enum ActionType {
    BUY, SELL;

    public static ActionType from(String rawType) {
        if (rawType == null) throw  new IllegalArgumentException("ActionType이 비어있습니다.");
        try {
            return ActionType.valueOf(rawType.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ActionType: " + rawType);
        }
    }
}
