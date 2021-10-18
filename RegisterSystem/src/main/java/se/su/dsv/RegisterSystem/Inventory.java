package se.su.dsv.RegisterSystem;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private HashMap<Item, Integer> items;
    private BankService bank;
    private Currency currency;

    public Inventory(BankService bank, Currency currency){
        items = new HashMap<>();
        this.currency = currency;
        this.bank = bank;
    }

    public Inventory(BankService bank, Currency currency, Item... item) throws IOException{
        if (item == null || item.length == 0){
            throw new IllegalArgumentException();
        } 
        items = new HashMap<>();
        this.currency = currency;
        this.bank = bank;
        add(item);
    }

    public void add(Item... item) throws IOException{
        for (Item i : item){
            if(i.getPrice().getCurrency() != this.currency){
                i.setPrice(bank.exchange(i.getPrice(), this.currency));
            }
            items.put(i, (isAvailable(i)) ? items.get(i) + 1 : 1);
        }
    }

    public void remove(Item item){
        if(isAvailable(item)){
            items.put(item, items.get(item)-1);
            if(items.get(item) == 0){
                items.remove(item);
            }
        }
    }

    public HashMap<Item, Integer> getItems(){
        return new HashMap<>(items);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) throws IOException {
        if(this.currency != currency){
            BigDecimal rate = bank.getRate(this.currency, currency);
            for (Map.Entry<Item, Integer> entry : this.items.entrySet()){
                Money price = bank.exchange(entry.getKey().getPrice(), currency, rate);
                entry.getKey().setPrice(price);
            }
            this.currency = currency;
        }
    }

    public boolean isAvailable(Item item){
        return items.containsKey(item);
    }

    public void importInventory(){
        importInventory("default");
    }

    public void importInventory(String fileName){
        HashMap<Item, Integer> newItems = new HashMap<>();
    }

    public void exportInventory(){
        exportInventory("default");
    }

    public void exportInventory(String fileName){
        for (Map.Entry<Item, Integer> entry : items.entrySet()){

        }
    }

}
