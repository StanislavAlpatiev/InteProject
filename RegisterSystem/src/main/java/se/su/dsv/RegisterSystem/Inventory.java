package se.su.dsv.RegisterSystem;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private HashMap<Item, Integer> items;
    private Register register;
    private Currency currency;

    public Inventory(Register register){
        items = new HashMap<>();
        this.register = register;
        currency = register.getCurrency();
    }

    public Inventory(Register register, Item... item){
        if (item == null || item.length == 0){
            throw new IllegalArgumentException();
        } 
        items = new HashMap<>();
        add(item);
        this.register = register;
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
        return new HashMap<Item, Integer>(items);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        if(this.currency != currency){
            //iterate over items and change them all. Call upon items internal things to change their amounts from there. 
            for (Map.Entry<Item, Integer> entry : this.items.entrySet()){
                Money price = register.getBank().exchange(entry.getKey().getPrice());
                entry.getKey().setPrice(price);
            }
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
