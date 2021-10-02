package se.su.dsv.checkout.mutual;

public abstract class Goods {
    private final String name;
    private final int price;

    protected Goods(String name, int price) {
        this.name = name;
        this.price = price;
    }

    // Returns name of goods item
    public String getName() {
        return name;
    }

    // Returns price of goods item
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
