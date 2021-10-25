package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    static final int DEFAULT_VALUE = 5;
    static final Item SMALL_BEVERAGE = new Item("coca cola", "0404040", "coca cola", new Money(new BigDecimal("10"), Currency.SEK), new BigDecimal("0.33"));
    static final Item ONE_LITER_BEVERAGE = new Item("coca cola", "0404040", "coca cola", new Money(new BigDecimal("15"), Currency.SEK), new BigDecimal("1"));
    static final Item BIG_BEVERAGE = new Item("coca cola", "0404040", "coca cola", new Money(new BigDecimal("20"), Currency.SEK), new BigDecimal("2"));
    static final Item GROCERY = new Item("mjöl", "0104040", "ICA", ItemType.GROCERY, new Money(new BigDecimal("7"), Currency.SEK));
    static final Item GROCERY_2 = new Item("mjöl", "0104040", "ICA", ItemType.GROCERY, new Money(new BigDecimal("7"), Currency.SEK));
    static final Item TOBACCO = new Item("snus", "0204040", "Knox", ItemType.TOBACCO, new Money(new BigDecimal("50"), Currency.SEK));
    static final Item NEWSPAPER = new Item("Aftonbladet", "0304040", "Aftonbladet", ItemType.NEWSPAPER, new Money(new BigDecimal("80"), Currency.SEK));
    static final Currency DEFAULT_CURRENCY = Currency.USD;
    static final MockBank DEFAULT_MOCK_BANK = new MockBank();
    static Item DEFAULT_ITEM;
    static Item ITEM_WITH_SEK_CURRENCY;
    Inventory defaultInventory;


    @BeforeAll
    static void setUp() {
        DEFAULT_ITEM = new Item("Test", "Test", "Test", ItemType.GROCERY, new Money(new BigDecimal(5), Currency.USD));
        ITEM_WITH_SEK_CURRENCY = new Item("Test", "Test", "Test", ItemType.GROCERY, new Money(new BigDecimal(5), Currency.SEK));
    }

    @BeforeEach
    void initialize() {
        defaultInventory = new Inventory(DEFAULT_MOCK_BANK, DEFAULT_CURRENCY);
    }

    //Tests whether constructor without parameters works
    @Test
    void validConstructorOnlyRegisterParamTest() {
        HashMap<Item, Integer> items = defaultInventory.getItems();
        assertTrue(items.isEmpty());
        //assertEquals(register.getCurrency(), inventory.getCurrency());
    }

    //Tests whether constructor with a list of items as parameter works
    @Test
    void validConstructorWithRegisterAndItemsParamsTest() throws IOException {
        Item[] items = new Item[DEFAULT_VALUE];
        for (int i = 0; i < DEFAULT_VALUE; i++) {
            items[i] = DEFAULT_ITEM;
        }

        Inventory inventory = new Inventory(DEFAULT_MOCK_BANK, Currency.USD, items);
        assertEquals(DEFAULT_VALUE, inventory.getItems().get(DEFAULT_ITEM));
        //assertEquals(register.getCurrency(), inventory.getCurrency());
    }

    //Tests whether constructor throws if fed with null or empty list of items
    @Test
    void constructorNullOrEmptyListParamThrowsTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(DEFAULT_MOCK_BANK, DEFAULT_CURRENCY, null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(DEFAULT_MOCK_BANK, DEFAULT_CURRENCY, new Item[0]);
        });
    }

    //Tests whether importing inventory currency works
