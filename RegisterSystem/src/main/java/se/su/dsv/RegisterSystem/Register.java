package se.su.dsv.RegisterSystem;

import java.util.HashMap;

public class Register {

    private Currency currency;
    private final Inventory inventory;
    private static final Bank bank = new Bank();
    private HashMap<Item, Integer> itemsBeingBought = new HashMap<>();



    public Register(Currency currency){
        this.currency = currency;
        inventory = new Inventory(bank, currency);
    }

    public Currency getCurrency(){
        return currency;
    }

    public Bank getBank(){
        return bank;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setCurrency(Currency otherCurrency) {
    public void addItem(){
        // Lägger till ett item
    }

    public void removeItem(){
        // Tar bort ett item
    }

    public void checkOut(){
        // Räknar ihop summan av alla items som ska köpas, betalas med pengarna ur en customers wallet, listan av items töms och kvittot skrivs ut,
    }

}
