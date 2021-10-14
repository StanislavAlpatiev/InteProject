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

    /**
     * unreadalble and too long, will split up in different methods
     */
    public String getReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("==============================\n");

        BigDecimal totalVat6 = new BigDecimal("0");
        BigDecimal totalVat12 = new BigDecimal("0");
        BigDecimal totalVat25 = new BigDecimal("0");
        BigDecimal total = new BigDecimal("0");

        for (Map.Entry<Item, Integer> e : order.getItems().entrySet()) {

            String currentItemName = e.getKey().getName();
            BigDecimal currentItemPrice = e.getKey().getPricePlusVat().getAmount().setScale(2, RoundingMode.CEILING);
            int noOfItems = e.getValue();

            sb.append(currentItemName).append("        ");

            if (e.getValue() > 1) {
                sb.append(e.getValue()).append(" x ").append(currentItemPrice).
                        append("     ").append(currentItemPrice.multiply(new BigDecimal(noOfItems))).append("\n");
            } else {
                sb.append("       ").append(currentItemPrice).append("\n");
            }

            total = total.add(currentItemPrice.multiply(new BigDecimal(noOfItems)));

            if (e.getKey().getVAT() == 0.06) {
                totalVat6 = totalVat6.add(currentItemPrice.multiply(new BigDecimal(noOfItems)));
            }
            if (e.getKey().getVAT() == 0.12) {
                totalVat12 = totalVat12.add(currentItemPrice.multiply(new BigDecimal(noOfItems)));
            }
            if (e.getKey().getVAT() == 0.25) {
                totalVat25 = totalVat25.add(currentItemPrice.multiply(new BigDecimal(noOfItems)));
            }
        }
        sb.append("==============================\n");
        sb.append("TOTAL:            ").append(total.setScale(2, RoundingMode.CEILING).toString()).append("\n");
        sb.append("Moms%    Moms     Netto     Brutto\n");

        if (!totalVat6.toString().equals("0")) {
            BigDecimal moms = totalVat6.multiply(new BigDecimal("0.06")).setScale(2, RoundingMode.CEILING);
            BigDecimal netto = totalVat6.subtract(moms).setScale(2, RoundingMode.CEILING);
            totalVat6 = totalVat6.setScale(2, RoundingMode.CEILING);
            sb.append("6.00    ").append(moms.toString()).append("       ").append(netto.toString()).append("        ").append(totalVat6.toString()).append("\n");
        }
        if (!totalVat12.toString().equals("0")) {
            BigDecimal moms = totalVat12.multiply(new BigDecimal("0.12")).setScale(2, RoundingMode.CEILING);
            BigDecimal netto = totalVat12.subtract(moms).setScale(2, RoundingMode.CEILING);
            totalVat12 = totalVat12.setScale(2, RoundingMode.CEILING);
            sb.append("12.00    ").append(moms.toString()).append("     ").append(netto.toString()).append("      ").append(totalVat12.toString()).append("\n");
        }
        if (!totalVat25.toString().equals("0")) {
            BigDecimal moms = totalVat25.multiply(new BigDecimal("0.25")).setScale(2, RoundingMode.CEILING);
            BigDecimal netto = totalVat25.subtract(moms).setScale(2, RoundingMode.CEILING);
            totalVat25 = totalVat25.setScale(2, RoundingMode.CEILING);
            sb.append("25.00    ").append(moms.toString()).append("        ").append(netto.toString()).append("         ").append(totalVat25.toString()).append("\n");
        }
        return sb.toString();

    }


}
