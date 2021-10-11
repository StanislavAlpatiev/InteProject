package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class MoneyTest {

    private static final String FAKE_CURRENCY = "NOK";
    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(10);
    private static final BigDecimal NEGATIVE_AMOUNT = new BigDecimal(-1);
    private static final BigDecimal ZERO_AMOUNT = new BigDecimal(0);

    @Test
    public void constructorValidParameterTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertEquals(DEFAULT_AMOUNT, money.getAmount());
        assertEquals(money.getCurrency(), Currency.USD);
    }

    @Test
    public void constructorThrowsIAEWhenCurrencyIsNullTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Money(DEFAULT_AMOUNT, null);
        });
    }

    @Test
    public void constructorNegativeAmountTest() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Money(NEGATIVE_AMOUNT, Currency.USD);
        });
    }

    // @Test
    // public void constructorUnlistedCurrencyParameterTest(){
    // assertThrows(IllegalArgumentException.class, () -> {new Money(DEFAULT_AMOUNT,
    // "hej");});
    // }

    @Test
    public void constructorNullAmountParameterTest() {
        assertThrows(NullPointerException.class, () -> {
            new Money(null, Currency.USD);
        });
    }

    // @ParameterizedTest
    // @CsvSource({"10, null, currency is null", "-1, USD", "10, FAKE_CURRENCY",
    // "null, USD"})
    // public void constructorThrowsIllegalArgumentExceptionTest(BigDecimal amount,
    // String currency, String errorMessage){
    // assertThrows(IllegalArgumentException.class, () -> {new Money(amount,
    // currency)});
    // }

    @ParameterizedTest
    @EnumSource(Currency.class)
    public void constructorValidCurrenciesTest(Currency currency) {
        Money money = new Money(DEFAULT_AMOUNT, currency);
        assertSame(DEFAULT_AMOUNT, money.getAmount());
        assertSame(currency, money.getCurrency());
    }

    @Test
    public void addValidParameterTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = money.add(new Money(DEFAULT_AMOUNT, Currency.USD));
        assertEquals(money.getAmount().add(DEFAULT_AMOUNT), money2.getAmount());
    }

    @Test
    public void addOtherCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = new Money(DEFAULT_AMOUNT, Currency.SEK);
        assertThrows(IllegalArgumentException.class, () -> {
            money.add(money2);
        });
    }

    @Test
    public void addNullTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertThrows(NullPointerException.class, () -> {
            money.add(null);
        });
    }

    @Test
    public void subtractValidParameterTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = money.subtract(new Money(DEFAULT_AMOUNT, Currency.USD));
        assertSame(money2.getAmount(), money.getAmount().subtract(DEFAULT_AMOUNT));
    }

    @Test
    public void subtractOtherCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = new Money(DEFAULT_AMOUNT, Currency.SEK);
        assertThrows(IllegalArgumentException.class, () -> {
            money.subtract(money2);
        });
    }

    @Test
    public void subtractNullTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertThrows(NullPointerException.class, () -> {
            money.subtract(null);
        });
    }

}