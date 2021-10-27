package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeMap;

/**
 * Holds the information for a specific order.
 */

public class Order {

    //TODO clean up and write more comments
    // check att ordernummer inte redan finns?


    //Map of the items in the order with the amount of every item mapped to it
    private final HashMap<Item, BigDecimal> items = new HashMap<>();

    //Maps different VAT rates with the total value of the items represented by the VAT rate in the order
    private final HashMap<Double, Money> totalPricePerVATRate = new HashMap<>();

    private final String number;
    private final Currency currency;

    private Money totalGrossPrice;
    private final HashSet<Item> restrictedItems;


    public Order(Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Null currency");
        }
        this.currency = currency;
        number = generateOrderNumber();
        totalGrossPrice = new Money(BigDecimal.ZERO, currency);
        restrictedItems = new HashSet<>();
        setUpTotalPricePerVATRate();
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
        verifyValidItem(item);
        if (itemNotInOrder(item))
            items.put(item, BigDecimal.ONE);

        // if the added item already is present the amount of it increments
        else
            items.put(item, items.get(item).add(BigDecimal.ONE));

        //the total price increases by the gross price of the item
        totalGrossPrice = totalGrossPrice.add(item.getPricePlusVatAndPant());

        //the map for different VAT rate values gets updated
        addTotalPricePerVATRate(item);

        //collection of age restricted items is updated if item is age restricted
        if (item.isAgeRestricted()) {
            restrictedItems.add(item);
        }

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
        verifyValidItem(item);
        //Order is unchanged if the item doesn't exist and method returns false
        if (itemNotInOrder(item))
            return false;

        //if there exists multiples of the item the amount of it will decrease
        if (items.get(item).doubleValue() > 1) {
            items.put(item, items.get(item).subtract(BigDecimal.ONE));
        } else {
            items.remove(item);

            //collection of age restricted items is updated if item is age restricted
            restrictedItems.remove(item);
        }

        //the total price decreases by the gross price of the item
        totalGrossPrice = totalGrossPrice.subtract(item.getPricePlusVatAndPant());
        //the map for different VAT rate values gets updated
        subtractTotalPricePerVATRate(item);
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
        restrictedItems.clear();

        //resets the total price to zero if all items are removed
        totalGrossPrice = new Money(BigDecimal.ZERO, currency);

        //resets the price per vat rate map to zero aswell
        setUpTotalPricePerVATRate();

    }

    public boolean isAgeRestricted() {
        return !restrictedItems.isEmpty();
    }

    public Money getTotalGrossPrice() {
        return totalGrossPrice;
    }

    /**
     * returns total amount of VAT in the order for a specific VAT rate
     */
    public Money getAmountOfVat(double vatRate) {
        verifyValidVat(vatRate);

        //to calculate the amount of VAT we multiplicate the net VAT with the vat rate
        BigDecimal multiplicand = new BigDecimal(vatRate);
        BigDecimal result = getNetVat(vatRate).getAmount().multiply(multiplicand);
        return new Money(result, currency);
    }

    /**
     * returns the total net VAT in the order for a specific VAT rate
     */
    public Money getNetVat(double vatRate) {
        verifyValidVat(vatRate);

        //to calculate netVat the grossVat is divided by 1 + the vat rate, for example 1.06, 1.12 or 1.25
        BigDecimal divisor = new BigDecimal(1 + vatRate);
        BigDecimal result = getGrossVat(vatRate).getAmount().divide(divisor, 2, RoundingMode.HALF_UP);
        return new Money(result, currency);

    }

    /**
     * returns the total gross VAT in the order for a specific VAT rate
     */
    public Money getGrossVat(double vatRate) {
        verifyValidVat(vatRate);
        return totalPricePerVATRate.get(vatRate);
    }

    /**
     * Returns the total gross price for a specific item and its duplicates
     */
    public Money getTotalPricePerItem(Item item) {
        verifyValidItem(item);

        // returns the item gross price multiplied with the amount of the item
        return getValuePerItem(item, item.getPricePlusVatAndPant());
    }

    /**
     * Returns the total pant for a specific item and its duplicates
     */
    public Money getTotalPantPerItem(Item item) {
        verifyValidItem(item);
        // returns the item pant value multiplied with the amount of the item
        return getValuePerItem(item, item.getPant());
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

        for (int i = 0; i < 4; i++) {
            sb.append((char) (r.nextInt(26) + 'A'));
        }
    }


    /**
     * Verifies whether a VAT rate is valid by throwing an exception if it is not
     */
    private void verifyValidVat(double vatRate) {
        if(!totalPricePerVATRate.containsKey(vatRate))
            throw new IllegalArgumentException("VAT not represented in order");

    }

    /**
     * Checks whether a item is in the order
     */
    private boolean itemNotInOrder(Item item) {
        return !items.containsKey(item);
    }

    /**
     * Verifies whether a item is valid by throwing an exception if it is null or has the wromg currency
     */
    private void verifyValidItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        if (item.getPrice().getCurrency() != currency)
            throw new IllegalArgumentException("Item price has wrong currency");
    }

    /**
     * Helper method to get a specific value for one type of item and its duplicates in the order
     */
    private Money getValuePerItem(Item item, Money money) {
        verifyValidItem(item);
        if (itemNotInOrder(item))
            throw new IllegalArgumentException("Item not in order");
        BigDecimal moneyAmount = money.getAmount();
        BigDecimal itemAmount = items.get(item);

        return new Money(moneyAmount.multiply(itemAmount), currency);
    }

    /**Initiates the total price per VAT rate map with the vat rates and value zero */
    private void setUpTotalPricePerVATRate(){
        for (VAT rate : VAT.values())
            totalPricePerVATRate.put(rate.label, new Money(BigDecimal.ZERO, currency));
    }

    /**Updates the the total price per VAT rate map for an added item*/
    private void addTotalPricePerVATRate(Item item){
        //the old value mapped to the vat rate of the item gets added with the value of the new item
        //the new summed value then replaces the old value in the map
        double vatRate = item.getVat().doubleValue();
        Money updatedValue = totalPricePerVATRate.get(vatRate).add(item.getPricePlusVatAndPant());
        totalPricePerVATRate.put(vatRate, updatedValue);
    }

    /**Updates the the total price per VAT rate map for a subtracted item*/
    private void subtractTotalPricePerVATRate(Item item){
        //the old value mapped to the vat rate of the item gets subtracted with the value of the new item
        //the new subtracted value then replaces the old value in the map
        double vatRate = item.getVat().doubleValue();
        Money updatedValue = totalPricePerVATRate.get(vatRate).subtract(item.getPricePlusVatAndPant());
        totalPricePerVATRate.put(vatRate, updatedValue);
    }
}
