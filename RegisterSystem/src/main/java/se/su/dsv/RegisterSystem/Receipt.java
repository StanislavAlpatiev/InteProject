package se.su.dsv.RegisterSystem;

import java.util.Date;


public class Receipt {

    private final Date date;
    private final Order order;

    public Receipt(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Payment is null");
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Can not create receipt for empty order");
        }
        this.date = new Date();
        this.order = order;
    }

    public String getRow(int pos){
        //should return requested row of receipt
        return "";
    }

    public String getReceipt(){
        //should return receipt
        return "";
    }


}
