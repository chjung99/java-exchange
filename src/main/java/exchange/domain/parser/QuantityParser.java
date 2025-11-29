package exchange.domain.parser;

import exchange.message.ErrorMessage;

public class QuantityParser extends AbstractDoubleParser{
    private QuantityParser() {}

    @Override
    protected ErrorMessage formatErrorMessage() {
        return ErrorMessage.ERROR_INPUT_FORMAT;
    }

    public static double parse(String rawValue) {
        return new QuantityParser().parseRaw(rawValue);
    }
}
