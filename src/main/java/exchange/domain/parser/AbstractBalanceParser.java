package exchange.domain.parser;

import exchange.message.ErrorMessage;

public abstract class AbstractBalanceParser {

    protected abstract ErrorMessage formatErrorMessage();

    protected double parseRaw(String rawValue) {
        validateNotBlank(rawValue);
        return parseToDouble(rawValue);
    }

    protected void validateNotBlank(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new IllegalArgumentException(formatErrorMessage().formatted());
        }
    }

    private double parseToDouble(String rawValue) {
        try {
            return Double.parseDouble(rawValue.trim());
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(formatErrorMessage().formatted());
        }
    }
}
