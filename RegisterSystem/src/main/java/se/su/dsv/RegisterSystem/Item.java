package se.su.dsv.RegisterSystem;

public abstract class Item implements Vat {

    private Money price;
    private String name;
    private String productNo;
    private String producer;
    private boolean ageRestricted;

    public String getName(){
        return name;
    }

    public Money getPrice(){
        return price;
    }

}
