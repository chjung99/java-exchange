package exchange.domain.parser;

import exchange.message.ErrorMessage;

public class UserIdParser extends AbstractStringParser{

    private UserIdParser(){}

    public static String parse(String rawUserId) {
        return new UserIdParser().parseRaw(rawUserId);
    }

    @Override
    protected ErrorMessage formatErrorMessage() {
        return ErrorMessage.ERROR_INPUT_FORMAT;
    }
}
