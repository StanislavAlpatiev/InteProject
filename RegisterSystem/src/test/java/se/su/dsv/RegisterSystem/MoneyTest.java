package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class MoneyTest {

    //private static final String FAKE_CURRENCY = "NOK";
    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(10);
    private static final BigDecimal NEGATIVE_AMOUNT = new BigDecimal(-1);
    private static final BigDecimal ZERO_AMOUNT = new BigDecimal(0);

    @Test
    void constructorValidParameterTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertEquals(DEFAULT_AMOUNT, money.getAmount());
        assertEquals(Currency.USD, money.getCurrency());
    }

    @Test
    void constructorThrowsIAEWhenCurrencyIsNullTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Money(DEFAULT_AMOUNT, null);
        });
    }

    @Test
    void constructorNegativeAmountTest() {

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
    void constructorNullAmountParameterTest() {
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
    void constructorValidCurrenciesTest(Currency currency) {
        Money money = new Money(DEFAULT_AMOUNT, currency);
        //assertSame(DEFAULT_AMOUNT, money.getAmount());
        assertEquals(currency, money.getCurrency()); 
    }

    @Test
    void addValidParameterTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = money.add(new Money(DEFAULT_AMOUNT, Currency.USD));
        assertEquals(money.getAmount().add(DEFAULT_AMOUNT), money2.getAmount());
    }

    @Test
    void addOtherCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = new Money(DEFAULT_AMOUNT, Currency.SEK);
        assertThrows(IllegalArgumentException.class, () -> {
            money.add(money2);
        });
    }

    @Test
    void addNullTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertThrows(NullPointerException.class, () -> {
            money.add(null);
        });
    }

    @Test
    void subtractValidParameterTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = money.subtract(new Money(DEFAULT_AMOUNT, Currency.USD));
        assertSame(money2.getAmount(), money.getAmount().subtract(DEFAULT_AMOUNT));
    }

    @Test
    void subtractOtherCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = new Money(DEFAULT_AMOUNT, Currency.SEK);
        assertThrows(IllegalArgumentException.class, () -> {
            money.subtract(money2);
        });
    }

    @Test
    void subtractNullTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertThrows(NullPointerException.class, () -> {
            money.subtract(null);
        });
    }

    @Test
    void isNotEqualTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertNotEquals(Currency.class, money);
    }

    @Test
    void isEqualTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertEquals(money2, money);
    }

    @Test
    void equalsNullIsFalseTest() {
        assertNotEquals(null, new Money(DEFAULT_AMOUNT, Currency.USD));
    }

    @Test
    void equalsNotSameAmountTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = new Money(ZERO_AMOUNT, Currency.USD);
        assertNotEquals(money2, money);
    }

    //Test that compareTo() return 0 when compared to Object with same attribute values
    @Test
    void compareToObjectWithSameAttributeValuesTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money otherMoney = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertEquals(0, money.compareTo(otherMoney));
    }

    //Test that compareTo() return 1 when this.amount is greater then other.amount
    @Test
    void compareToReturnsPositiveTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money otherMoney = new Money(BigDecimal.ZERO, Currency.USD);
        assertEquals(1, money.compareTo(otherMoney));
    }

    //Test that compareTo() return -1 when this.amount is less then other.amount
    @Test
    void compareToReturnsNegativeTest() {
        Money money = new Money(BigDecimal.ZERO, Currency.USD);
        Money otherMoney = new Money(BigDecimal.TEN, Currency.USD);
        assertEquals(-1, money.compareTo(otherMoney));
    }

    //Test that compareTo() return 1 when this.currency is greater then other.currency
    @Test
    void compareToThisCurrencyGreaterThenOtherCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.EUR);
        Money otherMoney = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertTrue((money.compareTo(otherMoney) > 0));
    }

    //Test that compareTo() return -1 when this.currency is less then other.currency
    @Test
    void compareToThisCurrencyLessThenOtherCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money otherMoney = new Money(DEFAULT_AMOUNT, Currency.EUR);
        assertTrue(money.compareTo(otherMoney) < 0);
    }
}