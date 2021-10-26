package se.su.dsv.RegisterSystem;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        assertThat(money, allOf(hasProperty("amount", is(DEFAULT_AMOUNT)),
                hasProperty("currency", is(Currency.USD))));

    }

    //Test whether null currency in constructor throws
    @Test
    void constructorThrowsIAEWhenCurrencyIsNullTest() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new Money(DEFAULT_AMOUNT, null);
        });
        assertThat(e.getMessage(), is("currency is null"));
    }

    //Test whether negatives amount in constructor throws
    @Test
    void constructorNegativeAmountTest() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new Money(NEGATIVE_AMOUNT, Currency.USD);
        });
        assertThat(e.getMessage(), is("amount is negative"));
    }

    //Test if null amount in constructor throws
    @Test
    void constructorNullAmountParameterTest() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new Money(null, Currency.USD);
        });
        assertThat(e.getMessage(), is("amount is null"));
    }

    //Test whether all accepted currencies are accepted
    @ParameterizedTest
    @EnumSource(Currency.class)
    void constructorValidCurrenciesTest(Currency currency) {
        Money money = new Money(DEFAULT_AMOUNT, currency);
        assertThat(money, hasProperty("currency", is(currency)));
    }

    //Test adding valid parameter
    @Test
    void addValidParameterTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money doubledMoney = money.add(money);
        BigDecimal expectedAmount = new BigDecimal("20.00");
        assertThat(doubledMoney, hasProperty("amount", is(expectedAmount)));
    }

    //Test whether adding another currency throws.
    @Test
    void addOtherCurrencyTest() {
        Money moneyUSD = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money moneySEK = new Money(DEFAULT_AMOUNT, Currency.SEK);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            moneyUSD.add(moneySEK);
        });
        assertThat(e.getMessage(), is("mismatching currencies!"));
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
        Money subtractedMoney = money.subtract(money);
        BigDecimal expectedAmount = new BigDecimal("0.00");
        assertThat(subtractedMoney, hasProperty("amount", is(expectedAmount)));
    }

    //Test whether subtracting another currency throws
    @Test
    void subtractOtherCurrencyTest() {
        Money moneyUSD = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money moneySEK = new Money(DEFAULT_AMOUNT, Currency.SEK);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            moneyUSD.subtract(moneySEK);
        });
        assertThat(e.getMessage(), is("mismatching currencies!"));
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
        Money moneyUSD = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money moneySEK = new Money(DEFAULT_AMOUNT, Currency.SEK);
        assertThat(moneyUSD, not(moneySEK));
    }

    //Test if equal money objects are equal
    @Test
    void isEqualTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money money2 = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertThat(money, is(money2));
    }

    //Tests if two equal even if they have .0 or not at the end
    @Test
    void differentScaleIsEqualTest() {
        Money money1 = new Money(new BigDecimal("0.0000"), Currency.USD);
        Money money2 = new Money(new BigDecimal(0), Currency.USD);
        assertThat(money1, is(money2));
    }

    //Test that if equals() is fed with null, the result is false.
    @Test
    void equalsNullIsFalseTest() {
        assertThat(new Money(DEFAULT_AMOUNT, Currency.USD), notNullValue());
    }

    //Test that make sure that if values are different in Money objects, equals() gives false.
    @Test
    void equalsNotSameAmountTest() {
        Money moneyValueTen = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money moneyValueZero = new Money(ZERO_AMOUNT, Currency.USD);
        assertThat(moneyValueTen, not(moneyValueZero));
    }

    //Test that compareTo() return 0 when compared to Object with same attribute values
    @Test
    void compareToObjectWithSameAttributeValuesTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money otherMoney = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertThat(money.compareTo(otherMoney), is(0));
    }

    //Test that compareTo() returns positive when this.amount is greater then other.amount
    @Test
    void compareToReturnsPositiveTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money otherMoney = new Money(BigDecimal.ZERO, Currency.USD);
        assertThat(money.compareTo(otherMoney), is(greaterThan(0)));
    }

    //Test that compareTo() return negative when this.amount is less then other.amount
    @Test
    void compareToReturnsNegativeTest() {
        Money money = new Money(BigDecimal.ZERO, Currency.USD);
        Money otherMoney = new Money(BigDecimal.TEN, Currency.USD);
        assertThat(money.compareTo(otherMoney), is(lessThan(0)));
    }

    //Test that compareTo() return positive when this.currency is greater then other.currency
    @Test
    void compareToThisCurrencyGreaterThenOtherCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.EUR);
        Money otherMoney = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertThat(money.compareTo(otherMoney), is(lessThan(0)));
    }

    //Test that compareTo() return -1 when this.currency is less then other.currency
    @Test
    void compareToThisCurrencyLessThenOtherCurrencyTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        Money otherMoney = new Money(DEFAULT_AMOUNT, Currency.EUR);
        assertThat(money.compareTo(otherMoney), is(greaterThan(0)));
    }

    @Test
    void moneyToStringFormatTest() {
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertThat(money, hasToString(equalTo("10.00 USD")));
    }

}