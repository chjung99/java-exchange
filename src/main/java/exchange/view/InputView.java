package exchange.view;

import exchange.domain.BalanceData;
import exchange.domain.OrderData;
import exchange.message.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

import static camp.nextstep.edu.missionutils.Console.readLine;

public class InputView {
    private static final String SPLITTER = ",";
    private static final String END_OF_LINE_MESSAGE = "END";
    private static final String READ_BALANCE_MESSAGE = "사용자 잔고를 입력해 주세요.";
    private static final String READ_ORDER_MESSAGE = "체결할 주문을 입력해 주세요.";


    public List<BalanceData> readBalance() {
        System.out.println(READ_BALANCE_MESSAGE);
        List<BalanceData> userBalances = new ArrayList<>();

        while (true) {
            String line = readLine();

            if (isEndOfInput(line)) {
                break;
            }

            userBalances.add(parseBalanceLine(line));
        }
        return userBalances;
    }

    private boolean isEndOfInput(String line) {
        return line.trim().equals(END_OF_LINE_MESSAGE);
    }

    private BalanceData parseBalanceLine(String line) {
        String[] fields = line.split(SPLITTER);
        if (fields.length != 3) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_INPUT_FORMAT.formatted());
        }
        return new BalanceData(fields[0], fields[1], fields[2]);
    }

    public List<OrderData> readOrder() {
        System.out.println(READ_ORDER_MESSAGE);
        List<OrderData> userOrders = new ArrayList<>();

        while (true) {
            String line = readLine();

            if (isEndOfInput(line)) {
                break;
            }

            userOrders.add(parseOrderLine(line));
        }
        return userOrders;
    }

    private OrderData parseOrderLine(String line) {
        String[] fields = line.split(SPLITTER);
        if (fields.length != 5) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_INPUT_FORMAT.formatted());
        }
        return new OrderData(fields[0], fields[1], fields[2], fields[3], fields[4]);
    }
}
