package se.su.dsv.RegisterSystem;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;


public class Receipt {

    //TODO: write more comments

    static final int WIDTH = 83;
    static final String ROW_DIVIDER = new String(new char[WIDTH]).replace("\0", "=");
    static final String EMPTY_COLUMN = "    ";

    private final Order order;
    private final String date;
    private final String time;
    private final String receipt;


    public Receipt(Order order) {
        this(order, new Date());

    }

    // able to send in date as parameter to allow for testing
    public Receipt(Order order, Date date){
        if (order == null)
            throw new IllegalArgumentException("Payment is null");
        if (date == null)
            throw new IllegalArgumentException("Date is null");
        if (order.getItems().isEmpty())
            throw new IllegalArgumentException("Can not create receipt for empty order");
        this.order = order;
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(date);
        this.time = new SimpleDateFormat("HH:mm").format(date);
        receipt = createReceipt();
    }

    public String getReceipt(){
        return receipt;
    }

    public void printToFile(){
        String fileName = order.getNumber();
        try {
            FileWriter writer = new FileWriter("src\\test\\resources\\" + fileName + ".txt");
            PrintWriter out = new PrintWriter(writer);
            out.print(getReceipt());
            writer.close();
            out.close();
        }catch (FileNotFoundException e) {
            System.err.println("Cant open file");
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }




    private String createReceipt() {
        StringBuilder sb = new StringBuilder();

        String totalPricePlusVat = order.getTotalPricePlusVat().getAmount()
                .setScale(2, RoundingMode.CEILING).toString();

        sb.append(ROW_DIVIDER).append("\n");

        sb.append(formatRow("OrderNr:", order.getNumber(), EMPTY_COLUMN, EMPTY_COLUMN));
        sb.append(formatRow("Datum:", date, "Tid:", time));

        sb.append(ROW_DIVIDER).append("\n");

        for (Map.Entry<Item, BigDecimal> e : order.getItems().entrySet()) {
            Item currentItem = e.getKey();
            BigDecimal amountOfCurrentItem = e.getValue();
            sb.append(createItemRow(currentItem, amountOfCurrentItem));
        }
        sb.append(ROW_DIVIDER).append("\n");
        sb.append(formatRow("TOTAL",EMPTY_COLUMN,EMPTY_COLUMN, totalPricePlusVat));
        sb.append(formatRow("Moms %", "Moms", "Netto", "Brutto"));

        for (VAT rate : VAT.values())
            sb.append(createVATRow(rate.label));

        sb.append(ROW_DIVIDER);


        return sb.toString();
    }


    // formats to row so it is lined up as four columns
    private String formatRow(String column1, String column2, String column3, String column4){
        String row = String.format("%-20s %20s %20s %20s", column1, column2, column3, column4);
        if (row.length() > WIDTH)
            throw new IllegalStateException("Out of characters for row");
        return row + "\n";
    }


    // creates row for specific item with name of item and total price for that item.
    //if multiple items it will account for that aswell as pant
    private String createItemRow(Item item, BigDecimal amountOfItem) {
        String itemName = item.getName();
        String itemPricePlusVat = item.getPricePlusVat().getAmount()
                .setScale(2, RoundingMode.CEILING).toString();
        String totalItemPricePlusVat = order.getTotalPricePerItem(item).getAmount()
                .setScale(2, RoundingMode.CEILING).toString();
        String totalItemPant = order.getTotalPantPerItem(item).getAmount()
                .setScale(2, RoundingMode.CEILING).toString();

        String pantColumn = (totalItemPant.equals("0.00")) ? EMPTY_COLUMN :
                "inkl. pant " + totalItemPant;

        if (amountOfItem.intValue() > 1)
            return formatRow(itemName, amountOfItem + "*" +
                    itemPricePlusVat, pantColumn, totalItemPricePlusVat);
        else
            return formatRow(itemName, EMPTY_COLUMN, pantColumn, itemPricePlusVat);

    }

    // creates the account for the different vats represented by the items in the receipt
    // will not account vats not represented by any item
    private String createVATRow(double VAT) {

        String totalAmountOfVAT = order.getAmountOfVat(VAT).getAmount()
                .setScale(2, RoundingMode.CEILING).toString();
        String totalNetVAT = order.getNetVat(VAT).getAmount()
                .setScale(2, RoundingMode.CEILING).toString();
        String totalGrossVat = order.getGrossVat(VAT).getAmount()
                .setScale(2, RoundingMode.CEILING).toString();

        if (totalAmountOfVAT.equals("0.00"))
            return "";

        return formatRow(VAT * 100 + "0", totalAmountOfVAT, totalNetVAT, totalGrossVat);
    }

}
