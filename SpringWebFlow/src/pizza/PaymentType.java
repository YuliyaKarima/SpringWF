package pizza;
import static org.apache.commons.lang.WordUtils.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public enum PaymentType implements Serializable{
    CASH, CHECK, CREDIT_CARD;

    public static List<PaymentType> asList() {
        PaymentType[] all = PaymentType.values();
        return Arrays.asList(all);
    }

    @Override
    public String toString() {
        return capitalizeFully(name().replace('_', ' '));
    }
}
