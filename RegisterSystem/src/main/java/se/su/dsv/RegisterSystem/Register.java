package se.su.dsv.RegisterSystem;

public class Register {

    private Currency currency;
    private final Inventory inventory;
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

    public Inventory getInventory() {
        return inventory;
    }

    public void setCurrency(Currency otherCurrency) {
    }

}
