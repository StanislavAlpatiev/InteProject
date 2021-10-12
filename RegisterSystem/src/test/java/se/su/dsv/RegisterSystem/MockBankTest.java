package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;

class MockBankTest {
    static final BigDecimal DEFAULT_VALUE = new BigDecimal(10);


    @Test
    void getRateFromAPIResultSuccessTest() {
        assertDoesNotThrow(() -> {MockBank.getRate(Currency.USD, Currency.SEK);});
    }
     
    @Test
    void exchangeGivesNewCurrencyTest() throws IOException {
        Money money = new Money(new BigDecimal(10), Currency.USD);
        money = MockBank.exchange(money, Currency.SEK);
        assertEquals(Currency.SEK, money.getCurrency());
    }

    @Test
    void exchangeNewAmountCorrect() throws IOException {
        Money money = new Money(DEFAULT_VALUE, Currency.USD);
        money = MockBank.exchange(money, Currency.SEK);
        BigDecimal rate = MockBank.getRate(Currency.USD, Currency.SEK);
        
        BigDecimal newAmount = DEFAULT_VALUE.multiply(rate);

        assertEquals(newAmount, money.getAmount());
    }

    
}