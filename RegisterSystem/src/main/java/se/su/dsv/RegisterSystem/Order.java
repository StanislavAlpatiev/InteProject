package se.su.dsv.RegisterSystem;

import java.util.*;

public class Order {

    private final TreeMap<Item, Integer> items = new TreeMap<>();
    private final String number;


    public Order() {
        number = generateOrderNumber();
    }

    public Order(Item... items) {
        this();
        if (items == null)
            throw new IllegalArgumentException("Null item");
        for (Item item : items)
            addItem(item);
    }

    /**
     * TODO: should check if order number already exists in database?
     */
    private String generateOrderNumber() {
        if (number != null)
            throw new IllegalStateException("Order number already generated");

        StringBuilder number = new StringBuilder();

        Date d = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(d);

        number.append(c.get(Calendar.YEAR));
        number.append((c.get(Calendar.MONTH) + 1));
        number.append(c.get(Calendar.DAY_OF_MONTH));

        Random r = new Random();
        for (int i = 0; i < 4; i++)
            number.append((char) (r.nextInt(26) + 'A'));

        return number.toString();
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

    public TreeMap<Item, Integer> getItems() {
        return new TreeMap<>(items);
    }

    public String getNumber(){
        return number;
    }

}
