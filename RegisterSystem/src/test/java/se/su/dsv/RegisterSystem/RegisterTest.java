package se.su.dsv.RegisterSystem;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegisterTest {

    @Test
    public void testItemIsAdded(){

    }

    @Test
    public void testItemIsRemoved(){

    }

    @Test
    public void testListIsEmptyAfterCheckout(){

    }


    
    Register defaultRegister;
    Currency defaultCurrency = Currency.USD;
    Currency otherCurrency = Currency.SEK;

    @BeforeEach
    void initialize(){
        defaultRegister = new Register(defaultCurrency);
    }

    @Test
    void constructorSetsCurrencyAndInventoryTest(){
        assertEquals(defaultCurrency, defaultRegister.getCurrency());
        assertTrue(defaultRegister.getInventory()!=null);
    }

    @Test
    void constructorThrowsIfNullCurrency(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Register(null);
        });
    }

    @Test
    void setCurrencyChangesCurrencyInRegisterAndInventoryTest() throws IllegalArgumentException, IOException{
        defaultRegister.setCurrency(otherCurrency);

        Currency inventoryCurrency = defaultRegister.getInventory().getCurrency();
        assertEquals(otherCurrency, defaultRegister.getCurrency());
        assertEquals(defaultRegister.getCurrency(), inventoryCurrency);
    }

    @Test
    void setCurrencyWithNullParamThrowsTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            defaultRegister.setCurrency(null);
        });
    }

    //import inventory? maybe something on order? 

}
