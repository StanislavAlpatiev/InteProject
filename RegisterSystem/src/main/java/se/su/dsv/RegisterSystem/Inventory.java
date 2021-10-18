package se.su.dsv.RegisterSystem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import java.util.List;
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

    public void importInventory() throws FileNotFoundException {
        importInventory("default");
    }

    public void importInventory(String fileName) throws FileNotFoundException {
        HashMap<Item, Integer> newItems = new HashMap<>();
        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(fileName));

            items = new Gson().fromJson(new FileReader(fileName), HashMap.class);

            // close reader
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exportInventory(){
        exportInventory("default");
    }

    public void exportInventory(String fileName){
        try {
            // create a writer
            Writer writer = new FileWriter(fileName);
            writer.flush();

            // convert map to JSON File
            new Gson().toJson(items, writer);

            // close the writer
            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
