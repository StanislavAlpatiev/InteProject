package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

//TODO: Add comments to what every method does
public class ReceiptTest {

    static final String EXPECTED_RECEIPT_ONE_ITEM = "===================================================================================\n" +
            "OrderNr:                     19990101XXXX                                          \n" +
            "Datum:                         1999-01-01                 Tid:                00:00\n" +
            "===================================================================================\n" +
            "DN Newspaper                                                                 286.19\n" +
            "===================================================================================\n" +
            "TOTAL                                                                        286.19\n" +
            "Moms %                               Moms                Netto               Brutto\n" +
            "6.00                                16.20               269.99               286.19\n" +
            "===================================================================================";
    static final String EXPECTED_RECEIPT_MULTIPLE_ITEMS = "===================================================================================\n" +
            "OrderNr:                     19990101XXXX                                          \n" +
            "Datum:                         1999-01-01                 Tid:                00:00\n" +
            "===================================================================================\n" +
            "Coca cola                         5*57.38     inkl. pant 10.00               286.90\n" +
            "DN Newspaper                                                                 286.19\n" +
            "Snus                            9*1249.99                                  11249.91\n" +
            "Watermelon bigpack                7*10.98                                     76.86\n" +
            "===================================================================================\n" +
            "TOTAL                                                                      11899.86\n" +
            "Moms %                               Moms                Netto               Brutto\n" +
            "6.00                                16.20               269.99               286.19\n" +
            "12.00                               38.97               324.79               363.76\n" +
            "25.00                             2249.98              8999.93             11249.91\n" +
            "===================================================================================";
    private static final Item DEFAULT_ITEM_1 = new Item("DN Newspaper", "12345678", "Dn", ItemType.NEWSPAPER, new Money(new BigDecimal("269.99"), Currency.SEK));
    private static final Item DEFAULT_ITEM_2 = new Item("Coca cola", "12345678", "Dn", new Money(new BigDecimal("49.45"), Currency.SEK), new BigDecimal("5"));
    private static final Item DEFAULT_ITEM_3 = new Item("Watermelon bigpack", "12345678", "Dn", ItemType.GROCERY, new Money(new BigDecimal("9.7955"), Currency.SEK));
    private static final Item DEFAULT_ITEM_4 = new Item("Snus", "12345678", "Dn", ItemType.TOBACCO, new Money(new BigDecimal("999.99"), Currency.SEK));
    private static final String DEFAULT_ORDER_NUMBER = "19990101XXXX";
    private static final LocalDateTime MOCK_DATE = LocalDateTime.of(1999, 1, 1, 0, 0);

    ReceiptTest.MockOrder mockOrder;

    @BeforeEach
    void setUp(){
         mockOrder = new ReceiptTest.MockOrder(Currency.SEK, DEFAULT_ORDER_NUMBER);
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
                new Receipt(new Order(Currency.SEK));
            });
        }
    }

    @Test
    void constructorThrowsExceptionWhenDateIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                new Receipt(new Order(Currency.SEK, DEFAULT_ITEM_1), null);
            });
        }
    }

    @Test
    void receiptMatchesExpectedReceiptOneItem() {

        mockOrder.addItem(DEFAULT_ITEM_1);

        Receipt receipt = new Receipt(mockOrder, MOCK_DATE);

        assertEquals(EXPECTED_RECEIPT_ONE_ITEM, receipt.getReceipt());

    }

  /*  @Test
    void exceptionIsThrownWhenNumberOfCharactersOnRowIsExceeded() {
        {
            assertThrows(IllegalStateException.class, () -> {
                new Receipt(new Order(LONG_STRING_ITEM), new Date());
            });
        }
    }*/

    @Test
    void receiptMatchesExpectedReceiptWithMultipleItems() {
        mockOrder.addItem(DEFAULT_ITEM_1);

        int noOfSameItemAdded = 5;
        for (int i = 0; i < noOfSameItemAdded; i++) {
            mockOrder.addItem(DEFAULT_ITEM_2);
        }

        noOfSameItemAdded = 7;
        for (int i = 0; i < noOfSameItemAdded; i++) {
            mockOrder.addItem(DEFAULT_ITEM_3);
        }

        noOfSameItemAdded = 9;
        for (int i = 0; i < noOfSameItemAdded; i++) {
            mockOrder.addItem(DEFAULT_ITEM_4);
        }

        Receipt receipt = new Receipt(mockOrder, MOCK_DATE);

        assertEquals(EXPECTED_RECEIPT_MULTIPLE_ITEMS, receipt.getReceipt());

    }

    @Test
    void printToFileTest() {
        Order order = new Order(Currency.SEK, DEFAULT_ITEM_1, DEFAULT_ITEM_2, DEFAULT_ITEM_3);
        Receipt receipt = new Receipt(order);
        String receiptStr = receipt.getReceipt();
        receipt.printToFile();

        assertEquals(receiptStr + "\n", loadTextFile("src\\test\\resources\\" + order.getNumber() + ".txt"));

        File file = new File("src\\test\\resources\\" + order.getNumber() + ".txt");
        assertTrue(file.delete());
    }

    @Test
    void printToFileThrowsExceptionWhenFileAlreadyExists() {

        mockOrder.addItem(DEFAULT_ITEM_1);
        Receipt receipt = new Receipt(mockOrder);
        receipt.printToFile();

        assertThrows(IllegalStateException.class, () -> {
            receipt.printToFile();
        });

        File file = new File("src\\test\\resources\\" + mockOrder.getNumber() + ".txt");
        assertTrue(file.delete());
    }

    // helper method to load textfile
    String loadTextFile(String fileName) {
        StringBuilder readFile = new StringBuilder();
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                readFile.append(line).append("\n");
            }
            in.close();
            reader.close();

        } catch (FileNotFoundException e) {
            System.err.println("Cant open file");
        } catch (IOException e) {
            System.out.println("IO error " + e.getMessage());
        }
        return readFile.toString();
    }

    // to be able to create receipt without random generated order number
    static class MockOrder extends Order {

        String mockNumber;

        public MockOrder(Currency currency, String mockOrderNumber) {
            super(currency);
            mockNumber = mockOrderNumber;
        }

        @Override
        public String getNumber() {
            return mockNumber;
        }
    }

}