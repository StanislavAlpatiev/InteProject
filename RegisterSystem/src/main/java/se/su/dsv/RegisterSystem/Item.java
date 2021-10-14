package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;

public abstract class Item implements Vat, Comparable<Item> {

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

    public Money getPricePlusVat(){
        return price.add(getVATAmountOfPrice());
    }

    public Money getVATAmountOfPrice(){
        BigDecimal vat = new BigDecimal(String.valueOf(getVAT()));
        return new Money(price.getAmount().multiply(vat), price.getCurrency());

    }

    @Override
    public int compareTo(Item o) {
        int cmp = name.compareTo(o.name);
        if (cmp != 0)
            return cmp;
        return price.compareTo(o.price);
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof Item))
            return false;
        Item other = (Item) o;
        return name.equals(other.name) && price.equals(other.price);

    }

}
