package se.su.dsv.RegisterSystem;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;

public class MockBank implements BankService {

    private HashMap<Currency, EnumMap<Currency, BigDecimal>> exchangeRates;

    @Override
    public BigDecimal getRate(Currency from, Currency to) throws IOException {
        if (from == to) {
            return BigDecimal.valueOf(1);
        }
        if (from == Currency.SEK) {
            return BigDecimal.valueOf(0.1);
        } else {
            return BigDecimal.valueOf(10);
        }

    }

    @Override
    public Money exchange(Money money, Currency currency) throws IOException {
        BigDecimal rate = getRate(money.getCurrency(), currency);
        return exchange(money, currency, rate);
    }

    @Override
    public Money exchange(Money money, Currency currency, BigDecimal rate) {
        BigDecimal newAmount = money.getAmount().multiply(rate);
        return new Money(newAmount, currency);
    }

}
