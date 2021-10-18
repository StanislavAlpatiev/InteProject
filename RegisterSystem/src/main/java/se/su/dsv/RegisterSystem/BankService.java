package se.su.dsv.RegisterSystem;

import java.io.IOException;
import java.math.BigDecimal;

public interface BankService {
    public BigDecimal getRate(Currency from, Currency to) throws IOException;
    public Money exchange(Money money, Currency currency) throws IOException;
    public Money exchange(Money money, Currency currency, BigDecimal rate);
}
