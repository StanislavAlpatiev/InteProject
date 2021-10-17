package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.junit.jupiter.api.Assertions.*;

public class ReceiptTest {

    static final BigDecimal ZERO = new BigDecimal("0");
    static final Money DEFAULT_MONEY_ZERO = new Money(ZERO, Currency.SEK);
    static final int POSITION_OF_ORDER_NUMBER = 1;
    static final int POSITION_OF_DATE_AND_TIME_ROW = 2;
    static final int POSITION_OF_FIRST_ITEM = 4;
    static final int POSITION_OF_LAST_ROW = 8;  //if receipt contains only one item
    static final double VAT6 = 0.06;
    static final double VAT12 = 0.12;
    static final double VAT25 = 0.25;

    static Order DEFAULT_ORDER;
    static Item[] DEFAULT_ITEMS;
    static Receipt DEFAULT_RECEIPT_ONE;
    static Receipt DEFAULT_RECEIPT_TWO;
    static Receipt DEFAULT_RECEIPT_THREE;


    @BeforeAll
    static void setUp() {
        Item item1 = new Item("Item1") {
            @Override
            public double getVAT() {
                return 0.06;
            }
        }; //20, 0.06
        Item item2 = new Item("Item2") {
            @Override
            public double getVAT() {
                return 0.12;
            }
        }; //40, 0.12
        Item item3 = new Item("Item3") {
            @Override
            public double getVAT() {
                return 0.25;
            }
        }; //50, 0.25
        Item item4 = new Item("Item4") {
            @Override
            public double getVAT() {
                return 0.06;
            }
        }; //20, 0.06
        Item item5 = new Item("Item5") {
            @Override
            public double getVAT() {
                return 0.12;
            }
        }; //40, 0.12
        Item item6 = new Item("Item6") {
            @Override
            public double getVAT() {
                return 0.25;
            }
        }; //50, 0.25
        DEFAULT_ITEMS = new Item[]{item1, item2, item3, item4, item5, item6};
        DEFAULT_ORDER = new Order(item1, item2, item3);
        DEFAULT_RECEIPT_ONE = new Receipt(DEFAULT_ORDER);
        DEFAULT_RECEIPT_TWO = new Receipt(new Order(item1, item2, item3, item4, item5, item6));
        DEFAULT_RECEIPT_THREE = new Receipt(new Order(item1));
    }

