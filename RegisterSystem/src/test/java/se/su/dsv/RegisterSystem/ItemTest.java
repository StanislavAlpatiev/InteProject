package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;

class ItemTest {

    private static final Item DEFAULT_SMALL_BEVERAGE = new Item("coca cola", "0404040", "coca cola", new Money(new BigDecimal("10"), Currency.SEK), new BigDecimal("0.33"));
    private static final Item DEFAULT_ONE_LITER_BEVERAGE = new Item("coca cola", "0404040", "coca cola", new Money(new BigDecimal("10"), Currency.SEK), new BigDecimal("1"));
    private static final Item DEFAULT_BIG_BEVERAGE = new Item("coca cola", "0404040", "coca cola", new Money(new BigDecimal("10"), Currency.SEK), new BigDecimal("2"));
    private static final Item DEFAULT_GROCERY = new Item("mjöl", "0104040", "ICA", ItemType.GROCERY, new Money(new BigDecimal("10"), Currency.SEK));
    private static final Item DEFAULT_TOBACCO = new Item("snus", "0204040", "Knox", ItemType.TOBACCO, new Money(new BigDecimal("10"), Currency.SEK));
    private static final Item DEFAULT_NEWSPAPER = new Item("Aftonbladet", "0304040", "Aftonbladet", ItemType.NEWSPAPER, new Money(new BigDecimal("10"), Currency.SEK));


    @Test
    void constructorValidParameterTest(){
        assertEquals("mjöl", DEFAULT_GROCERY.getName());
        assertEquals("0104040", DEFAULT_GROCERY.getProductNo());
        assertEquals("ICA", DEFAULT_GROCERY.getProducer());
        assertEquals(ItemType.GROCERY, DEFAULT_GROCERY.getType());
        assertEquals(new Money(new BigDecimal("10"), Currency.SEK), DEFAULT_GROCERY.getPrice());
    }

    @Test
    void constructorVatForNewsPaperIsCorrect(){
        assertEquals(new BigDecimal("0.06"), DEFAULT_NEWSPAPER.getVat());
    }

    @Test
    void constructorVatForTobaccoIsCorrect(){
        assertEquals(new BigDecimal("0.25"), DEFAULT_TOBACCO.getVat());
    }

    @Test
    void constructorVatForGroceryIsCorrect(){
        assertEquals(new BigDecimal("0.12"), DEFAULT_GROCERY.getVat());
    }

    @Test
    void constructorSetsTobaccoAsAgeRestricted(){
        assertEquals(true, DEFAULT_TOBACCO.isAgeRestricted());
    }

    @Test
    void constructorSetsPantCorrectFor33Cl() {
        assertEquals(BigDecimal.ONE, DEFAULT_SMALL_BEVERAGE.getPant());
    }

    @Test
    void constructorSetsPantCorrectFor100Cl() {
        assertEquals(new BigDecimal("2"), DEFAULT_ONE_LITER_BEVERAGE.getPant());
    }

    @Test
    void constructorSetsPantCorrectFor200Cl() {
        assertEquals(new BigDecimal("2"), DEFAULT_BIG_BEVERAGE.getPant());
    }

    @Test
    void constructorThrowsIAEForNegativeCl(){
        assertThrows(IllegalArgumentException.class, () -> {new Item("coca cola", "0404040", "coca cola", new Money(new BigDecimal("10"), Currency.SEK), new BigDecimal("-1"));});
    }

    @Test
    void setPriceChangesPriceCorrectlyTest(){
        Item item = new Item("mjöl", "0104040", "ICA", ItemType.GROCERY, new Money(new BigDecimal("10"), Currency.SEK));
        Money money = new Money(new BigDecimal("20"), Currency.SEK);
        item.setPrice(money);
        assertEquals(new Money(new BigDecimal("20"), Currency.SEK), item.getPrice());
    }

    @Test
    void toStringTest(){
        assertEquals("name='mjöl', productNo='0104040', producer='ICA', ageRestricted='false', type='GROCERY', price='10 SEK'", DEFAULT_GROCERY.toString());
    }






}