//    @Test
//    void importImportsCurrencyTest() throws FileNotFoundException {
//        defaultInventory.importInventory("test"); //String for filename?
//        //assertEquals(DEFAULT_CURRENCY, inventory.getCurrency() eller .getRegister.getCurrency());
//    }

    //Tests whether importing inventory items works
    @Test
    void importImportsItemsTest() throws IOException {
        Inventory testOracle = new Inventory(DEFAULT_MOCK_BANK, DEFAULT_CURRENCY);
        testOracle.add(NEWSPAPER, GROCERY, GROCERY_2, TOBACCO, BIG_BEVERAGE, ONE_LITER_BEVERAGE, SMALL_BEVERAGE);
        //Export Inventory to TestInventory.json
        testOracle.exportInventory("src\\test\\resources\\ImportInventory.json");
        //Import TestInventory to testOracle
        //Import Inventory to defaultInventory
        defaultInventory.importInventory("src\\test\\resources\\ImportInventory.json");
        //System.out.println(defaultInventory.getItems().containsKey(GROCERY));
        assertTrue(defaultInventory.getItems().containsKey(GROCERY_2));
        //Test that defaultInventory imports inventory correctly
    }

    //Tests whether importing broken saved inventory throws
    @Test
    void brokenImportThrowsTest() {
        assertThrows(FileNotFoundException.class, () -> {
            defaultInventory.importInventory("test2");
        }); //String for bad importInventory?
    }

    //Tests whether attempting to import nonexisting file throws.
    @Test
    void missingImportThrowsTest() {
        assertThrows(FileNotFoundException.class, () -> {
            defaultInventory.importInventory("test99");
        }); //String for nonexisting file
    }

    //Tests whether exportInventory exports currency as intended
    @Test
    void exportTest() throws IOException {
        Inventory testOracle = new Inventory(DEFAULT_MOCK_BANK, DEFAULT_CURRENCY);
        testOracle.add(NEWSPAPER, GROCERY, GROCERY_2, TOBACCO, BIG_BEVERAGE, ONE_LITER_BEVERAGE, SMALL_BEVERAGE);
        testOracle.exportInventory("src\\test\\resources\\ExportInventory.json");
        testOracle.importInventory("src\\test\\resources\\ExportInventory.json");
        defaultInventory.importInventory("src\\test\\resources\\TestInventory.json");
        assertEquals(defaultInventory.getItems(), testOracle.getItems());
    }

    //Adds item with a SEK money object to Inventory with USD currency, and makes sure the Item is converted into USD as its added.
    @Test
    void addedItemHasSameCurrency() throws IOException {
        defaultInventory.add(ITEM_WITH_SEK_CURRENCY);
        Item item = defaultInventory.getItems().keySet().stream().findFirst().orElse(null);
        assertEquals(DEFAULT_CURRENCY, item.getPrice().getCurrency());
    }

    @Test
    void addAddsNewMapEntryTest() throws IOException {
        defaultInventory.add(DEFAULT_ITEM);
        assertTrue(defaultInventory.getItems().keySet().contains(DEFAULT_ITEM));
        assertEquals(1, defaultInventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void addIncrementsIntegerTest() throws IOException {
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.add(DEFAULT_ITEM);
        assertEquals(2, defaultInventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void removeDecrementsIntegerTest() throws IOException {
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.remove(DEFAULT_ITEM);
        assertEquals(1, defaultInventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void removeRemovesIfDecrementIntegerBelowOneTest() throws IOException {
        defaultInventory.add(DEFAULT_ITEM);
        defaultInventory.remove(DEFAULT_ITEM);
        assertFalse(defaultInventory.getItems().containsKey(DEFAULT_ITEM));
    }

    @Test
    void setCurrencyChangesCurrency() throws IOException {
        defaultInventory.importInventory("src\\test\\resources\\TestInventory.json");
        defaultInventory.setCurrency(Currency.SEK);
        assertEquals(Currency.SEK, defaultInventory.getCurrency());
        //test here if items within have new currency.
    }

    @Test
    void setCurrencyChangesCurrencyOfItems() throws IOException {
        defaultInventory.importInventory("src\\test\\resources\\TestInventory.json");
        defaultInventory.setCurrency(Currency.SEK);
        //test here whether items within have Currency.SEK!
    }

    //Tests whether requested list of items can edit inventories list of items.
    //IS CURRENTLY NOT UNMUTABLE, ONLY SHALLOW COPY. WHICH IS PREFERABLE?
    @Test
    void getItemsIsInmutableTest() throws IOException {
        defaultInventory.add(DEFAULT_ITEM);
        Map<Item, Integer> items = defaultInventory.getItems();
        items.clear();
        items = defaultInventory.getItems();
        assertFalse(items.isEmpty());
    }

    //Tests whether item is flagged as available when it is
    @Test
    void itemIsAvailableTest() throws IOException {
        defaultInventory.add(DEFAULT_ITEM);
        assertTrue(defaultInventory.isAvailable(DEFAULT_ITEM));
    }

    //Tests whether item is flagged as unavailable when it is
    @Test
    void itemIsNotAvailableTest() throws IOException {
        defaultInventory.add(DEFAULT_ITEM);
        Item item = new Item("Test2", "null", "null", ItemType.BEVERAGE, new Money(BigDecimal.TEN, Currency.AED));
        assertFalse(defaultInventory.isAvailable(item));
    }
}
