package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemTest {

    //Vat rates
    private static final BigDecimal SIX_PERCENT = new BigDecimal("0.06");
    private static final BigDecimal TWELVE_PERCENT = new BigDecimal("0.12");
    private static final BigDecimal FIFTEEN_PERCENT = new BigDecimal("0.15");
    private static final BigDecimal TWENTYFIVE_PERCENT = new BigDecimal("0.25");

    private static final Money DEFAULT_GROCERY_PRICE_PLUS_VAT = new Money(new BigDecimal("11.20"), Currency.SEK);
    private static final Money DEFAULT_GROCERY_VAT_OF_ITEM = new Money(new BigDecimal("1.20"), Currency.SEK);

    //Money in different currencies, DEFAULT is SEK
    private static final Money DEFAULT_MONEY = new Money(new BigDecimal("10.00"), Currency.SEK);
    private static final Money NOK_MONEY = new Money(new BigDecimal("10.00"), Currency.NOK);
    private static final Money DKK_MONEY = new Money(new BigDecimal("10.00"), Currency.DKK);

    //Beverage in different currencies, DEFAULT is SEK
    private static final Item DEFAULT_SMALL_BEVERAGE = new Item("coca cola", "0404040", "coca cola", DEFAULT_MONEY, new BigDecimal("0.33"));
    private static final Item DEFAULT_ONE_LITER_BEVERAGE = new Item("coca cola", "0404040", "coca cola", DEFAULT_MONEY, new BigDecimal("1"));
    private static final Item DEFAULT_BIG_BEVERAGE = new Item("coca cola", "0404040", "coca cola", DEFAULT_MONEY, new BigDecimal("2"));
    private static Item NOK_SMALL_BEVERAGE = new Item("coca cola", "0404040", "coca cola", NOK_MONEY, new BigDecimal("0.33"));
    private static Item NOK_BIG_BEVERAGE = new Item("coca cola", "0404040", "coca cola", NOK_MONEY, new BigDecimal("2"));
    private static Item DKK_SMALL_BEVERAGE = new Item("coca cola", "0404040", "coca cola", DKK_MONEY, new BigDecimal("0.33"));
    private static Item DKK_BIG_BEVERAGE = new Item("coca cola", "0404040", "coca cola", DKK_MONEY, new BigDecimal("2"));

    //Items in different currencies, DEFAULT is SEK
    private static final Item DEFAULT_GROCERY = new Item("mjöl", "0104040", "ICA", ItemType.GROCERY, DEFAULT_MONEY);
    private static final Item DEFAULT_TOBACCO = new Item("snus", "0204040", "Knox", ItemType.TOBACCO, DEFAULT_MONEY);
    private static final Item DEFAULT_NEWSPAPER = new Item("Aftonbladet", "0304040", "Aftonbladet", ItemType.NEWSPAPER, DEFAULT_MONEY);
    private static final Item NOK_GROCERY = new Item("mjöl", "0104040", "ICA", ItemType.GROCERY, NOK_MONEY);
    private static final Item NOK_TOBACCO = new Item("snus", "0204040", "Knox", ItemType.TOBACCO, NOK_MONEY);
    private static final Item NOK_NEWSPAPER = new Item("Aftonbladet", "0304040", "Aftonbladet", ItemType.NEWSPAPER, NOK_MONEY);
    private static final Item DKK_GROCERY = new Item("mjöl", "0104040", "ICA", ItemType.GROCERY, DKK_MONEY);
    private static final Item DKK_TOBACCO = new Item("snus", "0204040", "Knox", ItemType.TOBACCO, DKK_MONEY);
    private static final Item DKK_NEWSPAPER = new Item("Aftonbladet", "0304040", "Aftonbladet", ItemType.NEWSPAPER, DKK_MONEY);

    //Pant in different currencies, DEFAULT is SEK
    private static final Money DEFAULT_PANT_SMALL = new Money(BigDecimal.ONE, Currency.SEK);
    private static final Money DEFAULT_PANT_BIG = new Money(new BigDecimal("2"), Currency.SEK);
    private static final Money NOK_PANT_SMALL = new Money(new BigDecimal("2"), Currency.NOK);
    private static final Money NOK_PANT_BIG = new Money(new BigDecimal("3"), Currency.NOK);
    private static final Money DKK_PANT_SMALL = new Money(new BigDecimal("1.5"), Currency.DKK);
    private static final Money DKK_PANT_BIG = new Money(new BigDecimal("3"), Currency.DKK);




    // testar konstruktorn med valida parametrar
    @Test
    void constructorValidParameterTest() {
        assertEquals("mjöl", DEFAULT_GROCERY.getName());
        assertEquals("0104040", DEFAULT_GROCERY.getProductNo());
        assertEquals("ICA", DEFAULT_GROCERY.getProducer());
        assertEquals(ItemType.GROCERY, DEFAULT_GROCERY.getType());
        assertEquals(new Money(new BigDecimal("10"), Currency.SEK), DEFAULT_GROCERY.getPrice());
    }

    // testar att ett undantag kastas när någon parameter är null
    @Test
    void constructorNullParametersThrowIAETest() {
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

    // testar att VAT för tidningar är korrekt
    @Test
    void constructorVatForNewsPaperIsCorrect() {
        assertEquals(SIX_PERCENT, DEFAULT_NEWSPAPER.getVat());
        assertEquals(BigDecimal.ZERO, NOK_NEWSPAPER.getVat());
        assertEquals(BigDecimal.ZERO, DKK_NEWSPAPER.getVat());
    }

    // testar att VAT för tobak är korrekt
    @Test
    void constructorVatForTobaccoIsCorrect() {
        assertEquals(TWENTYFIVE_PERCENT, DEFAULT_TOBACCO.getVat());
        assertEquals(TWENTYFIVE_PERCENT, NOK_TOBACCO.getVat());
        assertEquals(TWENTYFIVE_PERCENT, DKK_TOBACCO.getVat());
    }

    // testar att VAT för matvaror är korrekt
    @Test
    void constructorVatForGroceryIsCorrect() {
        assertEquals(TWELVE_PERCENT, DEFAULT_GROCERY.getVat());
        assertEquals(FIFTEEN_PERCENT, NOK_GROCERY.getVat());
        assertEquals(TWENTYFIVE_PERCENT, DKK_GROCERY.getVat());
    }

    // testar att tobak sätts som age restricted
    @Test
    void constructorSetsTobaccoAsAgeRestricted() {
        assertEquals(true, DEFAULT_TOBACCO.isAgeRestricted());
        assertEquals(true, NOK_TOBACCO.isAgeRestricted());
        assertEquals(true, DKK_TOBACCO.isAgeRestricted());
    }

    // testar att panten för 33 cl sätts korrekt
    @Test
    void constructorSetsPantCorrectFor33Cl() {
        assertEquals(DEFAULT_PANT_SMALL, DEFAULT_SMALL_BEVERAGE.getPant());
        assertEquals(NOK_PANT_SMALL, NOK_SMALL_BEVERAGE.getPant());
        assertEquals(DKK_PANT_SMALL, DKK_SMALL_BEVERAGE.getPant());
    }

    // testar att panten för 100 cl sätts korrekt
    @Test
    void constructorSetsPantCorrectFor100Cl() {
        assertEquals(DEFAULT_PANT_BIG, DEFAULT_ONE_LITER_BEVERAGE.getPant());
    }

    // testar att panten för 200 cl sätts korrekt
    @Test
    void constructorSetsPantCorrectFor200Cl() {
        assertEquals(DEFAULT_PANT_BIG, DEFAULT_BIG_BEVERAGE.getPant());
        assertEquals(NOK_PANT_BIG, NOK_BIG_BEVERAGE.getPant());
        assertEquals(DKK_PANT_BIG, DKK_BIG_BEVERAGE.getPant());
    }

    // testar att ett undantag kastas om storleken på en dryck är negativt
    @Test
    void constructorThrowsIAEForNegativeCl() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Item("coca cola", "0404040", "coca cola", new Money(new BigDecimal("10"), Currency.SEK), new BigDecimal("-1"));
        });
    }

    // testar att ett undantag kastas om ett varunamn är för långt
    @Test
    void constructorTooLongItemNameTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Item("really long name test OOOO", "0515102", "testproducer", ItemType.GROCERY, DEFAULT_MONEY);
        });
    }

    //If currency is not set to SEK, pant should be 0
    @Test
    void constructorPantCurrencyNotSEKIsZeroTest() {
        Item notInSEK = new Item("coca cola", "0404040", "coca cola", new Money(new BigDecimal("10"), Currency.USD), new BigDecimal("2"));
        assertEquals(new Money(BigDecimal.ZERO, Currency.USD), notInSEK.getPant());
    }

    // testar att setPrice() metoden ändrar priset korrekt
    @Test
    void setPriceChangesPriceCorrectlyTest() {
        Item item = new Item("mjöl", "0104040", "ICA", ItemType.GROCERY, new Money(new BigDecimal("10"), Currency.SEK));
        Money money = new Money(new BigDecimal("20"), Currency.SEK);
        item.setPrice(money);
        assertEquals(new Money(new BigDecimal("20"), Currency.SEK), item.getPrice());
    }

    //tests that setPrice changes pant for switching between currencies
    @Test
    void pantChangesWithSetPriceToOtherCurrencyTest() {
        Item beverageSmall = new Item("coca cola", "0404040", "coca cola", DEFAULT_MONEY, new BigDecimal("0.33"));
        Item beverage1L = new Item("coca cola", "0404040", "coca cola", DEFAULT_MONEY, new BigDecimal("1"));
        Item beverageLarge = new Item("coca cola", "0404040", "coca cola", DEFAULT_MONEY, new BigDecimal("2"));
        beverageSmall.setPrice(new Money(BigDecimal.ONE, Currency.NOK));
        beverage1L.setPrice(new Money(BigDecimal.ONE, Currency.NOK));
        beverageLarge.setPrice(new Money(BigDecimal.ONE, Currency.NOK));
        assertEquals(new Money(new BigDecimal("2"), Currency.NOK), beverageSmall.getPant());
        assertEquals(new Money(new BigDecimal("3"), Currency.NOK), beverage1L.getPant());
        assertEquals(new Money(new BigDecimal("3"), Currency.NOK), beverageLarge.getPant());

    }

    // testar att priset plus VAT beräknas korrekt
    @Test
    void PricePlusVatCorrectTest() {
        assertEquals(DEFAULT_GROCERY_PRICE_PLUS_VAT, DEFAULT_GROCERY.getPricePlusVatAndPant());
    }

    // testar att VAT delen av priset beräknas korrekt
    @Test
    void VatPriceOfItemTest() {
        assertEquals(DEFAULT_GROCERY_VAT_OF_ITEM, DEFAULT_GROCERY.getVATAmountOfPrice());
    }

    // testar att priset plus VAT och pant beräknas korrekt
    @Test
    void pricePlusVatAndPantCorrectTest() {
        assertEquals(new Money(new BigDecimal("13.20"), Currency.SEK), DEFAULT_BIG_BEVERAGE.getPricePlusVatAndPant());
    }

    // testar att toString metoden fungerar som den ska
    @Test
    void toStringTest() {
        assertEquals("mjöl@0104040@ICA@GROCERY@10.00@SEK", DEFAULT_GROCERY.toString());
    }


}


