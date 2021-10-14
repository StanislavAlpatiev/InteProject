package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class Receipt {

    private final Date date;
    private final Order order;

    public Receipt(Order order) {
        if (order == null)
            throw new IllegalArgumentException("Payment is null");
        if (order.getItems().isEmpty())
            throw new IllegalArgumentException("Can not create receipt for empty order");
        this.date = new Date();
        this.order = order;
    }

    /**
     * not complete
     */
    public String getReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("==============================\n");
        for (Map.Entry<Item, Integer> e : order.getItems().entrySet()) {
            sb.append(e.getKey().getName()).append("        ");
            if (e.getValue() > 1) {
                sb.append(e.getValue()).append(" x ").append(e.getKey().getPrice().getAmount()).
                        append("     ").append(e.getKey().getPrice().getAmount().multiply(new BigDecimal(e.getValue().toString()))).append("\n");
            } else
                sb.append("       ").append(e.getKey().getPrice().getAmount()).append("\n");
        }
        sb.append("==============================\n");

        return sb.toString();

    }


}
