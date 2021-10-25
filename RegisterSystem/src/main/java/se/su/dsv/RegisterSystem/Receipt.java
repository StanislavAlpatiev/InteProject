package se.su.dsv.RegisterSystem;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * Class representing a receipt for a specific order
 */


public class Receipt {

    //TODO: write more comments

    static final int WIDTH = 83; // width of the receipt

    static final String ROW_DIVIDER =
            new String(new char[WIDTH]).replace("\0", "="); //splits up rows of the receipt

    //the receipt is structured into four columns so this constant is used to fill in empty columns
    static final String EMPTY_COLUMN = "    ";

    private final Order order;
    private final String date;
    private final String time;
    private final String receipt;


    public Receipt(Order order) {
        this(order, new Date());

    }


    public Receipt(Order order, Date date) { //Constructor is able to send in date as parameter to allow for testing
        if (order == null) {
            throw new IllegalArgumentException("Payment is null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date is null");
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Can not create receipt for empty order");
        }
        this.order = order;
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(date);
        this.time = new SimpleDateFormat("HH:mm").format(date);
        receipt = createReceipt();
    }

    public String getReceipt() {
        return receipt;
    }


    /**
     * Prints the receipt to a textfile
     */
    public void printToFile() {
        String fileName = order.getNumber();
        String pathName = "src\\test\\resources\\" + fileName + ".txt";
        File file = new File(pathName);
        if (file.exists()) {
            throw new IllegalStateException("File already exists");
        }
        try {
            FileWriter writer = new FileWriter(pathName);
            PrintWriter out = new PrintWriter(writer);
            out.print(getReceipt());
            writer.close();
            out.close();
        } catch (FileNotFoundException e) {
            System.err.println("Cant open file");
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }


    /**
     * Generates the receipt as a string
     */
    private String createReceipt() {
        StringBuilder sb = new StringBuilder();


        sb.append(ROW_DIVIDER).append("\n");

        //creating rows for order number and date/time
        sb.append(formatRow("OrderNr:", order.getNumber(), EMPTY_COLUMN, EMPTY_COLUMN));
        sb.append(formatRow("Datum:", date, "Tid:", time));

        sb.append(ROW_DIVIDER).append("\n");

        //loops through every item in the order to create row for each item
        order.getItems().entrySet().forEach(e -> {
            sb.append(createItemRow(e));
        });

        sb.append(ROW_DIVIDER).append("\n");

        //creates row for the total price
        String totalPricePlusVat = formatMoneyValue(order.getTotalGrossPrice());
        sb.append(formatRow("TOTAL", EMPTY_COLUMN, EMPTY_COLUMN, totalPricePlusVat));


        //creates the rows accounting the VATs.
        // If a specific VAT is not represented by an item in the receipt it will not be included
        sb.append(formatRow("Moms %", "Moms", "Netto", "Brutto"));
        for (VAT rate : VAT.values()) {
            sb.append(createVATRow(rate.label));
        }

        sb.append(ROW_DIVIDER);


        return sb.toString();
    }


    /**
     * Formats to row so it is lined up as four columns
     */
    private String formatRow(String column1, String column2, String column3, String column4) {
        String row = String.format("%-20s %20s %20s %20s", column1, column2, column3, column4);
        if (row.length() > WIDTH) {
            throw new IllegalStateException("Out of characters for row");
        }
        return row + "\n";
    }


    /**
     * Creates a row for specific item
     */
    private String createItemRow(Map.Entry<Item, BigDecimal> entry) {
        Item item = entry.getKey();
        BigDecimal amount = entry.getValue();

        String itemName = item.getName();

        //Holds the value of the individual item
        String itemGrossPrice = formatMoneyValue(item.getPricePlusVatAndPant());

        //Holds the total value of the individual item multiplied with the amount of it
        String totalItemGrossPrice = formatMoneyValue(order.getTotalPricePerItem(item));

        //Holds the total pant value of the item
        String totalItemPant = formatMoneyValue(order.getTotalPantPerItem(item));


        //if the item has pant it will be accounted in the third column,
        //otherwise the column will be empty
        String pantColumn = (totalItemPant.equals("0.00")) ? EMPTY_COLUMN :
                "inkl. pant " + totalItemPant;

        //if it is more than one of the item the row will include:
        // the price of the single item, the amount and the total price of all the same items
        if (amount.intValue() > 1) {
            return formatRow(itemName, amount + "*" +
                    itemGrossPrice, pantColumn, totalItemGrossPrice);
        }
        //if it is only one of the item only the name and individual price will be stated
        else {
            return formatRow(itemName, EMPTY_COLUMN, pantColumn, itemGrossPrice);
        }

    }

    /**
     * Creates a row for a VATs represented by items in the receipt
     */
    private String createVATRow(double VAT) {

        //Holds the total amount of VAT in the order for the specific VAT rate
        String totalAmountOfVAT = formatMoneyValue(order.getAmountOfVat(VAT));

        //Holds the total net VAT in the order for the specific VAT rate
        String totalNetVAT = formatMoneyValue(order.getNetVat(VAT));

        //Holds the total gross VAT in the order for the specific VAT rate
        String totalGrossVat = formatMoneyValue(order.getGrossVat(VAT));

        //returns empty string if the VAT is not represented in the order
        if (totalGrossVat.equals("0.00")) {
            return "";
        }

        return formatRow(VAT * 100 + "0", totalAmountOfVAT, totalNetVAT, totalGrossVat);
    }

    /**
     * formats a money value to round to two decimals as a string
     */
    private String formatMoneyValue(Money value) {
        return value.getAmount().setScale(2, RoundingMode.HALF_UP).toString();
    }

}
