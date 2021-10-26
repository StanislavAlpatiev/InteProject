package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal("10.00");
    private static final BigDecimal NEGATIVE_AMOUNT = new BigDecimal(-1);
    private static final BigDecimal ZERO_AMOUNT = new BigDecimal(0);

    //Test constructor with valid parameters
    @Test
    void constructorValidParameterTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertEquals(DEFAULT_AMOUNT, money.getAmount());
        assertEquals(Currency.USD, money.getCurrency());
    }

    //Test whether null currency in constructor throws
    @Test
    void constructorThrowsIAEWhenCurrencyIsNullTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Money(DEFAULT_AMOUNT, null);
        });
    }

    //Test whether negatives amount in constructor throws
    @Test
    void constructorNegativeAmountTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Money(NEGATIVE_AMOUNT, Currency.USD);
        });
    }

    //Test if null amount in constructor throws
    @Test
    void constructorNullAmountParameterTest() {
        assertThrows(NullPointerException.class, () -> {
            new Money(null, Currency.USD);
        });
    }

    //Test whether all accepted currencies are accepted
    @ParameterizedTest
    @EnumSource(Currency.class)
    void constructorValidCurrenciesTest(Currency currency) {
        Money money = new Money(DEFAULT_AMOUNT, currency);
        //assertSame(DEFAULT_AMOUNT, money.getAmount());
        assertEquals(currency, money.getCurrency());
    }

    //Test adding valid parameter
    @Test
    void addValidParameterTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = money.add(new Money(DEFAULT_AMOUNT, Currency.USD));
        assertEquals(money.getAmount().add(DEFAULT_AMOUNT), money2.getAmount());
    }

    //Test whether adding another currency throws.
    @Test
    void addOtherCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = new Money(DEFAULT_AMOUNT, Currency.SEK);
        assertThrows(IllegalArgumentException.class, () -> {
            money.add(money2);
        });
    }

    //Test whether adding null gives a null pointer exception
    @Test
    void addNullTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertThrows(NullPointerException.class, () -> {
            money.add(null);
        });
    }

    //Test whether subtract works when fed correct parameters
    @Test
    void subtractValidParameterTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = money.subtract(new Money(DEFAULT_AMOUNT, Currency.USD));
        assertSame(money2.getAmount(), money.getAmount().subtract(DEFAULT_AMOUNT));
    }

    //Test whether subtracting another currency throws
    @Test
    void subtractOtherCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = new Money(DEFAULT_AMOUNT, Currency.SEK);
        assertThrows(IllegalArgumentException.class, () -> {
            money.subtract(money2);
        });
    }

    //Test whether subtracting null gives a null pointer exception
    @Test
    void subtractNullTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertThrows(NullPointerException.class, () -> {
            money.subtract(null);
        });
    }

    //Test whether differing currencies leads to a false result from equals()
    @Test
    void equalsNotSameCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertNotEquals(Currency.class, money);
    }

    //Test if equal money objects are equal
    @Test
    void isEqualTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertEquals(money2, money);
    }

    //Tests if two equal even if they have .0 or not at the end
    @Test
    void differentScaleIsEqualTest() {
        Money money = new Money(new BigDecimal(0.0000), Currency.USD);
        Money money2 = new Money(new BigDecimal(0), Currency.USD);
        assertEquals(money, money2);
    }

    //Test that if equals() is fed with null, the result is false.
    @Test
    void equalsNullIsFalseTest() {
        assertNotEquals(null, new Money(DEFAULT_AMOUNT, Currency.USD));
    }

    //Test that make sure that if values are different in Money objects, equals() gives false.
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
        assertTrue((money.compareTo(otherMoney) < 0));
    }

    //Test that compareTo() return -1 when this.currency is less then other.currency
    @Test
    void compareToThisCurrencyLessThenOtherCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money otherMoney = new Money(DEFAULT_AMOUNT, Currency.EUR);
        assertTrue(money.compareTo(otherMoney) > 0);
    }

    @Test
    void moneyToStringFormatTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertEquals("10 USD", money.toString());
    }

}