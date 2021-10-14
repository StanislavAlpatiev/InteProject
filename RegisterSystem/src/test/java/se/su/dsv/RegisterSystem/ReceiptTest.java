package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
        DEFAULT_ITEM_3 = new Item("Test2") {
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

    }

    @Test
    void receiptHasCorrectTime() {

    }

    @Test
    void receiptHasCorrectItems() {
    }

    @Test
    void receiptHasCorrectTotalPricePlusVAT() {
    }

    @Test
    void receiptHasCorrectVATRow() {
    }

    @Test
    void receiptDoesNotHaveIrrelevantVATRow() {
    }

    @Test
    void receiptFormatHasCorrectWidth() {
    }




}
