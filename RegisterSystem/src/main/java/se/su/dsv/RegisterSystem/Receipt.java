package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

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


}
