package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class InventoryTest {

    static final int DEFAULT_VALUE = 5;
    static Item DEFAULT_ITEM;
    static final Currency DEFAULT_CURRENCY = Currency.USD;


    @BeforeAll
    static void setUp(){
        DEFAULT_ITEM = new Item("Test") {
            @Override
            public double getVAT() {
                return 0;
            }
        };
    }
    //Tests whether constructor without parameters works
    @Test
    void validConstructorOnlyRegisterParamTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        HashMap<Item, Integer> items = inventory.getItems();
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

        Inventory inventory = new Inventory(DEFAULT_CURRENCY, items);
        assertEquals(DEFAULT_VALUE, inventory.getItems().get(DEFAULT_ITEM));
        //assertEquals(register.getCurrency(), inventory.getCurrency());
    }

    //Tests whether constructor throws if fed with null or empty list of items
    @Test
    void constructorNullOrEmptyListParamThrowsTest(){
        assertThrows(IllegalArgumentException.class, () -> {new Inventory(DEFAULT_CURRENCY, null);});
        assertThrows(IllegalArgumentException.class, () -> {new Inventory(DEFAULT_CURRENCY, new Item[0]);});
    }

    //Tests whether importing inventory currency works
    @Test
    void importImportsCurrencyTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        inventory.importInventory("test"); //String for filename?
        //assertEquals(DEFAULT_CURRENCY, inventory.getCurrency() eller .getRegister.getCurrency());
    }

        //Tests whether importing inventory items works
        @Test
        void importImportsItemsTest(){
            Inventory inventory = new Inventory(DEFAULT_CURRENCY);
            inventory.importInventory("test"); //String for filename?
            //assert something about items. requires subclasses i feel. 
        }

    //Tests whether importing broken saved inventory throws
    @Test 
    void brokenImportThrowsTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        assertThrows(IllegalArgumentException.class, () -> {inventory.importInventory("test2");}); //String for bad importInventory?
    }

    //Tests whether attempting to import nonexisting file throws.
    @Test
    void missingImportThrowsTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        assertThrows(IllegalArgumentException.class, () -> {inventory.importInventory("test99");}); //String for nonexisting file
    }

    //Tests whether exportInventory exports currency as intended
    @Test
    void exportTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        inventory.exportInventory("testOutput"); //string for name
        //do an import here of testOutput, and check whether currency is there.
    }

    @Test
    void addAddsNewMapEntryTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        inventory.add(DEFAULT_ITEM);
        assertTrue(inventory.getItems().keySet().contains(DEFAULT_ITEM));
        assertEquals(1, inventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void addIncrementsIntegerTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        inventory.add(DEFAULT_ITEM);
        inventory.add(DEFAULT_ITEM);
        assertEquals(2, inventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void removeDecrementsIntegerTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        inventory.add(DEFAULT_ITEM);
        inventory.add(DEFAULT_ITEM);
        inventory.remove(DEFAULT_ITEM);
        assertEquals(1, inventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void removeRemovesIfDecrementIntegerBelowOneTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        inventory.add(DEFAULT_ITEM);
        inventory.remove(DEFAULT_ITEM);
        assertFalse(inventory.getItems().containsKey(DEFAULT_ITEM));
    }

    @Test
    void setCurrencyChangesCurrency(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        inventory.importInventory("test");
        inventory.setCurrency(Currency.SEK);
        assertEquals(Currency.SEK, inventory.getCurrency());
        //test here if items within have new currency. 
    }

    @Test
    void setCurrencyChangesCurrencyOfItems(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        inventory.importInventory("test");
        inventory.setCurrency(Currency.SEK);
        //test here whether items within have Currency.SEK!
    }

    //Tests whether requested list of items can edit inventories list of items. 
    //IS CURRENTLY NOT UNMUTABLE, ONLY SHALLOW COPY. WHICH IS PREFERABLE?
    @Test
    void getItemsIsInmutableTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        inventory.add(DEFAULT_ITEM);
        Map<Item, Integer> items = inventory.getItems();
        items.clear();
        items = inventory.getItems();
        assertFalse(items.isEmpty());
    }

    //Tests whether item is flagged as available when it is
    @Test
    void itemIsAvailableTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        inventory.add(DEFAULT_ITEM);
        assertTrue(inventory.isAvailable(DEFAULT_ITEM));
    }

    //Tests whether item is flagged as unavailable when it is
    @Test
    void itemIsNotAvailableTest(){
        Inventory inventory = new Inventory(DEFAULT_CURRENCY);
        inventory.add(DEFAULT_ITEM);
        Item item = new Item("Test2") {
            @Override
            public double getVAT() {
                return 0;
            }
        };
        assertFalse(inventory.isAvailable(item));
    }
}
