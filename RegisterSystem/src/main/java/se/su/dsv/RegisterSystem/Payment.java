package se.su.dsv.RegisterSystem;

import java.lang.reflect.Array;
import java.util.*;

public class Payment {

    private Date date;
    private TreeMap<Item, Integer> items = new TreeMap<>();

    public Payment(Date date) {
        if (date == null)
            throw new IllegalArgumentException("Null date");
        this.date = date;
    }

    public Payment(Date date, Item... items) {
        this(date);
        if (items == null)
            throw new IllegalArgumentException("Null item");
        for (Item item : items)
            addItem(item);
    }

    public void addItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        items.put(item, (items.containsKey(item)) ? items.get(item) + 1 : 1);
    }

    public boolean removeItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        if (!items.containsKey(item))
            return false;
        if (items.get(item) > 1)
            items.replace(item, items.get(item), items.get(item) - 1);
        else
            items.remove(item);
        return true;
    }
}
