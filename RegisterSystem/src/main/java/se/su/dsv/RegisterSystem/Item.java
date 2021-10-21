package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;
import java.util.Objects;

//Holds all information for a specific item
public class Item implements Comparable<Item> {

    private final String name;
    private final String productNo;
    private final String producer;
    private boolean ageRestricted = false;
    private final ItemType type;
    private Money price;
    private BigDecimal volumeCl;
    private BigDecimal vat;
    private Money pant;


    //first item constructor that sets all attributes and sets the vat for different categories of Items
    public Item(String name, String productNo, String producer, ItemType type, Money price) {

        if(name == null || productNo == null || producer == null || price == null){
            throw new IllegalArgumentException("null parameter in Item constructor");
        }
        //item name cant be longer than 25 characters because it needs to fit on the receipt
        if(name.length() > 25){
            throw new IllegalArgumentException("Item name cant be longer than 25 characters");
        }

        this.name = name;
        this.productNo = productNo;
        this.producer = producer;
        this.type = type;
        this.price = price;
        this.pant = new Money (BigDecimal.ZERO, price.getCurrency());

        setVat();
    }

    //second Item constructor that takes volumeCl as parameter and passes BEVERAGE into the first constructor, then sets pant for the volume of the beverage
    public Item(String name, String productNo, String producer, Money price, BigDecimal volumeCl) {
        this(name, productNo, producer, ItemType.BEVERAGE, price);

        if(volumeCl == null){
            throw new IllegalArgumentException("volumeCl cant be null in Item constructor");
        }

        if(volumeCl.doubleValue() <= 0){
            throw new IllegalArgumentException();
        }

        this.volumeCl = volumeCl;

        setPant(price.getCurrency());

    }

    public String getName() {
        return name;
    }

    public String getProductNo() {
        return productNo;
    }

    public String getProducer() {
        return producer;
    }

    public boolean isAgeRestricted() {
        return ageRestricted;
    }

    public ItemType getType() {
        return type;
    }

    public Money getPrice() {
        return price;
    }

    public Money getPant(){
        return pant;
    }

    public BigDecimal getVat(){
        return vat;
    }

    public BigDecimal getVolumeCl(){
        return volumeCl;
    }

    public void setPrice(Money price){
        this.price = price;
    }

    //sets pant for a specific currency, is only implemented for SEK
    public void setPant(Currency currency){
        if(price.getCurrency() == Currency.SEK){
            if(volumeCl.doubleValue() >= BigDecimal.ONE.doubleValue()){
                this.pant = new Money (new BigDecimal("2"), price.getCurrency());
            }else{
                this.pant = new Money (BigDecimal.ONE, price.getCurrency());
            }
        }else{
            new Money (BigDecimal.ZERO, price.getCurrency());
        }
    }

    //sets vat, is only implemented for swedish tax laws, could be expanded
    public void setVat(){
        switch(type){
            case NEWSPAPER:
                this.vat = new BigDecimal("0.06");
                break;
            case TOBACCO:
                this.vat = new BigDecimal("0.25");
                this.ageRestricted = true;
                break;
            default:
                this.vat = new BigDecimal("0.12");
                break;
        }
    }

    //TODO implement
    //never used, could be used in Inventory.setCurrency()
    public void setCurrency(Currency currency){
        MockBank bank = new MockBank();

        if(currency != price.getCurrency()){
//            bank.exchange(price, currency, new BigDecimal("1.2"));
//            setPant(currency);
//            setVat(currency);
        }
    }

    //returns the full price including vat and pant
    public Money getPricePlusVatAndPant() {
        return price.add(getVATAmountOfPrice().add(pant));
    }

    //returns only the vat amount
    public Money getVATAmountOfPrice() {
        return new Money(price.getAmount().multiply(vat), price.getCurrency());
    }

    //compares Items by name
    @Override
    public int compareTo(Item o) {
        return name.compareTo(o.name);
    }

    //returns equal if the Items are both Items and name, productNo, producer, type and price are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return getName().equals(item.getName()) &&
                getProductNo().equals(item.getProductNo()) &&
                getProducer().equals(item.getProducer()) &&
                getType() == item.getType() &&
                getPrice().equals(item.getPrice());
    }

    //generates hashcode
    @Override
    public int hashCode() {
        return Objects.hash(name, productNo, producer, type, price);
    }

    //returns formatted string
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getName() + "@" + getProductNo() + "@" + getProducer() + "@" + getType() + "@" + getPrice().toExport());
        return sb.toString();
    }
}
