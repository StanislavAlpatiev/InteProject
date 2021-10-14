package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class InventoryTest {

    static final int DEFAULT_VALUE = 5;
    static Item DEFAULT_ITEM;


    @BeforeAll
    void setUp(){
        DEFAULT_ITEM = new Item() {
            @Override 
            public String getName(){
                return "Test";
            }
        };
    }
    //Tests whether constructor without parameters works
    @Test
    void validConstructorEmptyParamTest(){
        Inventory inventory = new Inventory();
        HashMap<Item, Integer> items = inventory.getItems();
        assertTrue(items.isEmpty());
    }

    //Tests whether constructor with a list of items as parameter works
    @Test
    void validConstructorWithParamTest(){
        Item[] items = new Item[DEFAULT_VALUE];
        for(int i = 0; i < DEFAULT_VALUE; i++){
            item[i] = DEFAULT_ITEM;
        }

        Inventory inventory = new Inventory(items);
        Set<Item> items2 = inventory.getItems().keySet();
        assertFalse(items2.add(items[0]));
        assertEquals(DEFAULT_VALUE, inventory.getItems().get(DEFAULT_ITEM));
    }

    //Tests whether constructor throws if fed with null or empty list of items
    @Test
    void constructorNullOrEmptyListParamThrowsTest(){
        assertThrows(IllegalArgumentException.class, () -> {new Inventory(null);});
        assertThrows(IllegalArgumentException.class, () -> {new Inventory(new Item[0]);});
    }

    //Tests whether importing saved inventory works
    @Test
    void importTest(){
        Inventory inventory = new Inventory();
        inventory.importInventory(""); //String for location?
    }

    //Tests whether importing broken saved inventory throws
    @Test 
    void brokenImportThrowsTest(){
        Inventory inventory = new Inventory();
        assertThrows(IllegalArgumentException.class, () -> {inventory.importInventory("");}); //String for bad location, or bad importInventory?
    }

    //Tests whether exportInventory works as intended
    @Test
    void exportTest(){
        Inventory inventory = new Inventory();
        inventory.exportInventory(""); //string for location?
    }

    @Test
    void addAddsNewMapEntryTest(){
        Inventory inventory = new Inventory();
        inventory.add(DEFAULT_ITEM);
        assertTrue(inventory.getItems().keySet().contains(DEFAULT_ITEM));
        assertEquals(1, inventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void addIncrementsIntegerTest(){
        Inventory inventory = new Inventory();
        inventory.add(DEFAULT_ITEM);
        inventory.add(DEFAULT_ITEM);
        assertEquals(2, inventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void removeDecrementsIntegerTest(){
        Inventory inventory = new Inventory();
        inventory.add(DEFAULT_ITEM);
        inventory.add(DEFAULT_ITEM);
        inventory.remove(DEFAULT_ITEM);
        assertEquals(1, inventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void removeRemovesIfDecrementIntegerBelowOneTest(){
        Inventory inventory = new Inventory();
        inventory.add(DEFAULT_ITEM);
        inventory.remove(DEFAULT_ITEM);
        assertFalse(inventory.getItems().containsKey(DEFAULT_ITEM));
    }

    //Tests whether requested list of items can edit inventories list of items. 
    //IS CURRENTLY NOT UNMUTABLE, ONLY SHALLOW COPY. WHICH IS PREFERABLE?
    @Test
    void getItemsIsInmutableTest(){
        Inventory inventory = new Inventory();
        inventory.add(DEFAULT_ITEM);
        Map<Item, Integer> items = inventory.getItems();
        items.clear();
        items = inventory.getItems();
        assertFalse(items.isEmpty());
    }

    //Tests whether item is flagged as available when it is
    @Test
    void itemIsAvailableTest(){
        Inventory inventory = new Inventory();
        inventory.add(DEFAULT_ITEM);
        assertTrue(inventory.isAvailable(DEFAULT_ITEM));
    }

    //Tests whether item is flagged as unavailable when it is
    @Test
    void itemIsNotAvailableTest(){
        Inventory inventory = new Inventory();
        inventory.add(DEFAULT_ITEM);
        Item item = new Item() {
            @Override
            public String getName() {
                return "Test2";
            }
        };
        assertTrue(inventory.isAvailable(item));
    }
}
