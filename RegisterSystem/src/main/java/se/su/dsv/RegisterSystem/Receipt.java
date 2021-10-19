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


    private final double[] VAT_RATES = {0.06, 0.12, 0.25};
    private final int WIDTH = 83;
    private final String ROW_DIVIDER = new String(new char[WIDTH]).replace("\0", "=");
    private final String EMPTY_COLUMN = "    ";

    private final Order order;
    private final String date;
    private final String time;
    private final String receipt;


    private final TreeMap<Double, BigDecimal> VATs = new TreeMap<>(); // maps the different vat-rates with amount of VAT in receipt for that rate
    private final TreeMap<Double, BigDecimal> netVATs = new TreeMap<>(); // maps the different vat-rates with amount of netVAT in receipt for that rate
    private final TreeMap<Double, BigDecimal> grossVATs = new TreeMap<>(); // maps the different vat-rates with amount of grossVAT in receipt for that rate

    private BigDecimal totalPricePlusVat = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);

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
        setUpVATsMaps();
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

        sb.append(ROW_DIVIDER).append("\n");

        sb.append(formatRow("OrderNr:", order.getNumber(), EMPTY_COLUMN, EMPTY_COLUMN));
        sb.append(formatRow("Datum:", date, "Tid:", time));

        sb.append(ROW_DIVIDER).append("\n");

        // creates row for each unique item in order and sums the item prices (plus vat)
        // aswell as make vat calculations for each item
        for (Map.Entry<Item, Integer> e : order.getItems().entrySet()) {

            Item currentItem = e.getKey();
            BigDecimal amountOfCurrentItem = new BigDecimal(e.getValue().toString());

            sb.append(createItemRow(currentItem, amountOfCurrentItem));

            calculateVat(currentItem, amountOfCurrentItem);
        }

        sb.append(ROW_DIVIDER).append("\n");

        //create row for the total price
        sb.append(formatRow("TOTAL",EMPTY_COLUMN,EMPTY_COLUMN, totalPricePlusVat.setScale(2, RoundingMode.CEILING).toString()));

        sb.append(formatRow("Moms %", "Moms", "Netto", "Brutto"));

        //creates the rows accounting vats
        sb.append(createVATRows());

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
    //if multiple items it will account for that aswell aswell as pant
    private String createItemRow(Item item, BigDecimal amountOfItem) {
        StringBuilder sb = new StringBuilder();

        String itemName = item.getName();
        BigDecimal itemPricePlusVat = item.getPricePlusVat().getAmount()
                .setScale(2, RoundingMode.CEILING);
        BigDecimal totalItemPricePlusVat = itemPricePlusVat.multiply(amountOfItem);
        BigDecimal totalItemPant = item.getPant().setScale(2, RoundingMode.CEILING).multiply(amountOfItem);

        String pantColumn = (totalItemPant.toString().equals("0.00")) ? EMPTY_COLUMN :
                "inkl. pant " + totalItemPant.toString();

        if (amountOfItem.intValue() > 1)
            sb.append(formatRow(itemName, amountOfItem + "*" +
                    itemPricePlusVat.toString(), pantColumn, totalItemPricePlusVat.toString()));
        else
            sb.append(formatRow(itemName, EMPTY_COLUMN, pantColumn, itemPricePlusVat.toString()));

        totalPricePlusVat = totalPricePlusVat.add(totalItemPricePlusVat);
        return sb.toString();

    }

    // creates the account for the different vats represented by the items in the receipt
    // will not account vats not represented by any item
    private String createVATRows() {
        StringBuilder sb = new StringBuilder();
        String totalAmountOfVAT;
        String totalNetVAT;
        String totalGrossVat;

        for (Double d : VAT_RATES) {
            if (!grossVATs.get(d).toString().equals("0")) {
                totalAmountOfVAT = VATs.get(d).setScale(2, RoundingMode.CEILING).toString();
                totalNetVAT = netVATs.get(d).setScale(2, RoundingMode.CEILING).toString();
                totalGrossVat = grossVATs.get(d).setScale(2, RoundingMode.CEILING).toString();
                sb.append(formatRow(d * 100 + "0", totalAmountOfVAT, totalNetVAT, totalGrossVat));
            }
        }
        return sb.toString();

    }

    // updates the different vat maps based on the vat of the inserted item
    private void calculateVat(Item item, BigDecimal amount) {
        double itemVATRate = item.getVat().doubleValue();

        BigDecimal currentItemPrice = item.getPrice().getAmount()
                .setScale(2, RoundingMode.CEILING);
        BigDecimal currentItemPricePlusVat = item.getPricePlusVat().getAmount()
                .setScale(2, RoundingMode.CEILING);
        BigDecimal currentItemAmountOfVAT = item.getVATAmountOfPrice().getAmount()
                .setScale(2, RoundingMode.CEILING);


        BigDecimal old = grossVATs.get(itemVATRate);
        BigDecimal nev = grossVATs.get(itemVATRate).add(currentItemPricePlusVat.multiply(amount));
        grossVATs.replace(item.getVat().doubleValue(), old, nev);

        old = VATs.get(itemVATRate);
        nev = VATs.get(itemVATRate).add(currentItemAmountOfVAT.multiply(amount));
        VATs.replace(itemVATRate, old, nev);

        old = netVATs.get(itemVATRate);
        nev = netVATs.get(itemVATRate).add(currentItemPrice.multiply(amount));
        netVATs.replace(itemVATRate, old, nev);

    }

    //sets up the vat maps with value zero
    private void setUpVATsMaps(){
        for (double vatRate : VAT_RATES) {
            VATs.put(vatRate, BigDecimal.ZERO);
            grossVATs.put(vatRate, BigDecimal.ZERO);
            netVATs.put(vatRate, BigDecimal.ZERO);
        }
    }

}
