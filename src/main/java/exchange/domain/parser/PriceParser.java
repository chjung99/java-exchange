package exchange.domain.parser;

import exchange.message.ErrorMessage;

public class PriceParser extends AbstractDoubleParser{

    private PriceParser(){}

    @Override
    protected ErrorMessage formatErrorMessage() {
        return ErrorMessage.ERROR_INPUT_FORMAT;
    }

    public static double parse(String rawValue) {
        return new PriceParser().parseRaw(rawValue);
    }
}
