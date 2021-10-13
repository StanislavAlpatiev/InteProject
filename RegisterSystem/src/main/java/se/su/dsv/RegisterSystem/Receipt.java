package se.su.dsv.RegisterSystem;

import java.util.ArrayList;
import java.util.Date;

public class Receipt {

    private Date date;
    private ArrayList<Item> items;

    public Receipt(Payment payment){
        if (payment == null)
            throw new IllegalArgumentException("Payment is null");
        date = date;
        items = items;
    }


}
