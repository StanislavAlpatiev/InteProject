package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;

class BankTest {
    static final BigDecimal DEFAULT_VALUE = new BigDecimal(10); 
    BankService bank;
    
    @BeforeEach
    void Initialize(){
        bank = new MockBank();
    } 


    @Test
    void getRateFromAPIResultSuccessTest() {
        assertDoesNotThrow(() -> {
            bank.getRate(Currency.USD, Currency.SEK);
        });
    }

    @Test
    void exchangeGivesNewCurrencyTest() throws IOException {
        Money money = new Money(new BigDecimal(10), Currency.USD);
        money = bank.exchange(money, Currency.SEK);
        assertEquals(Currency.SEK, money.getCurrency());
    }

    @Test
    void exchangeNewAmountCorrect() throws IOException {
        Money money = new Money(DEFAULT_VALUE, Currency.USD);
        money = bank.exchange(money, Currency.SEK);
        BigDecimal rate = bank.getRate(Currency.USD, Currency.SEK);

        BigDecimal newAmount = DEFAULT_VALUE.multiply(rate);

        assertEquals(newAmount, money.getAmount());
    }

    @Test
    void exchangeWithRateParameterNewAmountCorrect() {
        BigDecimal rate = new BigDecimal(2);
        Money money = new Money(DEFAULT_VALUE, Currency.USD);
        money = bank.exchange(money, Currency.SEK, rate);
        assertEquals(DEFAULT_VALUE.multiply(rate), money.getAmount());
    }


}