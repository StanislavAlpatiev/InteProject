package se.su.dsv.RegisterSystem;

public class Register {

    private Currency currency;
    private Inventory inventory;
    private static final Bank bank = new Bank();


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
}