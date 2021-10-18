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
    void tobaccoAgeRestrictedTest(){
        assertEquals(true, DEFAULT_TOBACCO.isAgeRestricted());
    }

    //test för att kolla att rätt vat sätts i konstruktorn för tidning kolla med hjälp av getVat i Item

    //test för att kolla att rätt vat sätt i konstruktorn för tobak kolla med hjälp av getVat i Item

    //test för att kolla att rätt vat sätt i konstruktorn för livsmedel kolla med hjälp av getVat i Item

    //testa så att namn sätts i konstruktorn

    //testa så att productNo sätts i konstruktorn

    //testa så att producer sätts i konstruktorn

    //testa så att typ sätts i konstruktorn

    //testa så att price sätts i konstruktorn

    //testa så att tobak sätts som age restricted

    //testa så att setPrice ändrar värdet på en produkt






}


