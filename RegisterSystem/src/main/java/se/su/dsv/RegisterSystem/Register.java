package se.su.dsv.RegisterSystem;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

    //Core method of system. Items bought in the order are being deducted from inventory,
    //and money in wallet is being deducted by the total cost of the order.
    public void checkOut(Order order, Wallet wallet) throws IOException {
        if(order == null || wallet == null){
            throw new IllegalArgumentException("Null arguments into checkout");
        }

        //checks whether item in order are available in the inventory
        if(!inventory.isAvailable(order)){
            throw new IllegalArgumentException("Item not available!");
        }

        Money toPay = order.getTotalPricePlusVat();
        Money walletMoney = wallet.totalValueInCurrency(currency);

        //checks whether there is enough money in wallet to pay for order
        if(toPay.compareTo(walletMoney)>0){
            throw new IllegalArgumentException();
        }

        updateInventory(order);
        updateWallet(wallet, toPay);
    }

    //Helper method for checkout, removes items from inventory when they're bought.
    private void updateInventory(Order order){
        TreeMap<Item, BigDecimal> itemsBeingBought = order.getItems();

        //Loops over each item
        for(Map.Entry<Item, BigDecimal> entry : itemsBeingBought.entrySet()){
            int amountOfItems = entry.getValue().intValue();

            //Loops over how many of each item there is.
            for(int i = 0; i < amountOfItems; i++){
                inventory.remove(entry.getKey());
            }
        }
    }

    //Helper method for checkout, removes total cost of order from wallet.
    private void updateWallet(Wallet wallet, Money toPay){
        //Currently only removes in one currency!
        wallet.remove(toPay);
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
