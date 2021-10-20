package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;
import java.util.Objects;

public class Item implements Comparable<Item> {

    private final String name;
    private final String productNo;
    private final String producer;
    private boolean ageRestricted = false;
    private final ItemType type;
    private Money price;
    private BigDecimal volumeCl;
    private final BigDecimal vat;
    private Money pant;


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

    public Item(String name, String productNo, String producer, Money price, BigDecimal volumeCl) {
        this(name, productNo, producer, ItemType.BEVERAGE, price);

        if(volumeCl == null){
            throw new IllegalArgumentException("volumeCl cant be null in Item constructor");
        }

        if(volumeCl.doubleValue() <= 0){
            throw new IllegalArgumentException();
        }

        this.volumeCl = volumeCl;

        //pant is set to SEK if we set up the item in another currency we would have to convert the pant form SEK to the desired currency
        if(price.getCurrency().equals(Currency.SEK)){
            if(volumeCl.doubleValue() >= BigDecimal.ONE.doubleValue()){
                this.pant = new Money (new BigDecimal("2"), price.getCurrency());
            }else{
                this.pant = new Money (BigDecimal.ONE, price.getCurrency());
            }
        }else{
            //TODO implement this
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

    public BigDecimal getVolumeCl(){
        return volumeCl;
    }

    public void setPrice(Money price){
        this.price = price;
    }


    //TODO rename method
    public Money getPricePlusVat() {
        if (type == ItemType.BEVERAGE) {
            return price.add(getVATAmountOfPrice()).add(pant);
        }
        return price.add(getVATAmountOfPrice());
    }

    public Money getVATAmountOfPrice() {
        return new Money(price.getAmount().multiply(vat), price.getCurrency());
    }


    @Override
    public int compareTo(Item o) {
        return name.compareTo(o.name);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (!(o instanceof Item)) {
//            return false;
//        }
//        Item other = (Item) o;
//        return name.equals(other.name);
//
//    }

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

    @Override
    public int hashCode() {
        return Objects.hash(name, productNo, producer, type, price);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getName() + "@" + getProductNo() + "@" + getProducer() + "@" + getType() + "@" + getPrice().toExport());
        return sb.toString();
    }

    //@Override
    public String toStrings() {
        return
            "name='" + getName() + "'" +
            ", productNo='" + getProductNo() + "'" +
            ", producer='" + getProducer() + "'" +
            ", ageRestricted='" + isAgeRestricted() + "'" +
            ", type='" + getType() + "'" +
            ", price='" + getPrice() + "'";
    }
}
