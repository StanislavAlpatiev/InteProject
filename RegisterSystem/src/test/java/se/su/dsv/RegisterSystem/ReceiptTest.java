package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptTest {

    static Order DEFAULT_ORDER;
    static Item DEFAULT_ITEM_1;
    static Item DEFAULT_ITEM_2;
    static Item DEFAULT_ITEM_3;
    static Receipt DEFAULT_RECEIPT;

    @BeforeAll
    static void setUp() {
        DEFAULT_ITEM_1 = new Item("Test1") {
            @Override
            public double getVAT() {
                return 0;
            }
        };
        DEFAULT_ITEM_2 = new Item("Test2") {
            @Override
            public double getVAT() {
                return 0;
            }
        };
        DEFAULT_ITEM_3 = new Item("Test3") {
            @Override
            public double getVAT() {
                return 0;
            }
        };
        DEFAULT_ORDER = new Order(DEFAULT_ITEM_1, DEFAULT_ITEM_2, DEFAULT_ITEM_3);
        DEFAULT_RECEIPT = new Receipt(DEFAULT_ORDER);
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
    void receiptHasCorrectDate() {
        /*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        assertTrue(DEFAULT_RECEIPT.getReceipt().contains(dtf.format(now)));*/
    }

    @Test
    void receiptHasCorrectTime() {

    }

    @Test
    void receiptHasCorrectOrderNumber() {
        //assertTrue(DEFAULT_RECEIPT.getReceipt().contains(DEFAULT_ORDER.getNumber()));

    }

    @Test
    void receiptHasCorrectItems() {
       /* assertTrue(DEFAULT_RECEIPT.getReceipt().contains(DEFAULT_ITEM_1.getName()));
        assertTrue(DEFAULT_RECEIPT.getReceipt().contains(DEFAULT_ITEM_2.getName()));
        assertTrue(DEFAULT_RECEIPT.getReceipt().contains(DEFAULT_ITEM_3.getName()));*/
    }

    @Test
    void receiptHasCorrectTotalPricePlusVAT() {
        /*Money m = new Money(0);
        m = m.add(DEFAULT_ITEM_1.getPricePlusVat());
        m = m.add(DEFAULT_ITEM_2.getPricePlusVat());
        m = m.add(DEFAULT_ITEM_3.getPricePlusVat());
        BigDecimal amount = m.getAmount().setScale(2, RoundingMode.CEILING);
        assertTrue(DEFAULT_RECEIPT.getReceipt().contains(amount.toString()));*/
    }

    @ParameterizedTest
    @ValueSource(doubles = {20, 35, 60.3, 100.76}) //just random numbers
    void receiptHasCorrectVAT6Row(double prices) {
       /* Item item = new Item("Item", prices, 0.06);
        Order order = new Order(item);
        Receipt receipt = new Receipt(order);
        String VAT = item.getVATAmountOfPrice().getAmount().setScale(2, RoundingMode.CEILING).toString();
        String net = item.getPrice().getAmount().setScale(2, RoundingMode.CEILING).toString();
        String gross = item.getPricePlusVat().getAmount().setScale(2, RoundingMode.CEILING).toString();
        System.out.println(receipt.getReceipt());
        System.out.println(VAT);
        assertTrue(receipt.getReceipt().contains("6.00"));
        assertTrue(receipt.getReceipt().contains(VAT));
        assertTrue(receipt.getReceipt().contains(net));
        assertTrue(receipt.getReceipt().contains(gross));*/



    }

    @Test
    void receiptVATRowContainsCorrectGrossPrice() {
    }

    @Test
    void receiptVATRowContainsCorrectNetPrice() {
    }

    @Test
    void receiptVATRowContainsCorrectVAT() {
    }

    @Test
    void receiptDoesNotHaveIrrelevantVATRow() {
    }

    @Test
    void receiptFormatHasCorrectWidth() {
    }

}
