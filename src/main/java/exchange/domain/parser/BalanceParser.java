package exchange.domain.parser;

import exchange.message.ErrorMessage;

public class BalanceParser extends AbstractBalanceParser{

    private BalanceParser(){}

    @Override
    protected ErrorMessage formatErrorMessage() {
        return ErrorMessage.ERROR_INPUT_FORMAT;
    }

    public static double parse(String rawValue) {
        return new BalanceParser().parseRaw(rawValue);
    }
}
