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
    private BigDecimal pant = BigDecimal.ZERO;


    public Item(String name, String productNo, String producer, ItemType type, Money price) {
        this.name = name;
        this.productNo = productNo;
        this.producer = producer;
        this.type = type;
        this.price = price;

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

        if(volumeCl.doubleValue() <= 0){
            throw new IllegalArgumentException();
        }

        this.volumeCl = volumeCl;
        if(volumeCl.doubleValue() >= BigDecimal.ONE.doubleValue()){
            this.pant = new BigDecimal("2");
        }else{
           this.pant = new BigDecimal("1");
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

    public BigDecimal getVolumeCl() {
        return volumeCl;
    }

    public Money getPrice() {
        return price;
    }

    public BigDecimal getPant(){
        return pant;
    }

    public BigDecimal getVat(){
        return vat;
    }

    public void setPrice(Money price){
        this.price = price;
    }

    public Money getPricePlusVat() {
        if (type == ItemType.BEVERAGE) {
            return price.add(getVATAmountOfPrice()).add(new Money(pant, price.getCurrency()));
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

    public String toExport(){
        StringBuilder sb = new StringBuilder();
        sb.append("@\n" + getName() + "\n" + getProductNo() + "\n" + getProducer() + "\n" + isAgeRestricted() + "\n" + getType() + "\n" + getPrice().toExport());
        return sb.toString();
    }

    @Override
    public String toString() {
        return
            "name='" + getName() + "'" +
            ", productNo='" + getProductNo() + "'" +
            ", producer='" + getProducer() + "'" +
            ", ageRestricted='" + isAgeRestricted() + "'" +
            ", type='" + getType() + "'" +
            ", price='" + getPrice() + "'";
    }
}
