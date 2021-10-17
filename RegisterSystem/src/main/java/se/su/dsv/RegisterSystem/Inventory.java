package se.su.dsv.RegisterSystem;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private HashMap<Item, Integer> items;
    private Currency currency;

    public Inventory(Register register){
        items = new HashMap<>();
        currency = register.getCurrency();
    }

    public Inventory(Register register, Item... item){
        if (item == null || item.length == 0){
            throw new IllegalArgumentException();
        } 
        items = new HashMap<>();
        add(item);
        currency = register.getCurrency();
    }

    public void add(Item... item){
        for (Item i : item){
            if(isAvailable(i)){
                items.put(i, items.get(i)+1);
            } else {
                items.put(i, 1);
            }
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
            BigDecimal rate = Bank.getRate(this.currency, currency);
            for (Map.Entry<Item, Integer> entry : this.items.entrySet()){
                Money price = Bank.exchange(entry.getKey().getPrice(), currency, rate);
                entry.getKey().setPrice(price);
            }
            this.currency = currency;
        }
    }

    public boolean isAvailable(Item item){
        return items.containsKey(item);
    }

    public void importInventory(){

    }

    public void importInventory(String fileName){

    }

    public void exportInventory(){

    }

    public void exportInventory(String fileName){
        
    }

}
