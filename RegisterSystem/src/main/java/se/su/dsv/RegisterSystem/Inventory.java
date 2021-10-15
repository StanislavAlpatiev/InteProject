package se.su.dsv.RegisterSystem;

import java.util.HashMap;

public class Inventory {

    private HashMap<Item, Integer> items;
    private Currency currency;

    public Inventory(Currency currency){
        items = new HashMap<>();
        this.currency = currency;
    }

    public Inventory(Currency currency, Item... item){
        if (item == null || item.length == 0){
            throw new IllegalArgumentException();
        } 
        items = new HashMap<>();
        add(item);
        this.currency = currency;
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
        return new HashMap<Item, Integer>(items);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        if(this.currency != currency){
            //iterate over items and change them all. Call upon items internal things to change their amounts from there. 
        }
    }

    public boolean isAvailable(Item item){
        return items.containsKey(item);
    }

    public void importInventory(){

    }

    public void importInventory(String location){

    }

    public void exportInventory(){

    }

    public void exportInventory(String location){
        
    }

}
