package exchange.message;

public enum ErrorMessage {

    ERROR_INPUT_FORMAT("잘못된 입력입니다.");

    private static final String errorMessageTag = "[ERROR] ";

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String formatted() {
        return errorMessageTag + message;
    }
}
