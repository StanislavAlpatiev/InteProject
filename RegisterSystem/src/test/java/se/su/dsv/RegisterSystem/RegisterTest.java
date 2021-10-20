package se.su.dsv.RegisterSystem;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegisterTest {

    final Currency defaultCurrency = Currency.USD;
    final Currency otherCurrency = Currency.SEK;
    Register defaultRegister;

    @BeforeEach
    void initialize() {
        defaultRegister = new Register(defaultCurrency);
    }

    //Tests whether constructor constructs as it is supposed to with valid params.
    @Test
    void constructorSetsCurrencyAndInventoryTest() {
        assertEquals(defaultCurrency, defaultRegister.getCurrency());
        assertTrue(defaultRegister.getInventory() != null);
    }

    //Tries to construct an object with null currency
    @Test
    void constructorThrowsIfNullCurrency() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Register(null);
        });
    }

    //Changes currency of the register to SEK from USD, and sees if this change is visible in both Register and its Inventory
    @Test
    void setCurrencyChangesCurrencyInRegisterAndInventoryTest() throws IllegalArgumentException, IOException {
        defaultRegister.setCurrency(otherCurrency);

        Currency inventoryCurrency = defaultRegister.getInventory().getCurrency();
        assertEquals(otherCurrency, defaultRegister.getCurrency());
        assertEquals(defaultRegister.getCurrency(), inventoryCurrency);
    }

    //Tries to change to null currency.
    @Test
    void setCurrencyWithNullParamThrowsTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultRegister.setCurrency(null);
        });
    }

    @Test
    public void testItemIsAdded() {

    }

    @Test
    public void testItemIsRemoved() {

    }

    @Test
    public void testListIsEmptyAfterCheckout() {

    }

    // import inventory? maybe something on order?

}
