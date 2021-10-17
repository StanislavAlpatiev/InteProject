package se.su.dsv.RegisterSystem;

import java.util.HashMap;

public class Inventory {

    HashMap<Item, Integer> items;

    public Inventory(){
        items = new HashMap<>();
    }

    public Inventory(Item... item){
        if (item == null || item.length == 0){
            throw new IllegalArgumentException();
        } 
        items = new HashMap<>();
        add(item);
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
