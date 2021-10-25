package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BankTest {
    static final BigDecimal DEFAULT_VALUE = new BigDecimal(10);
    BankService bank;

    @BeforeEach
    void Initialize() {
        bank = new MockBank();
    }


    //Test that the exchange rate is returned successfully from bank
    @Test
    void getRateFromAPIResultSuccessTest() {
        assertDoesNotThrow(() -> {
            bank.getRate(Currency.USD, Currency.SEK);
        });
    }

    //Test that the exchange method returns a new money object of a new currency according to the passed parameters
    @Test
    void exchangeGivesNewCurrencyTest() throws IOException {
        Money money = new Money(new BigDecimal(10), Currency.USD);
        money = bank.exchange(money, Currency.SEK);
        assertEquals(Currency.SEK, money.getCurrency());
    }

    //Test that the exchange method returns a money object with expected amount as per the exchange rate
    @Test
    void exchangeNewAmountCorrect() throws IOException {
        Money money = new Money(DEFAULT_VALUE, Currency.USD);
        money = bank.exchange(money, Currency.SEK);
        BigDecimal rate = bank.getRate(Currency.USD, Currency.SEK);

        BigDecimal newAmount = DEFAULT_VALUE.multiply(rate);

        assertEquals(newAmount, money.getAmount());
    }

    //Test that the exchange with supplied rate converts the amount correctly
    @Test
    void exchangeWithRateParameterNewAmountCorrect() {
        BigDecimal rate = new BigDecimal(2);
        Money money = new Money(DEFAULT_VALUE, Currency.USD);
        money = bank.exchange(money, Currency.SEK, rate);
        assertEquals(DEFAULT_VALUE.multiply(rate), money.getAmount());
    }
}