package se.su.dsv.RegisterSystem;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

//TODO: Add comments to what every method does
// fixa matchers
public class ReceiptTest {


    //expected structure of a receipt which test should match
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

    //expected structure of a receipt with multiple items which test should match
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


    private static final Item DEFAULT_NEWSPAPER = new Item("DN Newspaper", "12345678", "Dn", ItemType.NEWSPAPER, new Money(new BigDecimal("269.99"), Currency.SEK));
    private static final Item DEFAULT_BEVERAGE = new Item("Coca cola", "12345678", "Dn", new Money(new BigDecimal("49.45"), Currency.SEK), new BigDecimal("5"));
    private static final Item DEFAULT_GROCERY = new Item("Watermelon bigpack", "12345678", "Dn", ItemType.GROCERY, new Money(new BigDecimal("9.80"), Currency.SEK));
    private static final Item DEFAULT_TOBACCO = new Item("Snus", "12345678", "Dn", ItemType.TOBACCO, new Money(new BigDecimal("999.99"), Currency.SEK));
    private static final Order DEFAULT_ORDER = new Order(Currency.SEK, DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE, DEFAULT_GROCERY, DEFAULT_TOBACCO);
    private static final Item LONG_ITEM = new Item("Snus", "12345678", "Dn", ItemType.TOBACCO, new Money(new BigDecimal("10000000000000000000.00"), Currency.SEK));
    private static final String DEFAULT_ORDER_NUMBER = "19990101XXXX";
    private static final LocalDateTime DEFAULT_DATE = LocalDateTime.of(1999, 1, 1, 0, 0);

    @Mock Order mockOrder;



