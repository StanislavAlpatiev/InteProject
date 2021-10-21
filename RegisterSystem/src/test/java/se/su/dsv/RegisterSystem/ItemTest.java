package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class ItemTest {

    private static final Money DEFAULT_GROCERY_PRICE_PLUS_VAT = new Money(new BigDecimal("11.20"), Currency.SEK);
    private static final Money DEFAULT_GROCERY_VAT_OF_ITEM = new Money(new BigDecimal("1.20"), Currency.SEK);
    private static final Money DEFAULT_MONEY = new Money(new BigDecimal("10"), Currency.SEK);

    private static final Item DEFAULT_SMALL_BEVERAGE = new Item("coca cola", "0404040", "coca cola", DEFAULT_MONEY, new BigDecimal("0.33"));
    private static final Item DEFAULT_ONE_LITER_BEVERAGE = new Item("coca cola", "0404040", "coca cola", DEFAULT_MONEY, new BigDecimal("1"));
    private static final Item DEFAULT_BIG_BEVERAGE = new Item("coca cola", "0404040", "coca cola", DEFAULT_MONEY, new BigDecimal("2"));

    private static final Item DEFAULT_GROCERY = new Item("mjöl", "0104040", "ICA", ItemType.GROCERY, DEFAULT_MONEY);
    private static final Item DEFAULT_TOBACCO = new Item("snus", "0204040", "Knox", ItemType.TOBACCO, DEFAULT_MONEY);
    private static final Item DEFAULT_NEWSPAPER = new Item("Aftonbladet", "0304040", "Aftonbladet", ItemType.NEWSPAPER, DEFAULT_MONEY);

    private static final Money PANT_ONE = new Money(BigDecimal.ONE, Currency.SEK);
    private static final Money PANT_TWO = new Money(new BigDecimal("2"), Currency.SEK);


    @Test
    void constructorValidParameterTest(){
        assertEquals("mjöl", DEFAULT_GROCERY.getName());
        assertEquals("0104040", DEFAULT_GROCERY.getProductNo());
        assertEquals("ICA", DEFAULT_GROCERY.getProducer());
        assertEquals(ItemType.GROCERY, DEFAULT_GROCERY.getType());
        assertEquals(new Money(new BigDecimal("10"), Currency.SEK), DEFAULT_GROCERY.getPrice());
    }

    @Test
    void constructorNullParametersThrowIAETest(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Item(null, "0202020", "coca-cola", ItemType.BEVERAGE, DEFAULT_MONEY);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Item("coca-cola", null, "coca-cola", ItemType.BEVERAGE, DEFAULT_MONEY);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Item("coca-cola", "0202020", null, ItemType.BEVERAGE, DEFAULT_MONEY);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Item("coca-cola", "0202020", "coca-cola", ItemType.BEVERAGE, null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Item("coca-cola", "0202020", "coca-cola", DEFAULT_MONEY, null);
        });
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
        assertEquals(PANT_ONE, DEFAULT_SMALL_BEVERAGE.getPant());
    }

    @Test
    void constructorSetsPantCorrectFor100Cl() {
        assertEquals(PANT_TWO, DEFAULT_ONE_LITER_BEVERAGE.getPant());
    }

    @Test
    void constructorSetsPantCorrectFor200Cl() {
        assertEquals(PANT_TWO, DEFAULT_BIG_BEVERAGE.getPant());
    }

    @Test
    void constructorThrowsIAEForNegativeCl(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Item("coca cola", "0404040", "coca cola", new Money(new BigDecimal("10"), Currency.SEK), new BigDecimal("-1"));
        });
    }

    @Test
    void constructorTooLongItemNameTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Item("really long name test OOOO", "0515102", "testproducer", ItemType.GROCERY, DEFAULT_MONEY);
        });
    }

    //If currency is not set to SEK, pant should be 0
    @Test
    void constructorPantCurrencyNotSEKIsZeroTest(){
        Item notInSEK = new Item("coca cola", "0404040", "coca cola", new Money(new BigDecimal("10"), Currency.USD), new BigDecimal("2"));
        assertEquals(new Money(BigDecimal.ZERO, Currency.USD), notInSEK.getPant());
    }

    @Test
    void setPriceChangesPriceCorrectlyTest(){
        Item item = new Item("mjöl", "0104040", "ICA", ItemType.GROCERY, new Money(new BigDecimal("10"), Currency.SEK));
        Money money = new Money(new BigDecimal("20"), Currency.SEK);
        item.setPrice(money);
        assertEquals(new Money(new BigDecimal("20"), Currency.SEK), item.getPrice());
    }

    //if Currency of Item is changed to SEK, pant should be added

    @Test
    void PricePlusVatCorrectTest(){
        assertEquals(DEFAULT_GROCERY_PRICE_PLUS_VAT, DEFAULT_GROCERY.getPricePlusVatAndPant());
    }

    @Test
    void VatPriceOfItemTest(){
        assertEquals(DEFAULT_GROCERY_VAT_OF_ITEM, DEFAULT_GROCERY.getVATAmountOfPrice());
    }

    @Test
    void pricePlusVatAndPantCorrectTest(){
        assertEquals(new Money(new BigDecimal("13.20"), Currency.SEK),DEFAULT_BIG_BEVERAGE.getPricePlusVatAndPant());
    }

    @Test
    void toStringTest(){
        assertEquals("mjöl@0104040@ICA@GROCERY@10@SEK", DEFAULT_GROCERY.toString());
    }


}


