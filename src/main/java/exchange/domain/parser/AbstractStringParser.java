package exchange.domain.parser;

import exchange.message.ErrorMessage;

public abstract class AbstractStringParser {

    protected abstract ErrorMessage formatErrorMessage();

    protected String parseRaw(String rawValue) {
        validateNotBlank(rawValue);
        return rawValue;
    }

    protected void validateNotBlank(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new IllegalArgumentException(formatErrorMessage().formatted());
        }
    }
}
