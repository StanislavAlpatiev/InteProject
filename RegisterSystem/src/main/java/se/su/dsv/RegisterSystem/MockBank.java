package se.su.dsv.RegisterSystem;

import java.io.IOException;
import java.math.BigDecimal;

public class MockBank implements BankService {
    
    @Override
    public BigDecimal getRate(Currency from, Currency to) throws IOException {
        if(from == to) {
            return BigDecimal.valueOf(1);
        }
        if(from == Currency.USD){
            return BigDecimal.valueOf(10);
        } else if (from == Currency.SEK){
            return BigDecimal.valueOf(0.1);
        }
        else return BigDecimal.valueOf(0.5);
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
