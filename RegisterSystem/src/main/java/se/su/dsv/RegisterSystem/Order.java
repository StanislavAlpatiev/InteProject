package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Order {

    //TODO clean up and write more comments


    private final HashMap<Item, BigDecimal> items = new HashMap<>();

    private final String number;
    private final Currency currency;

    private Money totalPricePlusVat;


    public Order(Currency currency) {
        if (currency == null)
            throw new IllegalArgumentException("Null currency");
        this.currency = currency;
        number = generateOrderNumber();
        totalPricePlusVat = new Money(BigDecimal.ZERO, currency);
    }

    public Order(Currency currency, Item... items) {
        this(currency);
        if (items == null)
            throw new IllegalArgumentException("Null item");
        addItem(items);
    }

    public void addItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        if (item.getPrice().getCurrency() != currency)
            throw new IllegalArgumentException("Item price has wrong currency");
        if (!items.containsKey(item))
            items.put(item, BigDecimal.ONE);
        else
            // if item is present in the amount of it increases
            items.put(item, items.get(item).add(BigDecimal.ONE));
        //the total price increases by the gross price of the item
        totalPricePlusVat = totalPricePlusVat.add(item.getPricePlusVatAndPant());

    }

    public void addItem(Item... items) {
        for (Item item : items) {
            addItem(item);
        }
    }

    public boolean removeItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        if (!items.containsKey(item))
            //the order is unchanged if the item doesn't exist
            return false;
        if (items.get(item).doubleValue() > 1)
            //if there exists multiples of the item the amount of it will decrease
            items.put(item, items.get(item).subtract(BigDecimal.ONE));
        else
            items.remove(item);
        //the total price decreases by the gross price of the item
        totalPricePlusVat = totalPricePlusVat.subtract(item.getPricePlusVatAndPant());
        return true;
    }

    public void removeItem(Item... items) {
        for (Item item : items) {
            removeItem(item);
        }
    }

    public void clear() {
        items.clear();
        //resets the total price to zero if all items are removed
        totalPricePlusVat = new Money(BigDecimal.ZERO, currency);
    }

    public Money getTotalPricePlusVat() {
        return totalPricePlusVat;
    }

    /**
     * returns total amount of VAT in the order for a specific VAT rate
     */
    public Money getAmountOfVat(double vatRate) {
        checkValidVat(vatRate);
        BigDecimal result = BigDecimal.ZERO;
        for (Item item : items.keySet()) {
            BigDecimal addend = calculateTotalPerVATRate(item, vatRate, item.getVATAmountOfPrice());
            result = result.add(addend);
        }
        return new Money(result, currency);
    }

    /**
     * returns the total netVAT in the order for a specific VAT rate
     */
    public Money getNetVat(double vatRate) {
        checkValidVat(vatRate);
        BigDecimal result = BigDecimal.ZERO;
        for (Item item : items.keySet()) {
            BigDecimal addend = calculateTotalPerVATRate(item, vatRate, item.getPrice());
            result = result.add(addend);
        }
        return new Money(result, currency);
    }

    /**
     * returns the total grossVAT in the order for a specific VAT rate
     */
    public Money getGrossVat(double vatRate) {
        checkValidVat(vatRate);
        BigDecimal result = BigDecimal.ZERO;
        for (Item item : items.keySet()) {
            BigDecimal addend = calculateTotalPerVATRate(item, vatRate, item.getPricePlusVatAndPant());
            result = result.add(addend);
        }
        return new Money(result, currency);
    }

    /**
     * returns the total grossPrice for a specific item
     */
    public Money getTotalPricePerItem(Item item) {
        checkValidItem(item);
        BigDecimal moneyAmount = item.getPricePlusVatAndPant().getAmount();
        BigDecimal itemAmount = items.get(item);

        return new Money(moneyAmount.multiply(itemAmount), currency);
    }

    /**
     * returns the total pant for a specific item
     */
    public Money getTotalPantPerItem(Item item) {
        checkValidItem(item);
        BigDecimal moneyAmount = item.getPant().getAmount();
        BigDecimal itemAmount = items.get(item);

        return new Money(moneyAmount.multiply(itemAmount), currency);
    }


    public TreeMap<Item, BigDecimal> getItems() {
        return new TreeMap<>(items);
    }


    public String getNumber() {
        return number;
    }


    /**
     * returns an unique ordernumber based on the current date
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


    private void generateDatePartOfOrderNr(StringBuilder sb) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        sb.append(dtf.format(now));
    }

    /**
     * generates four random uppercase letters to be put in the order number
     */
    private void generateEndPartOfOrderNumber(StringBuilder sb) {
        Random r = new Random();

        for (int i = 0; i < 4; i++)
            sb.append((char) (r.nextInt(26) + 'A'));
    }


    private void checkValidVat(double vatRate) {
        for (VAT validRate : VAT.values()){
            if (vatRate == validRate.label)
                return;
        }
            throw new IllegalArgumentException("Not a valid VAT rate");
    }


    /**
     * calculates total of specified item value based on VAT rate of the item
     */
    private BigDecimal calculateTotalPerVATRate(Item item, double vatRate, Money value) {
        if (item.getVat().doubleValue() == vatRate)
            return value.getAmount().multiply(items.get(item));
        return BigDecimal.ZERO;
    }


    private void checkValidItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        if (!items.containsKey(item))
            throw new IllegalArgumentException("Item not found");
    }
}
