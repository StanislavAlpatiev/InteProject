package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Order {


    private final TreeMap<Item, BigDecimal> items = new TreeMap<>();
    private final HashMap<Item, Money> pricePerItem = new HashMap<>();
    private final HashMap<Item, Money> pantPerItem = new HashMap<>();


    private final HashMap<Double, Money> VATs = new HashMap<>(); // maps the different vat-rates with amount of VAT for that rate
    private final HashMap<Double, Money> netVATs = new HashMap<>(); // maps the different vat-rates with amount of netVAT for that rate
    private final HashMap<Double, Money> grossVATs = new HashMap<>(); // maps the different vat-rates with amount of grossVAT for that rate

    private final String number;

    private Money totalPricePlusVat;


    public Order() {
        setUp();
        number = generateOrderNumber();
    }

    public Order(Item... items) {
        this();
        if (items == null)
            throw new IllegalArgumentException("Null item");
        for (Item item : items)
            addItem(item);
    }

    public void addItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        addToItemMaps(item);
        addToVATMaps(item);

        totalPricePlusVat = totalPricePlusVat.add(item.getPricePlusVatAndPant());

    }

    public void addItem(Item... items) {
        for (Item item : items){
            addItem(item);
        }
    }

    public boolean removeItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        if (!items.containsKey(item))
            return false;
        totalPricePlusVat = totalPricePlusVat.subtract(item.getPricePlusVatAndPant());
        subtractFromItemMaps(item);
        subtractFromVATMaps(item);
        return true;
    }

    public void removeItem(Item... items) {
        for (Item item : items){
            removeItem(item);
        }
    }

    public Money getTotalPricePlusVat() {
        return totalPricePlusVat;
    }

    public Money getAmountOfVat(double vatRate) {
        if (VAT.valueOfLabel(vatRate) == 0)
            throw new IllegalArgumentException("Not a valid VAT rate");
        return VATs.get(vatRate);
    }

    public Money getNetVat(double vatRate) {
        if (VAT.valueOfLabel(vatRate) == 0)
            throw new IllegalArgumentException("Not a valid VAT rate");
        return netVATs.get(vatRate);
    }

    public Money getGrossVat(double vatRate) {
        if (VAT.valueOfLabel(vatRate) == 0)
            throw new IllegalArgumentException("Not a valid VAT rate");
        return grossVATs.get(vatRate);
    }

    public Money getTotalPricePerItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        if (!pricePerItem.containsKey(item))
            throw new IllegalArgumentException("Item not found");
        return pricePerItem.get(item);
    }

    public Money getTotalPantPerItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null item");
        if (!pantPerItem.containsKey(item))
            throw new IllegalArgumentException("Item not found");
        return pantPerItem.get(item);
    }


    public TreeMap<Item, BigDecimal> getItems() {
        return new TreeMap<>(items);
    }


    public String getNumber() {
        return number;
    }

    //sets up the vat maps with value zero
    private void setUp() {
        Money zero = new Money(BigDecimal.ZERO, Currency.SEK);
        totalPricePlusVat = zero;
        for (VAT vatRate : VAT.values()) {
            VATs.put(vatRate.label, zero);
            grossVATs.put(vatRate.label, zero);
            netVATs.put(vatRate.label, zero);
        }
    }

    /**
     * TODO: should check if order number already exists in database?
     */
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

    private void generateEndPartOfOrderNumber(StringBuilder sb) {
        Random r = new Random();

        for (int i = 0; i < 4; i++)
            sb.append((char) (r.nextInt(26) + 'A'));
    }

    private void addToVATMaps(Item item) {
        double vatRate = item.getVat().doubleValue();

        grossVATs.put(vatRate, grossVATs.get(vatRate).add(item.getPricePlusVatAndPant()));
        netVATs.put(vatRate, netVATs.get(vatRate).add(item.getPrice()));
        VATs.put(vatRate, VATs.get(vatRate).add(item.getVATAmountOfPrice()));
    }

    private void subtractFromVATMaps(Item item) {
        double vatRate = item.getVat().doubleValue();
<<<<<<< HEAD
        grossVATs.put(vatRate, grossVATs.get(vatRate).subtract(item.getPricePlusVatAndPant()));
=======

        grossVATs.put(vatRate, grossVATs.get(vatRate).subtract(item.getPricePlusVat()));
>>>>>>> ea8879453f8f2c68a7f7141feafb78cd51e5e8b7
        netVATs.put(vatRate, netVATs.get(vatRate).subtract(item.getPrice()));
        VATs.put(vatRate, VATs.get(vatRate).subtract(item.getVATAmountOfPrice()));
    }

    private void addToItemMaps(Item item) {
        if (items.containsKey(item)) {

            items.put(item, items.get(item).add(BigDecimal.ONE));
            pricePerItem.put(item, pricePerItem.get(item).add(item.getPricePlusVatAndPant()));
            pantPerItem.put(item, pantPerItem.get(item).add(item.getPant()));

        } else {

            items.put(item, BigDecimal.ONE);
            pricePerItem.put(item, item.getPricePlusVatAndPant());
            pantPerItem.put(item, item.getPant());
        }
    }

    private void subtractFromItemMaps(Item item) {
        if (items.get(item).doubleValue() > 1) {

            items.put(item, items.get(item).subtract(BigDecimal.ONE));
            pricePerItem.put(item, pricePerItem.get(item).subtract(item.getPricePlusVatAndPant()));
            pantPerItem.put(item, pantPerItem.get(item).subtract(item.getPant()));

        } else {

            items.remove(item);
            pricePerItem.remove(item);
            pantPerItem.remove(item);
        }
    }
}