    @Test
    void constructorThrowsExceptionForNullArgument() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                new Receipt(null);
            });
        }
    }

    @Test
    void constructorThrowsExceptionWhenOrderIsEmpty() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                new Receipt(new Order());
            });
        }
    }

    @Test
    void receiptHasCorrectOrderNumber() {
        assertTrue(DEFAULT_RECEIPT_ONE.getRow(POSITION_OF_ORDER_NUMBER).contains(DEFAULT_ORDER.getNumber()));
    }


    @Test
    void receiptHasCorrectDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        assertTrue(DEFAULT_RECEIPT_ONE.getRow(POSITION_OF_DATE_AND_TIME_ROW).contains(dtf.format(now)));
    }

    @Test
    void receiptHasCorrectTime() {

    }

    @Test
    void getRowMethodThrowsExceptionForNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> {DEFAULT_RECEIPT_ONE.getRow(-1);});
    }

    @Test
    void getRowMethodThrowsExceptionWhenRowNumberExceeded() {
        assertThrows(IllegalArgumentException.class, () -> {DEFAULT_RECEIPT_THREE.getRow(POSITION_OF_LAST_ROW + 1);});
    }

    @Test
        //checks if receipt puts new item on expected row for different number of unique items added
    void receiptHasCorrectItemsOnCorrectRow() {
        boolean itemOnCorrectRow = true;
        for (int i = 0; i < 6; i++) {
            if (!(DEFAULT_RECEIPT_TWO.getRow(POSITION_OF_FIRST_ITEM + i).contains("Item" + (i + 1))))
                itemOnCorrectRow = false;
        }
        assertTrue(itemOnCorrectRow);
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 50, 99}) //could be any numbers > 1
        //checks if n of same items creates one row of that item with price * n
    void receiptPutsMultipleItemsOnSameRow(int noOfItems) {
        Item item = new Item("Item", null, null, false, null, null) {
            @Override
            public double getVAT() {
                return 0.25;
            }
            @Override
            public Money getSalesPrice() {
                return null;
            }
        };
        Order order = new Order();
        BigDecimal expectedSum = ZERO;
        for (int i = 0; i < noOfItems; i++){
            order.addItem(item);
            expectedSum = expectedSum.add(item.getPricePlusVat().getAmount());
        }
        Receipt receipt = new Receipt(order);
        assertTrue(receipt.getRow(POSITION_OF_FIRST_ITEM).contains(noOfItems + "*" + "62.50"));
        assertTrue(receipt.getRow(POSITION_OF_FIRST_ITEM).contains(expectedSum.setScale(2, RoundingMode.CEILING).toString()));

    }

    @Test
        // adds twice of every default item to check if receipt calculates and adds correct total price
        //TODO: can be done better
    void receiptHasCorrectTotalPrice() {
        Order order = new Order();
        BigDecimal expectedSum = ZERO;
        for (Item item : DEFAULT_ITEMS) {
            order.addItem(item);
            order.addItem(item);
            expectedSum = expectedSum.add(item.getPricePlusVat().getAmount());
            expectedSum = expectedSum.add(item.getPricePlusVat().getAmount());
        }
        Receipt receipt = new Receipt(order);
        assertTrue(receipt.getRow(DEFAULT_ITEMS.length + 5).contains(expectedSum.setScale(2, RoundingMode.CEILING).toString()));
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 3, 50, 99}) //could be any numbers > 0
    void receiptHasCorrectVAT6Row(int noOfItems) {
        receiptHasCorrectVATRow(noOfItems, new Item("Item", null, null, false, null, null) {
            @Override
            public double getVAT() {
                return 0.06;
            }
            @Override
            public Money getSalesPrice() {
                return null;
            }
        }, "6.00");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 50, 99}) //could be any numbers > 0
    void receiptHasCorrectVAT12Row(int noOfItems) {
        receiptHasCorrectVATRow(noOfItems, new Item("Item", null, null, false, null, null) {
            @Override
            public double getVAT() {
                return 0.12;
            }
            @Override
            public Money getSalesPrice() {
                return null;
            }
        }, "12.00");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 50, 99}) //could be any numbers > 0
    void receiptHasCorrectVAT25Row(int noOfItems) {
        receiptHasCorrectVATRow(noOfItems, new Item("Item", null, null, false, null, null) {
            @Override
            public double getVAT() {
                return 0.25;
            }
            @Override
            public Money getSalesPrice() {
                return null;
            }
        }, "25.00");
    }

    @ParameterizedTest
    @ValueSource(doubles = {VAT6, VAT12, VAT25})
        //Receipt with only one item is expected to contain only one type of vat and have exactly 9 rows
    void receiptDoesNotHaveIrrelevantVATRows(double vats) {
        Order order = new Order();
        order.addItem(new Item("Item", null, null, false, null, null) {
            @Override
            public double getVAT() {
                return vats;
            }
            @Override
            public Money getSalesPrice() {
                return null;
            }
        });
        Receipt receipt = new Receipt(order);
        assertTrue(receipt.getRow(POSITION_OF_LAST_ROW).contains(vats * 100 + "0")); 
        assertThrows(IllegalArgumentException.class, () -> {receipt.getRow(POSITION_OF_LAST_ROW + 1);});

    }


    /* helper method to make it possible to check each possible vat row (6.00, 12.00, 25.00). Allows for different number of
     the same vat-Item so we can see if the VAT-calculations on the receipt are correct*/
    void receiptHasCorrectVATRow(int noOfSameItems, Item item, String vat) {
        Order order = new Order();
        Money expectedVAT = DEFAULT_MONEY_ZERO;
        Money expectedNet = DEFAULT_MONEY_ZERO;
        Money expectedGross = DEFAULT_MONEY_ZERO;
        for (int i = 0; i < noOfSameItems; i++) {
            order.addItem(item);
            expectedVAT = expectedVAT.add(item.getVATAmountOfPrice());
            expectedNet = expectedNet.add(item.getPrice());
            expectedGross = expectedGross.add(item.getPricePlusVat());
        }
        Receipt receipt = new Receipt(order);

        assertTrue(receipt.getRow(POSITION_OF_LAST_ROW).contains(vat));
        assertTrue(receipt.getRow(POSITION_OF_LAST_ROW).contains(expectedVAT.getAmount().setScale(2, RoundingMode.CEILING).toString()));
        assertTrue(receipt.getRow(POSITION_OF_LAST_ROW).contains(expectedNet.getAmount().setScale(2, RoundingMode.CEILING).toString()));
        assertTrue(receipt.getRow(POSITION_OF_LAST_ROW).contains(expectedGross.getAmount().setScale(2, RoundingMode.CEILING).toString()));
    }

}