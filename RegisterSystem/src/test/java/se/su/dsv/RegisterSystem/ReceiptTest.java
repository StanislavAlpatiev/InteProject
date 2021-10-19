package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;


import static org.junit.jupiter.api.Assertions.*;

public class ReceiptTest {

    // to be able to create receipt without random generated order number
    static class MockOrder extends Order {

        String mockNumber;

        public MockOrder(String mockOrderNumber) {
            mockNumber = mockOrderNumber;
        }

        @Override
        public String getNumber() {
            return mockNumber;
        }
    }

    private static final Item DEFAULT_ITEM_1 = new Item("DN Newspaper", "12345678", "Dn", ItemType.NEWSPAPER, new Money(new BigDecimal("200"), Currency.SEK));
    private static final Item DEFAULT_ITEM_2 = new Item("Coca-cola", "12345678", "Dn", new Money(new BigDecimal("20"), Currency.SEK), new BigDecimal("20"));
    private static final Item DEFAULT_ITEM_3 = new Item("Watermelon bigpack", "12345678", "Dn", ItemType.GROCERY, new Money(new BigDecimal("50"), Currency.SEK));
    private static final Item DEFAULT_ITEM_4 = new Item("Snus", "12345678", "Dn", ItemType.TOBACCO, new Money(new BigDecimal("1000"), Currency.SEK));
    private static final Item LONG_STRING_ITEM = new Item("Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "12345678", "Dn", ItemType.TOBACCO, new Money(new BigDecimal(Integer.MAX_VALUE), Currency.SEK));

    static final String EXPECTED_RECEIPT_ONE_ITEM ="===================================================================================\n" +
            "OrderNr:                     19990101XXXX                                          \n" +
            "Datum:                         1999-01-01                 Tid:                00:00\n" +
            "===================================================================================\n" +
            "DN Newspaper                                                                 212.00\n" +
            "===================================================================================\n" +
            "TOTAL                                                                        212.00\n" +
            "Moms %                               Moms                Netto               Brutto\n" +
            "6.00                                12.00               200.00               212.00\n" +
            "===================================================================================";

    static final String EXPECTED_RECEIPT_MULTIPLE_ITEMS = "===================================================================================\n" +
            "OrderNr:                     19990101XXXX                                          \n" +
            "Datum:                         1999-01-01                 Tid:                00:00\n" +
            "===================================================================================\n" +
            "Coca-cola                         5*24.40     inkl. pant 10.00               122.00\n" +
            "DN Newspaper                                                                 212.00\n" +
            "Snus                            9*1250.00                                  11250.00\n" +
            "Watermelon bigpack                7*56.00                                    392.00\n" +
            "===================================================================================\n" +
            "TOTAL                                                                      11976.00\n" +
            "Moms %                               Moms                Netto               Brutto\n" +
            "6.00                                12.00               200.00               212.00\n" +
            "12.00                               54.00               450.00               514.00\n" +
            "25.00                             2250.00              9000.00             11250.00\n" +
            "===================================================================================";



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
    void constructorThrowsExceptionWhenDateIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                new Receipt(new Order(DEFAULT_ITEM_1), null);
            });
        }
    }

    @Test
    void exceptionIsThrownWhenNumberOfCharactersOnRowIsExceeded() {
        {
            assertThrows(IllegalStateException.class, () -> {
                new Receipt(new Order(LONG_STRING_ITEM), new Date());
            });
        }
    }

    @Test
    void receiptMatchesExpectedReceiptOneItem() {

        ReceiptTest.MockOrder mockOrder = new ReceiptTest.MockOrder("19990101XXXX");

        mockOrder.addItem(DEFAULT_ITEM_1);

        Date mockDate = new Date(99, Calendar.JANUARY, 1, 0, 0);
        Receipt receipt = new Receipt(mockOrder, mockDate);

        assertEquals(EXPECTED_RECEIPT_ONE_ITEM, receipt.getReceipt());

    }

    @Test
    void receiptMatchesExpectedReceiptWithMultipleItems() {

        ReceiptTest.MockOrder mockOrder = new ReceiptTest.MockOrder("19990101XXXX");

        mockOrder.addItem(DEFAULT_ITEM_1);

        for (int i = 0; i < 5; i++) {
            mockOrder.addItem(DEFAULT_ITEM_2);
        }
        for (int i = 0; i < 7; i++) {
            mockOrder.addItem(DEFAULT_ITEM_3);
        }
        for (int i = 0; i < 9; i++) {
            mockOrder.addItem(DEFAULT_ITEM_4);
        }
        Date mockDate = new Date(99, Calendar.JANUARY, 1, 0, 0);
        Receipt receipt = new Receipt(mockOrder, mockDate);

        assertEquals(EXPECTED_RECEIPT_MULTIPLE_ITEMS, receipt.getReceipt());

    }

    @Test
    void printToFileTest() {
        Item item1 = new Item("DN Newspaper", "12345678", "Dn", ItemType.NEWSPAPER, new Money(new BigDecimal("99.99"), Currency.SEK));
        Item item2 = new Item("Coca-cola", "12345678", "Dn", new Money(new BigDecimal("29.99"), Currency.SEK), new BigDecimal("2"));
        Item item3 = new Item("Watermelon bigpack", "12345678", "Dn", ItemType.GROCERY, new Money(new BigDecimal("66.66"), Currency.SEK));
        Order order = new Order(item1, item2, item3);
        Receipt receipt = new Receipt(order);
        String receiptStr = receipt.getReceipt();
        receipt.printToFile();

        assertEquals(receiptStr + "\n", loadTextFile(order.getNumber() + ".txt"));

        File file = new File("C:\\Users\\augus\\IdeaProjects\\InteProject\\RegisterSystem" + order.getNumber() + ".txt");
        System.out.println(file.delete());
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

}