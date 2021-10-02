package se.su.dsv.checkout.mutual;

import java.util.Date;

public abstract class Goods {
    private final String name;
    private final int price;
    //private final Date expirationDate;

    protected Goods(String name, int price/*, Date expirationDate*/) {
        this.name = name;
        this.price = price;
        //this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    /*public Date getExpirationDate() {
        return expirationDate;
    }*/

    /*Calendar c = Calendar.getInstance();

    c.setTime(new Date()); // Now use today date.

    c.add(Calendar.DATE, 15); // Adds 15 days

    c.getTime(); // Returns Date Object

    c.set(int year, int month, int date) // Sets the values for the calendar fields YEAR, MONTH, and DAY_OF_MONTH*/


}
