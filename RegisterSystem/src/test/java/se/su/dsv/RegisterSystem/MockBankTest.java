package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class MockBankTest {


    @Test
    void getRateFromAPIResultSuccessTest() {
        assertDoesNotThrow(() -> {MockBank.getRate(Currency.USD, Currency.SEK);});
    }
     
    @Test
    void exchangeIsCorrectTest() {
        Money money = new Money(new BigDecimal(10), Currency.USD);
        money = MockBank.exchange(money, Currency.SEK);
        assertEquals(Currency.SEK, money.getCurrency());
    }

    
}