package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;

import se.su.dsv.RegisterSystem.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {

    private static final String FAKE_CURRENCY = "NOK";
    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(10);
    private static final BigDecimal NEGATIVE_AMOUNT = new BigDecimal(-1);
    private static final BigDecimal ZERO_AMOUNT = new BigDecimal(0);

    @Test
    public void constructorValidParameterTest(){
        Money money = new Money(DEFAULT_AMOUNT, Currency.USD);
        assertEquals(DEFAULT_AMOUNT, money.getAmount());
        assertEquals(money.getCurrency(), Currency.USD);
    }

    @Test
    public void constructorThrowsIAEWhenCurrencyIsNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {new Money(DEFAULT_AMOUNT, null);});
    }

    @Test
    public void constructorNegativeAmountTest(){

        assertThrows(IllegalArgumentException.class, () -> {new Money(NEGATIVE_AMOUNT, Currency.USD);});
    }

//    @Test
//    public void constructorUnlistedCurrencyParameterTest(){
//        assertThrows(IllegalArgumentException.class, () -> {new Money(DEFAULT_AMOUNT, "hej");});
//    }

    @Test
    public void constructorNullAmountParameterTest(){
        assertThrows(NullPointerException.class, () -> {new Money(null, Currency.USD);});
    }

//    @ParameterizedTest
//    @CsvSource({"10, null, currency is null", "-1, USD", "10, FAKE_CURRENCY", "null, USD"})
//    public void constructorThrowsIllegalArgumentExceptionTest(BigDecimal amount, String currency, String errorMessage){
//        assertThrows(IllegalArgumentException.class, () -> {new Money(amount, currency)});
//    }


}