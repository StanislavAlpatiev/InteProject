package se.su.dsv.RegisterSystem;

import com.google.gson.Gson;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

//Add comments to each method!
public class Inventory {

    private final HashMap<Item, Integer> items;
    private final BankService bank;
    private Currency currency;

    public Inventory(BankService bank, Currency currency) {
        items = new HashMap<>();
        this.currency = currency;
        this.bank = bank;
    }

    public Inventory(BankService bank, Currency currency, Item... item) throws IOException {
        if (item == null || item.length == 0) {
            throw new IllegalArgumentException();
        }
        items = new HashMap<>();
        this.currency = currency;
        this.bank = bank;
        add(item);
    }

    //Adds item(s) into inventory
    public void add(Item... item) throws IOException {
        for (Item i : item) {
            if (i.getPrice().getCurrency() != this.currency) {
                i.setPrice(bank.exchange(i.getPrice(), this.currency));
            }
            items.put(i, (isAvailable(i)) ? items.get(i) + 1 : 1);
        }
    }

    //removes one item from the inventory
    public void remove(Item item) {
        //if the item is not available, do nothing.
        if (isAvailable(item)) {
            //if there is more than 0 of that item, remove 1.
            items.put(item, items.get(item) - 1);

            //if there is 0 of that item, remove entry in map.
            if (items.get(item) == 0) {
                items.remove(item);
            }
        }
    }

    public HashMap<Item, Integer> getItems() {
        return new HashMap<>(items);
    }

    public Currency getCurrency() {
        return currency;
    }

    //Changes currency of the inventory, and all of the items in it.
    public void setCurrency(Currency currency) throws IOException {

        //if the currency is the same as the current one, do nothing.
        if (this.currency != currency) {

            //get the exchange rate from the bank
            BigDecimal rate = bank.getRate(this.currency, currency);
            if (rate == null) {
                return;
            }
            //goes through every item in inventory, and gives them their new price
            for (Map.Entry<Item, Integer> entry : this.items.entrySet()) {
                Money price = bank.exchange(entry.getKey().getPrice(), currency, rate);
                entry.getKey().setPrice(price);
            }
            this.currency = currency;
        }
    }

    //checks whether there is any of the item in the inventory
    public boolean isAvailable(Item item) {
        return items.containsKey(item);
    }

    //checks whether all items in an order is available
    public boolean isAvailable(Order order) {
        if (order == null) {
            return false;
        }
        TreeMap<Item, BigDecimal> itemAndAmount = order.getItems();

        for (Map.Entry<Item, BigDecimal> entry : itemAndAmount.entrySet()) {
            //makes sure there are enough of each item available
            if (!itemAmountNeededIsAvailable(entry)) {
                return false;
            }
        }
        return true;
    }

    //Helper method for isAvailable(Order), determines if the number of items
    //being bought in an order is equal or less than the amount of items of that
    //type in inventory.
    private boolean itemAmountNeededIsAvailable(Map.Entry<Item, BigDecimal> entry) {
        boolean itemIsAvailable = true;
        // Checks if there is any of the item at all in the inventory
        if (!items.containsKey(entry.getKey())) {
            itemIsAvailable = false;
        }

        // Checks if the object is available, is there enough of them to satisfy order
        if (entry.getValue().compareTo(new BigDecimal(items.get(entry.getKey()))) > 0) {
            itemIsAvailable = false;
        }

        return itemIsAvailable;
    }

    //Imports inventory map of items and how many of each in an integer from file
    public void importInventory(String fileName) throws FileNotFoundException {

        try(Reader reader = Files.newBufferedReader(Paths.get(fileName))) {
            HashMap<String, Integer> newItems;
            Gson gson = new Gson();

            //Uses gson to convert the string containing the string-ified hashmap
            //of items and integers into an actual hashmap
            //However, this hashmap will not be items/integers
            //but rather the toString of the item/integer
            newItems = gson.fromJson(new FileReader(fileName), HashMap.class);

            //Emptying out old items before importing the new ones
            items.clear();

            for (Map.Entry<String, Integer> entry : newItems.entrySet()) {
                String stringEntry = entry.getKey();

                //we have constructed the toString of items to contain @ as a "splitter" 
                String[] params = stringEntry.split("@");
                //if the parameters of this split is 7, that means it is a beverage - because
                //it also has a parameter for volume that is needed to reconstruct the object
                if (params.length == 7) {
                    Money money = new Money(new BigDecimal(params[4]), Currency.valueOf(params[5]));
                    add(new Item(params[0], params[1], params[2], money, new BigDecimal(params[6])));
                
                //All other items share the same constructor, so they all go under else:
                } else {
                    add(new Item(params[0], params[1], params[2], ItemType.valueOf(params[3]),
                            new Money(new BigDecimal(params[4]), Currency.valueOf(params[5]))));
                }
            }

        } catch (Exception ex) {
            throw new FileNotFoundException();
        }
    }

    //Exports items currently in the hashmap of items as key and amounts of each item as value.
    public void exportInventory(String fileName) throws FileNotFoundException {
        try(Writer writer = new FileWriter(fileName)) {
            //makes sure there are no leftovers from prior import.
            writer.flush();

            // convert the hashmap of items into a gson string which is 
            //exported as a json with help of the writer. 
            new Gson().toJson(items, writer);

        } catch (Exception ex) {
            throw new FileNotFoundException();
        }
    }

}
