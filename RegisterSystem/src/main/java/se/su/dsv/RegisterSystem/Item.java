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
    private BigDecimal volumeLiter;
    private BigDecimal vat;
    private Money pant;


    //first item constructor that sets all attributes and sets the vat for different categories of Items
    public Item(String name, String productNo, String producer, ItemType type, Money price) {
        //item name cant be longer than 25 characters because it needs to fit on the receipt
        argumentRegexFilter(name, "^[\\p{L} -']{1,20}$");
        //item product number must be digits. 
        argumentRegexFilter(productNo, "\\d{1,20}");
        //item producer name cant be longer than 25 characters because it needs to fit on the receipt
        argumentRegexFilter(producer, "^[\\p{L} -']{1,20}$");

        if(price == null){
            throw new IllegalArgumentException("null parameter in Item constructor");
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
        public Item(String name, String productNo, String producer, Money price, BigDecimal volumeLiter) {
            this(name, productNo, producer, ItemType.BEVERAGE, price);
    
            //Checks whether volume is null, zero, or below, and throws if that is the case. 
            if(volumeLiter == null || volumeLiter.compareTo(BigDecimal.ZERO)<=0){
                throw new IllegalArgumentException();
            }
    
            this.volumeLiter = volumeLiter;
    
            setPant();
        }

    private void argumentRegexFilter(String argument, String regex) {
        if(argument == null || argument.isEmpty() || !argument.matches(regex)) {
            throw new IllegalArgumentException(argument + " does not match" + regex);
        }
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

    public BigDecimal getVolumeLiter(){
        return volumeLiter;
    }

    //sets price and changes pant, only changes the price if its not the same
    public void setPrice(Money newPrice){
        if(newPrice == null){
            throw new IllegalArgumentException("setPrice cant set price to null");
        }
        if(!newPrice.equals(price)){
            this.price = newPrice;
            setVat();
            setPant();
        }
    }

    //sets pant for a specific currency, is only implemented for SEK and NOK, would be better to have pant be set to a different country in hindsight
    public void setPant(){
        if(type != ItemType.BEVERAGE){
            return;
        }
        pant = determinePant();
    }
    //Helper method for setPant, see comment for that method for more information.
    private Money determinePant() {
        switch(price.getCurrency()){
            case SEK:
                if(volumeLiter.doubleValue() >= BigDecimal.ONE.doubleValue()){
                    return new Money (new BigDecimal("2"), price.getCurrency());
                }

                return new Money (BigDecimal.ONE, price.getCurrency());

            case NOK:
                if(volumeLiter.doubleValue() >= BigDecimal.ONE.doubleValue()){
                    return new Money (new BigDecimal("3"), price.getCurrency());
                }
                
                return new Money (new BigDecimal("2"), price.getCurrency());

            default:
                return new Money (BigDecimal.ZERO, price.getCurrency());
        }
        
    }

    //sets vat, is only implemented for swedish tax laws, could be expanded
    //should be set for different countries, we have not implemented countries could be done with enum or a complete restructure
    public void setVat(){
        vat = determineVat();
    }

    private BigDecimal determineVat() {
        switch(type){
            case NEWSPAPER:
                return new BigDecimal("0.06");
            case TOBACCO:
                this.ageRestricted = true;
                return new BigDecimal("0.25");
            default:
                return new BigDecimal("0.12");
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
        if(type == ItemType.BEVERAGE){
            sb.append("@" + volumeLiter);
        }
        return sb.toString();
    }
}
