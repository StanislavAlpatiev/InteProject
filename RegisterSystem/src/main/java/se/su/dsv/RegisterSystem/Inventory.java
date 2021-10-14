package se.su.dsv.RegisterSystem;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    HashMap<Item, Integer> items;

    public Inventory(){
        items = new HashMap<>();
    }

    public Inventory(Item... item){
        items = new HashMap<>();
    }

    public void add(Item item){
        if(isAvailable(item)){
            items.put(item, items.get(item)+1);
        } else {
            items.put(item, 1);
        }
    }

    public void remove(Item item){
        if(isAvailable(item)){
            items.put(item, items.get(item)-1);
            if(items.get(item) == 0){
                items.remove(item);
            }
        } 
        return;
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
