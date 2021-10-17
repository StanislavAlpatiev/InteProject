package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InventoryTest {

    static final int DEFAULT_VALUE = 5;
    static Item DEFAULT_ITEM;
    static Item ITEM_WITH_SEK_CURRENCY;
    static final Currency DEFAULT_CURRENCY = Currency.USD;
    Inventory defaultInventory;



    @BeforeAll
    static void setUp(){
        DEFAULT_ITEM = new Item("Test", "Test", "Test", false, ItemType.GROCERY, new Money(new BigDecimal(5), Currency.USD)) {
            @Override
            public double getVAT() {
                return 0;
            }
            @Override
            public Money getSalesPrice() {
                return null;
            }
        };

        ITEM_WITH_SEK_CURRENCY = new Item("Test", "Test", "Test", false, ItemType.GROCERY, new Money(new BigDecimal(5), Currency.SEK)) {
            @Override
            public double getVAT() {
                return 0;
            }
            @Override
            public Money getSalesPrice() {
                return null;
            }
        };
    }

    @BeforeEach
    void initialize(){
        defaultInventory = new Inventory(DEFAULT_CURRENCY);
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
    void validConstructorWithRegisterAndItemsParamsTest() throws IOException{
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
        defaultInventory.importInventory("test"); //String for filename?
        //assertEquals(DEFAULT_CURRENCY, inventory.getCurrency() eller .getRegister.getCurrency());
    }

    //Tests whether importing inventory items works
    @Test
    void importImportsItemsTest(){
        Inventory testOracle = new Inventory(DEFAULT_CURRENCY);
        Item testItem1 = new Grocery("testName1", 123123, "Arla", false, ItemType.GROCERY, new Money(new BigDecimal(20), Currency.USD));
        Item testItem2 = new Grocery("testName2", 123123, "Arla", false, ItemType.GROCERY, new Money(new BigDecimal(30), Currency.SEK));
        testOracle.add(testItem1, testItem2);
        defaultInventory.importInventory("test"); //String for filename?
        assertEquals(testOracle.getItems(), defaultInventory.getItems());
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
    void exportTest() throws IOException{
        //TODO: UPDATE WITH SUBCLASSES?
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.exportInventory("testOutput"); //string for name
        defaultInventory = new Inventory(DEFAULT_CURRENCY);
        defaultInventory.importInventory("testOutput");
        assertEquals(DEFAULT_ITEM, defaultInventory.getItems().keySet().stream().findFirst().orElse(null));
    }

    //Adds item with a SEK money object to Inventory with USD currency, and makes sure the Item is converted into USD as its added.
    @Test
    void addedItemHasSameCurrency() throws IOException{
        defaultInventory.add(ITEM_WITH_SEK_CURRENCY);
        Item item = defaultInventory.getItems().keySet().stream().findFirst().orElse(null);
        assertEquals(DEFAULT_CURRENCY, item.getPrice().getCurrency());
    }

    @Test
    void addAddsNewMapEntryTest() throws IOException{
        defaultInventory.add(DEFAULT_ITEM);
        assertTrue(defaultInventory.getItems().keySet().contains(DEFAULT_ITEM));
        assertEquals(1, defaultInventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void addIncrementsIntegerTest() throws IOException{
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.add(DEFAULT_ITEM);
        assertEquals(2, defaultInventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void removeDecrementsIntegerTest() throws IOException{
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.remove(DEFAULT_ITEM);
        assertEquals(1, defaultInventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void removeRemovesIfDecrementIntegerBelowOneTest() throws IOException{
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
    void getItemsIsInmutableTest() throws IOException{
        defaultInventory.add(DEFAULT_ITEM);
        Map<Item, Integer> items = defaultInventory.getItems();
        items.clear();
        items = defaultInventory.getItems();
        assertFalse(items.isEmpty());
    }

    //Tests whether item is flagged as available when it is
    @Test
    void itemIsAvailableTest() throws IOException{
        defaultInventory.add(DEFAULT_ITEM);
        assertTrue(defaultInventory.isAvailable(DEFAULT_ITEM));
    }

    //Tests whether item is flagged as unavailable when it is
    @Test
    void itemIsNotAvailableTest() throws IOException{
        defaultInventory.add(DEFAULT_ITEM);
        Item item = new Item("Test2", null, null, false, null, null) {
            @Override
            public double getVAT() {
                return 0;
            }
            @Override
            public Money getSalesPrice() {
                return null;
            }
        };
        assertFalse(defaultInventory.isAvailable(item));
    }
}
