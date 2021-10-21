package se.su.dsv.RegisterSystem;

import java.io.IOException;
import java.util.HashMap;

//TODO: add comments to each method
public class Register {

    private Currency currency;
    private final Inventory inventory;
    private static final Bank bank = new Bank();
    private HashMap<Item, Integer> itemsBeingBought = new HashMap<>();

    public Register(Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Null currency!");
        }
        this.currency = currency;
        inventory = new Inventory(bank, currency);
    }

    public void checkOut(Order order, Wallet wallet) {
        // Räknar ihop summan av alla items som ska köpas, betalas med pengarna ur en
        // customers wallet, listan av items töms och kvittot skrivs ut,
    }

    // It will throw illegalargument if currency is null. If there is anything wrong
    // with the API bank conversion, it throws IOException.
    public void setCurrency(Currency currency) throws IllegalArgumentException, IOException {
        if (currency == null) {
            throw new IllegalArgumentException("Null currency!");
        }
        this.currency = currency;
        inventory.setCurrency(currency);
    }

    public void addItem() {
        // Lägger till ett item
    }

    public void removeItem() {
        // Tar bort ett item
    }



    public Currency getCurrency() {
        return currency;
    }

    public Bank getBank() {
        return bank;
    }

    public Inventory getInventory() {
        return inventory;
    }

}
