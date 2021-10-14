package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class InventoryTest {
    
    static final int DEFAULT_VALUE = 5;
    //Tests whether constructor without parameters works
    @Test
    void validConstructorEmptyParamTest(){
        Inventory inventory = new Inventory();
        Map<Item, Integer> items = inventory.getItems();
        assertTrue(items.isEmpty());
    }

    //Tests whether constructor with a list of items as parameter works
    @Test
    void validConstructorWithParamTest(){
        Item[] items = new Item[DEFAULT_VALUE];
        for(int i = 0; i < DEFAULT_VALUE; i++){
            items[i] = new Item(); //ADD PARAMS/SUBCLASS
        }

        Inventory inventory = new Inventory(items);
        HashSet<Item> items2 = inventory.getItems().keySet();
        assertFalse(items2.add(items[0]));
        assertEquals(DEFAULT_VALUE, inventory.getItems().get(new Item()));
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
        inventory.import(""); //String for location?
    }

    //Tests whether importing broken saved inventory throws
    @Test 
    void brokenImportThrowsTest(){
        Inventory inventory = new Inventory();
        assertThrows(IllegalArgumentException.class, () -> {inventory.import("");}); //String for bad location, or bad import?
    }

    //Tests whether export works as intended
    @Test
    void exportTest(){
        Inventory inventory = new Inventory();
        inventory.export(""); //string for location?
    }

    @Test
    void addAddsNewMapEntryTest(){
        Item item = new Item(); //ADD SUBCLASS
        Inventory inventory = new Inventory();
        inventory.add(item);
        assertTrue(inventory.getItems().keySet().contains(item));
        assertEquals(1, inventory.getItems().get(item));
    }

    @Test
    void addIncrementsIntegerTest(){
        Inventory inventory = new Inventory();
        Item item = new Item();
        inventory.add(item);
        inventory.add(item);
        assertEquals(2, inventory.getItems().get(item));
    }

    @Test
    void removeDecrementsIntegerTest(){

    }

    @Test
    void removeDoesNotDecrementIntegerPastZeroTest(){

    }

    //Tests whether requested list of items can edit inventories list of items. 
    //IS CURRENTLY NOT UNMUTABLE, ONLY SHALLOW COPY. WHICH IS PREFERABLE?
    @Test
    void getItemsIsInmutableTest(){
        Inventory inventory = new Inventory();
        inventory.import(""); //location!
        Map<Item, Integer> items = inventory.getItems();
        items.clear();
        items = inventory.getItems();
        assertFalse(items.isEmpty());
    }

    //Tests whether item is flagged as available when it is
    @Test
    void itemIsAvailableTest(){
        Item item = new Item(); //MAKE INTO SUBCLASS IN INVENTORY
        Inventory inventory = new Inventory();
        inventory.import(""); //location
        Item item = inventory.getItems().keySet();
        assertTrue(inventory.isAvailable(item));
    }

    //Tests whether item is flagged as unavailable when it is
    @Test
    void itemIsNotAvailableTest(){
        Item item = new Item(){

        }; //MAKE INTO SUBCLASS NOT IN INVENTORY
        Inventory inventory = new Inventory();
        inventory.import(""); //location
        assertTrue(inventory.isAvailable(item));
    }



}
