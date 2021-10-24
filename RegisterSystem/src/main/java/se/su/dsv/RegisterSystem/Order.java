package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Holds the information for a specific order.
 */

public class Order {

    //TODO clean up and write more comments
    // göra så att man kan välja hur många av samma item som ska adderas, samma för remove
    // kolla att ordernummer inte finns


    //Map of the items in the order with the amount of every item mapped to it
    private final HashMap<Item, BigDecimal> items = new HashMap<>();

    private final String number;
    private final Currency currency;

    private Money totalGrossPrice;


    public Order(Currency currency) {
        if (currency == null)
            throw new IllegalArgumentException("Null currency");
        this.currency = currency;
        number = generateOrderNumber();
        totalGrossPrice = new Money(BigDecimal.ZERO, currency);
    }

    public Order(Currency currency, Item... items) {
        this(currency);
        if (items == null)
            throw new IllegalArgumentException("Null item");
        addItem(items);
    }

    /**
     * Adds an item to the order
     */
    public void addItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        if (item.getPrice().getCurrency() != currency)
            throw new IllegalArgumentException("Item price has wrong currency");

        if (!items.containsKey(item))
            items.put(item, BigDecimal.ONE);

            // if the added item already is present the amount of it increments
        else
            items.put(item, items.get(item).add(BigDecimal.ONE));
        //the total price increases by the gross price of the item
        totalGrossPrice = totalGrossPrice.add(item.getPricePlusVatAndPant());

    }

    public void addItem(Item... items) {
        for (Item item : items) {
            addItem(item);
        }
    }

    /**
     * Removes an item from the order.
     * Returns true if item is removed or false if the item didn't get removed
     */
    public boolean removeItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        //Order is unchanged if the item doesn't exist and method returns false
        if (!items.containsKey(item))
            return false;
        //if there exists multiples of the item the amount of it will decrease
        if (items.get(item).doubleValue() > 1)
            items.put(item, items.get(item).subtract(BigDecimal.ONE));
        else
            items.remove(item);

        //the total price decreases by the gross price of the item
        totalGrossPrice = totalGrossPrice.subtract(item.getPricePlusVatAndPant());
        return true;
    }

    public void removeItem(Item... items) {
        for (Item item : items) {
            removeItem(item);
        }
    }

    /**
     * Removes all items from the order
     */
    public void clear() {
        items.clear();

        //resets the total price to zero if all items are removed
        totalGrossPrice = new Money(BigDecimal.ZERO, currency);
    }


    public Money getTotalGrossPrice() {
        return totalGrossPrice;
    }

    /**
     * returns total amount of VAT in the order for a specific VAT rate
     */
    public Money getAmountOfVat(double vatRate) {
        verifyValidVat(vatRate);
        BigDecimal[] result = {BigDecimal.ZERO};

        //goes through every item in the order and sums the amount of VAT for the items of the specified VAT rate
        //if it is not found it will return a Money object with value zero
        items.keySet().forEach(i -> {
            if (i.getVat().doubleValue() == vatRate)
                result[0] = result[0].add(i.getVATAmountOfPrice().getAmount());
        });

        return new Money(result[0], currency);
    }

    /**
     * returns the total net VAT in the order for a specific VAT rate
     */
    public Money getNetVat(double vatRate) {
        verifyValidVat(vatRate);
        BigDecimal[] result = {BigDecimal.ZERO};

        //goes through every item in the order and sums the net VAT value for the items of the specified VAT rate
        //if it is not found it will return a Money object with value zero
        items.keySet().forEach(i -> {
            if (i.getVat().doubleValue() == vatRate)
                result[0] = result[0].add(i.getPrice().getAmount());
        });

        return new Money(result[0], currency);
    }

    /**
     * returns the total gross VAT in the order for a specific VAT rate
     */
    public Money getGrossVat(double vatRate) {
        verifyValidVat(vatRate);
        BigDecimal[] result = {BigDecimal.ZERO};

        //goes through every item in the order and sums the gross VAT value for the items of the specified VAT rate
        //if it is not found it will return a Money object with value zero
        items.keySet().forEach(i -> {
            if (i.getVat().doubleValue() == vatRate)
                result[0] = result[0].add(i.getPricePlusVatAndPant().getAmount());
        });

        return new Money(result[0], currency);
    }

    /**
     * Returns the total gross price for a specific item
     */
    public Money getTotalPricePerItem(Item item) {
        verifyValidItem(item);
        BigDecimal moneyAmount = item.getPricePlusVatAndPant().getAmount();
        BigDecimal amount = items.get(item);

        // returns the item gross price multiplied with the amount of the item
        return new Money(moneyAmount.multiply(amount), currency);
    }

    /**
     * Returns the total pant for a specific item
     */
    public Money getTotalPantPerItem(Item item) {
        verifyValidItem(item);
        BigDecimal moneyAmount = item.getPant().getAmount();
        BigDecimal itemAmount = items.get(item);

        // returns the item pant multiplied with the amount of the item
        return new Money(moneyAmount.multiply(itemAmount), currency);
    }


    /**
     * Returns a sorted map of the items in the order
     */
    public TreeMap<Item, BigDecimal> getItems() {
        return new TreeMap<>(items);
    }


    public String getNumber() {
        return number;
    }


    /**
     * Returns an unique ordernumber based on the current date.
     * Allows for 456,976 unique order numbers every day
     */
    //TODO: should check if order number already exists in database?
    private String generateOrderNumber() {
        if (number != null)
            throw new IllegalStateException("Order number already generated");
        StringBuilder sb = new StringBuilder();
        generateDatePartOfOrderNr(sb);
        generateEndPartOfOrderNumber(sb);

        return sb.toString();
    }


    /**
     * Generates the first part of the order number based on the current date
     */
    private void generateDatePartOfOrderNr(StringBuilder sb) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        sb.append(dtf.format(now));
    }

    /**
     * Generates four random uppercase letters to be put in the order number
     */
    private void generateEndPartOfOrderNumber(StringBuilder sb) {
        Random r = new Random();

        for (int i = 0; i < 4; i++)
            sb.append((char) (r.nextInt(26) + 'A'));
    }


    /**
     * Verifies whether a VAT rate is valid by throwing an exception if it is not
     */
    private void verifyValidVat(double vatRate) {
        for (VAT validRate : VAT.values()) {
            if (vatRate == validRate.label)
                return;
        }
        throw new IllegalArgumentException("Not a valid VAT rate");
    }

    /**
     * Verifies whether a item is valid by throwing an exception if it is null or not in the order
     */
    private void verifyValidItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        if (!items.containsKey(item))
            throw new IllegalArgumentException("Item not found");
    }
}
