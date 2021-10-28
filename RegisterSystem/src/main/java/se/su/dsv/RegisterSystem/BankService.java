package se.su.dsv.RegisterSystem;

import java.io.IOException;
import java.math.BigDecimal;

public interface BankService {
    BigDecimal getRate(Currency from, Currency to) throws IOException;

    Money exchange(Money money, Currency currency) throws IOException;

    Money exchange(Money money, Currency currency, BigDecimal rate);
}
