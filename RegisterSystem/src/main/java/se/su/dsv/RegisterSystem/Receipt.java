package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;


public class Receipt {

    static final double VAT6 = 0.06;
    static final double VAT12 = 0.12;
    static final double VAT25 = 0.25;
    static final int WIDTH = 83;
    static final String EMPTY_COLUMN = "    ";

    private final Order order;
    private final String date;
    private final String time;
    private final String receipt;


    private final TreeMap<Double, BigDecimal> VATs = new TreeMap<>(); // maps the different vat-rates with amount of VAT in receipt for that rate
    private final TreeMap<Double, BigDecimal> netVATs = new TreeMap<>(); // maps the different vat-rates with amount of netVAT in receipt for that rate
    private final TreeMap<Double, BigDecimal> grossVATs = new TreeMap<>(); // maps the different vat-rates with amount of grossVAT in receipt for that rate
    private BigDecimal totalPricePlusVat;

    public Receipt(Order order) {
        this(order, new Date());

    }

    // to allow for test with mock date
    public Receipt(Order order, Date date){
        if (order == null)
            throw new IllegalArgumentException("Payment is null");
        if (order.getItems().isEmpty())
            throw new IllegalArgumentException("Can not create receipt for empty order");
        if (date == null)
            throw new IllegalArgumentException("Date is null");
        this.order = order;
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(date);  ;
        this.time = new SimpleDateFormat("HH:mm").format(date);
        totalPricePlusVat = BigDecimal.ZERO;
        setUpVATsMaps();
        receipt = createReceipt();
    }

    public String getReceipt(){
        return receipt;
    }

    public void printToFile(){

    }




    private String createReceipt() {
        StringBuilder sb = new StringBuilder();

        sb.append(new String(new char[WIDTH]).replace("\0", "=")).append("\n");

        sb.append(formatRow("OrderNr:", order.getNumber(), EMPTY_COLUMN, EMPTY_COLUMN));
        sb.append(formatRow("Datum:", date, "Tid:", time));

        sb.append(new String(new char[WIDTH]).replace("\0", "=")).append("\n");

        // creates row for each unique item in order and sums the item prices (plus vat) aswell as make vat calculations for each item
        for (Map.Entry<Item, Integer> e : order.getItems().entrySet()) {
            BigDecimal currentItemPricePlusVat = e.getKey().getPricePlusVat().getAmount().setScale(2, RoundingMode.CEILING);
            int noOfItems = e.getValue();
            sb.append(createItemRow(e.getKey(), e.getValue()));
            totalPricePlusVat = totalPricePlusVat.add(currentItemPricePlusVat.multiply(new BigDecimal(noOfItems)));
            calculateVat(e.getKey(), e.getValue());
        }

        sb.append(new String(new char[WIDTH]).replace("\0", "=")).append("\n");

        //create row for the total price
        sb.append(formatRow("TOTAL",EMPTY_COLUMN,EMPTY_COLUMN, totalPricePlusVat.setScale(2, RoundingMode.CEILING).toString()));

        sb.append(formatRow("Moms %", "Moms", "Netto", "Brutto"));

        //creates the rows accounting vats
        sb.append(createVATRows());

        sb.append(new String(new char[WIDTH]).replace("\0", "="));


        return sb.toString();
    }


    private String formatRow(String column1, String column2, String column3, String column4){
        String row = String.format("%-20s %20s %20s %20s", column1, column2, column3, column4);
        if (row.length() > WIDTH)
            throw new IllegalStateException("Out of characters for row");
        return row + "\n";
    }


    private String createItemRow(Item item, int noOfItems) {
        StringBuilder sb = new StringBuilder();

        String currentItemName = item.getName();
        String currentItemPricePlusVat = item.getPricePlusVat().getAmount().setScale(2, RoundingMode.CEILING).toString();
        String totalCurrentItemPricePlusVat = item.getPricePlusVat().getAmount().setScale(2, RoundingMode.CEILING).multiply(new BigDecimal(noOfItems)).toString();
        String totalItemPant = item.getPant().setScale(2, RoundingMode.CEILING).multiply(new BigDecimal(noOfItems)).toString();

        String pantColumn = (totalItemPant.equals("0.00")) ? EMPTY_COLUMN : "inkl. pant " + totalItemPant;


        sb.append((noOfItems > 1) ? formatRow(currentItemName, noOfItems + "*" + currentItemPricePlusVat, pantColumn, totalCurrentItemPricePlusVat)
                : formatRow(currentItemName, EMPTY_COLUMN, pantColumn, currentItemPricePlusVat));


        return sb.toString();

    }

    private String createVATRows() {
        StringBuilder sb = new StringBuilder();

        for (Double e : grossVATs.keySet()) {
            if (!grossVATs.get(e).toString().equals("0")) {
                BigDecimal v = VATs.get(e).setScale(2, RoundingMode.CEILING);
                BigDecimal n = netVATs.get(e).setScale(2, RoundingMode.CEILING);
                BigDecimal g = grossVATs.get(e).setScale(2, RoundingMode.CEILING);
                sb.append(formatRow(e * 100 + "0", v.toString(), n.toString(), g.toString()));
            }
        }
        return sb.toString();

    }

    private void calculateVat(Item item, int amount) {
        BigDecimal currentItemPrice = item.getPrice().getAmount().setScale(2, RoundingMode.CEILING);
        BigDecimal currentItemPricePlusVat = item.getPricePlusVat().getAmount().setScale(2, RoundingMode.CEILING);
        BigDecimal currentItemAmountOfVAT = item.getVATAmountOfPrice().getAmount().setScale(2, RoundingMode.CEILING);

        BigDecimal old = grossVATs.get(item.getVat().doubleValue());
        BigDecimal nev = grossVATs.get(item.getVat().doubleValue()).add(currentItemPricePlusVat.multiply(new BigDecimal(amount)));
        grossVATs.replace(item.getVat().doubleValue(), old, nev);

        old = VATs.get(item.getVat().doubleValue());
        nev = VATs.get(item.getVat().doubleValue()).add(currentItemAmountOfVAT.multiply(new BigDecimal(amount)));
        VATs.replace(item.getVat().doubleValue(), old, nev);

        old = netVATs.get(item.getVat().doubleValue());
        nev = netVATs.get(item.getVat().doubleValue()).add(currentItemPrice.multiply(new BigDecimal(amount)));
        netVATs.replace(item.getVat().doubleValue(), old, nev);

    }

    //sets up the vat maps with value zero
    private void setUpVATsMaps(){
        BigDecimal zero = new BigDecimal("0");
        for (int i = 0; i < 3; i++) {
            VATs.put(VAT6, zero);
            grossVATs.put(VAT6, zero);
            netVATs.put(VAT6, zero);

            VATs.put(VAT12, zero);
            grossVATs.put(VAT12, zero);
            netVATs.put(VAT12, zero);

            VATs.put(VAT25, zero);
            grossVATs.put(VAT25, zero);
            netVATs.put(VAT25, zero);
        }
    }

}
