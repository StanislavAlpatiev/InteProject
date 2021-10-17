package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;

public abstract class Item implements Vat, Comparable<Item> {

    protected final String name;
    protected final String productNo;
    protected final String producer;
    protected final boolean ageRestricted;
    protected final ItemType type;
    protected Money price;

    protected Item(String name, String productNo, String producer, boolean ageRestricted, ItemType type, Money price) {
        this.name = name;
        this.productNo = productNo;
        this.producer = producer;
        this.ageRestricted = ageRestricted;
        this.type = type;
        this.price = price;
    }

    public abstract Money getSalesPrice();

    public final String getName() {
        return name;
    }

    public final String getProductNo() {
        return productNo;
    }

    public final String getProducer() {
        return producer;
    }

    public final boolean isAgeRestricted() {
        return ageRestricted;
    }

    public final ItemType getType() {
        return type;
    }

    public final Money getPrice() {
        return price;
    }

    public final void setPrice(Money price){
        this.price = price;
    }

    public final Money getPricePlusVat() {
        return price.add(getVATAmountOfPrice());
    }

    public final Money getVATAmountOfPrice() {
        BigDecimal vat = new BigDecimal(String.valueOf(getVAT()));
        return new Money(price.getAmount().multiply(vat), price.getCurrency());

    }

    @Override
    public int compareTo(Item o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        Item other = (Item) o;
        return name.equals(other.name);

    }

    public String toExport(){
        StringBuilder sb = new StringBuilder();
        sb.append("@\n" + getName() + "\n" + getProductNo() + "\n" + getProducer() + "\n" + isAgeRestricted() + "\n" + getType() + "\n" + getPrice().toExport());
        return sb.toString();
    }

    @Override
    public String toString() {
        return
            " name='" + getName() + "'" +
            ", productNo='" + getProductNo() + "'" +
            ", producer='" + getProducer() + "'" +
            ", ageRestricted='" + isAgeRestricted() + "'" +
            ", type='" + getType() + "'" +
            ", price='" + getPrice() + "'";
    }
}