    /**
     * Sets up a mock order to allow for non random order number
     */
    @BeforeEach
    void setUp() {
        mockOrder = org.mockito.Mockito.spy(new Order(Currency.SEK));
        Mockito.lenient().when(mockOrder.getNumber()).thenReturn(DEFAULT_ORDER_NUMBER);
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
                new Receipt(new Order(Currency.SEK, DEFAULT_NEWSPAPER), null);
            });
        }
    }


    /**
     * - The expected receipt contains one newspaper item with price 269.99.
     * - That should give the gross price of that item 286.19 (269.99 * 1.06 = 286.19 rounded to two decimals)
     * - The total price and the gross price for VAT6 should also be 286.19
     * - The amount of VAT should be 16.20 (269.99 * 0.06 = 16.20 rounded to two decimals),
     * the net VAT should be 269.99 and the gross VAT should be 286.19
     * - Only the VAT6 should be accounted in the receipt
     */
    @Test
    void receiptMatchesExpectedReceiptOneItem() {
        mockOrder.addItem(DEFAULT_NEWSPAPER);
        Receipt receipt = new Receipt(mockOrder, DEFAULT_DATE);
        assertEquals(EXPECTED_RECEIPT_ONE_ITEM, receipt.getReceipt());

    }

    /**
     * - The expected receipt contains one newspaper item with price 269.99,
     * 5 beverage items á 49.45, 9 tobacco items á 999.99 and 7 grocery items á 9.8
     * - The items should be presented in alphabetical order of the names
     * - The the beverage row should account the number of items and the total pant (2 * 5 = 10 in pant)
     * - The gross price for the beverages should be 286.90 (49.45 * 1.12 + 2 = 57.38 rounded to two decimals.
     * 57.38 * 5 = 286.90)
     * - The gross price for the newspaper should be the same as in the other receipt, 286.19.
     * - The gross price for the tobacco item should be 11249.91 (999.99 * 1.25 = 1249.99 rounded to two decimals.
     * 1249.99 * 9 = 11249.91)
     * - The gross price for the grocery item should be 76.86 (9.8 * 1.12 = 10.98 rounded to two decimals.
     * * 10.98 * 7 = 76.86)
     * - The total price for the order should be 11899.86 (286.90 + 286.19 + 11249.91 + 76.86 = 11899.86)
     * - VAT6 row should contain amount of VAT = 16.20 (269.99 * 0.06 = 16.20 rounded to two decimals),
     * the net VAT = 269.99 and the gross VAT = 286.19
     * - VAT12 row should contain the gross VAT = 363.76 (286.90 + 76.86),
     * the net VAT = 324.79 (363.76 - 38.97 = 324.79 rounded to two decimals or
     * 363.76 / 1.12 = 324.79 rounded to two decimals)
     * and the amount of VAT = 38.97 (324.79 * 0.12 = 38.97 rounded to two decimals)
     * - VAT25 row should contain the gross VAT = 11249.91,
     * the net VAT = 8999.93 (11249.91 - 2249.98 = 8999.93 or
     * 11249.91 / 1.25 = 8999.93 rounded to two decimals)
     * and the amount of VAT = 2249.98 (8999.93 * 0.25 = 2249.98 rounded to two decimals)
     */
    @Test
    void receiptMatchesExpectedReceiptWithMultipleItems() {
        mockOrder.addItem(DEFAULT_NEWSPAPER);

        int noOfSameItemAdded = 5;
        for (int i = 0; i < noOfSameItemAdded; i++)
            mockOrder.addItem(DEFAULT_BEVERAGE);

        noOfSameItemAdded = 7;
        for (int i = 0; i < noOfSameItemAdded; i++)
            mockOrder.addItem(DEFAULT_GROCERY);

        noOfSameItemAdded = 9;
        for (int i = 0; i < noOfSameItemAdded; i++)
            mockOrder.addItem(DEFAULT_TOBACCO);


        Receipt receipt = new Receipt(mockOrder, DEFAULT_DATE);
        assertEquals(EXPECTED_RECEIPT_MULTIPLE_ITEMS, receipt.getReceipt());

    }

    /**
     * Test that exception is throw when row length is exceeded
     */
    @Test
    void throwsExceptionWhenRowLengthIsExceeded() {
        mockOrder.addItem(LONG_ITEM);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            new Receipt(mockOrder);
        });

        assertEquals("Out of characters for row", e.getMessage());
    }


    /**
     * Testing the print to file method
     */
    @Test
    void printToFileTest() throws IOException {
        Receipt receipt = new Receipt(DEFAULT_ORDER);
        String pathName = "src\\test\\resources\\" + DEFAULT_ORDER.getNumber() + ".txt";

        receipt.printReceiptToFile(pathName);

        String expected = receipt.getReceipt() + "\n";
        String actual = readTextFile(pathName);

        //if a file with the order number exists and has the same content as the receipt string the test has succeeded
        assertEquals(expected, actual);

        File file = new File(pathName);
        assertTrue(file.delete());
    }


    @Test
    void printToFileThrowsExceptionWhenFileAlreadyExists() throws IOException {

        Receipt receipt = new Receipt(DEFAULT_ORDER);
        String pathName = "src\\test\\resources\\" + DEFAULT_ORDER.getNumber() + ".txt";
        receipt.printReceiptToFile(pathName);

        assertThrows(IllegalStateException.class, () -> {
            receipt.printReceiptToFile(pathName);
        });

        File file = new File(pathName);
        assertTrue(file.delete());
    }

    @Test
    void printToFileThrowsIOException() {

        Receipt receipt = new Receipt(DEFAULT_ORDER);
        String nonExistingFile = "no\\such\\path\\noFile.txt";

        assertThrows(IOException.class, () -> {
            receipt.printReceiptToFile(nonExistingFile);
        });


    }

    // helper method to read text file
    String readTextFile(String pathName) throws FileNotFoundException {
        StringBuilder readFile = new StringBuilder();
        try {
            FileReader reader = new FileReader(pathName);
            BufferedReader in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                readFile.append(line).append("\n");
            }
            in.close();
            reader.close();
        } catch (IOException e) {
            throw new FileNotFoundException("File not found");
        }
        return readFile.toString();
    }

}