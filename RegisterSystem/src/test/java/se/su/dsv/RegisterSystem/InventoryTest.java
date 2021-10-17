package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class InventoryTest {

    static final int DEFAULT_VALUE = 5;
    static Item DEFAULT_ITEM;
    static final Currency DEFAULT_CURRENCY = Currency.USD;
    Inventory defaultInventory;


    @BeforeAll
    static void setUp(){
        DEFAULT_ITEM = new Item("Test") {
            @Override
            public double getVAT() {
                return 0;
            }
        };
    }

    @BeforeEach
    void initialize(){
        defaultInventory = new Inventory(new Register(DEFAULT_CURRENCY));
    }

    //Tests whether constructor without parameters works
    @Test
    void validConstructorOnlyRegisterParamTest(){
        HashMap<Item, Integer> items = defaultInventory.getItems();
        assertTrue(items.isEmpty());
        //assertEquals(register.getCurrency(), inventory.getCurrency());
    }

    //Tests whether constructor with a list of items as parameter works
    @Test
    void validConstructorWithRegisterAndItemsParamsTest(){
        Item[] items = new Item[DEFAULT_VALUE];
        for(int i = 0; i < DEFAULT_VALUE; i++){
            items[i] = DEFAULT_ITEM;
        }

        Inventory inventory = new Inventory(new Register(DEFAULT_CURRENCY), items);
        assertEquals(DEFAULT_VALUE, inventory.getItems().get(DEFAULT_ITEM));
        //assertEquals(register.getCurrency(), inventory.getCurrency());
    }

    //Tests whether constructor throws if fed with null or empty list of items
    @Test
    void constructorNullOrEmptyListParamThrowsTest(){
        assertThrows(IllegalArgumentException.class, () -> {new Inventory(new Register(DEFAULT_CURRENCY), null);});
        assertThrows(IllegalArgumentException.class, () -> {new Inventory(new Register(DEFAULT_CURRENCY), new Item[0]);});
    }

    //Tests whether importing inventory currency works
    @Test
    void importImportsCurrencyTest(){
        defaultInventory.importInventory("test"); //String for filename?
        //assertEquals(DEFAULT_CURRENCY, inventory.getCurrency() eller .getRegister.getCurrency());
    }

    //Tests whether importing inventory items works
    @Test
    void importImportsItemsTest(){
        defaultInventory.importInventory("test"); //String for filename?
        //assert something about items. requires subclasses i feel. 
    }

    //Tests whether importing broken saved inventory throws
    @Test 
    void brokenImportThrowsTest(){
        assertThrows(IllegalArgumentException.class, () -> {defaultInventory.importInventory("test2");}); //String for bad importInventory?
    }

    //Tests whether attempting to import nonexisting file throws.
    @Test
    void missingImportThrowsTest(){
        assertThrows(IllegalArgumentException.class, () -> {defaultInventory.importInventory("test99");}); //String for nonexisting file
    }

    //Tests whether exportInventory exports currency as intended
    @Test
    void exportTest(){
        defaultInventory.importInventory("test");
        defaultInventory.exportInventory("testOutput"); //string for name
        defaultInventory = new Inventory(new Register(DEFAULT_CURRENCY));
        defaultInventory.importInventory("testOutput");

        //do an import here of testOutput, and check whether currency is there.
    }

    @Test
    void addAddsNewMapEntryTest(){
        defaultInventory.add(DEFAULT_ITEM);
        assertTrue(defaultInventory.getItems().keySet().contains(DEFAULT_ITEM));
        assertEquals(1, defaultInventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void addIncrementsIntegerTest(){
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.add(DEFAULT_ITEM);
        assertEquals(2, defaultInventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void removeDecrementsIntegerTest(){
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.remove(DEFAULT_ITEM);
        assertEquals(1, defaultInventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void removeRemovesIfDecrementIntegerBelowOneTest(){
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.remove(DEFAULT_ITEM);
        assertFalse(defaultInventory.getItems().containsKey(DEFAULT_ITEM));
    }

    @Test
    void setCurrencyChangesCurrency() throws IOException{
        defaultInventory.importInventory("test");
        defaultInventory.setCurrency(Currency.SEK);
        assertEquals(Currency.SEK, defaultInventory.getCurrency());
        //test here if items within have new currency. 
    }

    @Test
    void setCurrencyChangesCurrencyOfItems() throws IOException{
        defaultInventory.importInventory("test");
        defaultInventory.setCurrency(Currency.SEK);
        //test here whether items within have Currency.SEK!
    }

    //Tests whether requested list of items can edit inventories list of items. 
    //IS CURRENTLY NOT UNMUTABLE, ONLY SHALLOW COPY. WHICH IS PREFERABLE?
    @Test
    void getItemsIsInmutableTest(){
        defaultInventory.add(DEFAULT_ITEM);
        Map<Item, Integer> items = defaultInventory.getItems();
        items.clear();
        items = defaultInventory.getItems();
        assertFalse(items.isEmpty());
    }

    //Tests whether item is flagged as available when it is
    @Test
    void itemIsAvailableTest(){
        defaultInventory.add(DEFAULT_ITEM);
        assertTrue(defaultInventory.isAvailable(DEFAULT_ITEM));
    }

    //Tests whether item is flagged as unavailable when it is
    @Test
    void itemIsNotAvailableTest(){
        defaultInventory.add(DEFAULT_ITEM);
        Item item = new Item("Test2") {
            @Override
            public double getVAT() {
                return 0;
            }
        };
        assertFalse(defaultInventory.isAvailable(item));
    }
}
