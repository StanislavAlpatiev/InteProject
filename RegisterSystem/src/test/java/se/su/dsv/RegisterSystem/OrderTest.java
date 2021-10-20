package se.su.dsv.RegisterSystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    //TODO clean up and write comments, better names for tests


    static Item DEFAULT_ITEM_1;
    static Item DEFAULT_ITEM_2;
    static Item DEFAULT_ITEM_3;
    static Item DEFAULT_ITEM_4;
    static Order DEFAULT_ORDER;

    @BeforeAll
    static void setUp() {
        DEFAULT_ITEM_1 = new Item("DN Newspaper", "12345678", "Dn", ItemType.NEWSPAPER, new Money(new BigDecimal("200"), Currency.SEK));
        DEFAULT_ITEM_2 = new Item("Coca-cola", "12345678", "Dn", new Money(new BigDecimal("20"), Currency.SEK), new BigDecimal("20"));
        DEFAULT_ITEM_3 = new Item("Watermelon bigpack", "12345678", "Dn", ItemType.GROCERY, new Money(new BigDecimal("50"), Currency.SEK));
        DEFAULT_ITEM_4 = new Item("Snus", "12345678", "Dn", ItemType.TOBACCO, new Money(new BigDecimal("1000"), Currency.SEK));
        DEFAULT_ORDER = new Order();
    }


    @Test
    void constructorThrowsExceptionForNullArgument() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                new Order((Item) null);
            });
        }
    }

    @Test
    void constructorAddsItemsToOrder() {
        Order order = new Order(DEFAULT_ITEM_1, DEFAULT_ITEM_2);
        assertTrue(order.getItems().containsKey(DEFAULT_ITEM_1));
        assertTrue(order.getItems().containsKey(DEFAULT_ITEM_2));
    }


    @Test
    void constructorSetsOrderNumber() {
        Order order = new Order();
        assertNotNull(order.getNumber());
    }

    @Test
    void constructorGeneratesOrderNumberBasedOnCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order();
        assertEquals(order.getNumber().substring(0, 8), dtf.format(now));
    }


    //dont know how to implement yet
    @Test
    void constructorGeneratesOrderNumberRandomly() {
        /*List<String> codes = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Order order = new Order();
            codes.add(order.getNumber().substring(8));
        }
        Map<Object, Long> counts = codes.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));*/
    }

    @Test
    void addItemsThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                DEFAULT_ORDER.addItem(null);
            });
        }
    }

    @Test
    void addItemsAddsItemToOrder() {
        DEFAULT_ORDER.addItem(DEFAULT_ITEM_1);
        assertTrue(DEFAULT_ORDER.getItems().containsKey(DEFAULT_ITEM_1));
        assertDoesNotThrow(() -> DEFAULT_ORDER.getTotalPricePerItem(DEFAULT_ITEM_1));
        assertDoesNotThrow(() -> DEFAULT_ORDER.getTotalPantPerItem(DEFAULT_ITEM_1));
    }

    @Test
    void addItemsIncreasesTotalOrderPriceForEmptyOrder() {
        Order order = new Order();
        order.addItem(DEFAULT_ITEM_1);
        Money expected = DEFAULT_ITEM_1.getPricePlusVat();
        Money actual = order.getTotalPricePlusVat();
        assertEquals(expected, actual);
    }


    @Test
    void addItemsIncreasesTotalOrderPriceForOrderContainingItems() {
        Order order = new Order(DEFAULT_ITEM_1);
        order.addItem(DEFAULT_ITEM_2);
        Money expected = DEFAULT_ITEM_1.getPricePlusVat().add(DEFAULT_ITEM_2.getPricePlusVat());
        Money actual = order.getTotalPricePlusVat();
        assertEquals(expected, actual);
    }

    @Test
    void addItemsUpdatesVATMap() {
        Order order = addItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            Money expectedAmountOfVAT = item.getVATAmountOfPrice();
            assertEquals(expectedAmountOfVAT, order.getAmountOfVat(vatRate));
        }
    }

    @Test
    void addItemsUpdatesNetVATMap() {
        Order order = addItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            Money expectedNetVAT = item.getPrice();
            assertEquals(expectedNetVAT, order.getNetVat(vatRate));
        }
    }

    @Test
    void addItemsUpdatesGrossVATMap() {
        Order order = addItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            Money expectedGrossVAT = item.getPricePlusVat();
            assertEquals(expectedGrossVAT, order.getGrossVat(vatRate));
        }
    }


    @Test
    void removeThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                DEFAULT_ORDER.removeItem(null);
            });
        }
    }

    @Test
    void removeItemsRemovesItemFromOrder() {
        Order order = new Order(DEFAULT_ITEM_1);
        order.removeItem(DEFAULT_ITEM_1);
        assertFalse(order.getItems().containsKey(DEFAULT_ITEM_1));
        assertThrows(IllegalArgumentException.class, () -> order.getTotalPricePerItem(DEFAULT_ITEM_1));
        assertThrows(IllegalArgumentException.class, () -> order.getTotalPantPerItem(DEFAULT_ITEM_1));
    }

    @Test
    void removeItemsDecreasesAmountOfSameItem() {
        Order order = new Order(DEFAULT_ITEM_1, DEFAULT_ITEM_1);
        order.removeItem(DEFAULT_ITEM_1);
        assertEquals(order.getItems().get(DEFAULT_ITEM_1), new BigDecimal("1"));
    }

    @Test
    void removeItemsReturnsTrueWhenRemoveSucceed() {
        Order order = new Order(DEFAULT_ITEM_1);
        assertTrue(order.removeItem(DEFAULT_ITEM_1));
    }

    @Test
    void removeItemsReturnsFalseWhenRemoveFailed() {
        Order order = new Order(DEFAULT_ITEM_1);
        assertFalse(order.removeItem(DEFAULT_ITEM_2));
    }

    @Test
    void removeItemsDecreasesTotalOrderPriceForOrderWithOneItem() {
        Order order = new Order(DEFAULT_ITEM_1);
        order.removeItem(DEFAULT_ITEM_1);
        double expected = 0;
        double actual = order.getTotalPricePlusVat().getAmount().doubleValue();
        assertEquals(expected, actual);
    }

    @Test
    void removeItemsDecreasesTotalOrderPriceForOrderWithMultipleItems() {
        Order order = new Order(DEFAULT_ITEM_1, DEFAULT_ITEM_2);
        order.removeItem(DEFAULT_ITEM_1);
        Money expected = DEFAULT_ITEM_2.getPricePlusVat();
        Money actual = order.getTotalPricePlusVat();
        assertEquals(expected, actual);
    }

    @Test
    void totalOrderPriceStaysUnchangedWhenTryToRemoveItemNotInOrder() {
        Order order = new Order();
        order.removeItem(DEFAULT_ITEM_2);
        double expected = 0;
        double actual = order.getTotalPricePlusVat().getAmount().doubleValue();
        assertEquals(expected, actual);
    }

    @Test
    void removeItemsUpdatesAmountOfVATMap() {
        Order order = removeItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            double expected = 0;
            double actual = order.getAmountOfVat(vatRate).getAmount().doubleValue();
            assertEquals(expected, actual);
        }
    }

    @Test
        //split up, change name
    void removeItemsUpdatesNetVATMap() {
        Order order = removeItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            double expected = 0;
            double actual = order.getNetVat(vatRate).getAmount().doubleValue();
            assertEquals(expected, actual);
        }
    }

    @Test
        //split up, change name
    void removeItemsUpdatesGrossVATMap() {
        Order order = removeItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            double expected = 0;
            double actual = order.getGrossVat(vatRate).getAmount().doubleValue();
            assertEquals(expected, actual);
        }
    }

    @Test
    void getTotalPricePerItemThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                DEFAULT_ORDER.getTotalPricePerItem(null);
            });
        }
    }

    @Test
    void getTotalPricePerItemReturnsCorrectValue() {
        Order order = addItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            Money expected = item.getPricePlusVat();
            Money actual = order.getTotalPricePerItem(item);
            assertEquals(expected, actual);
        }
    }

    @Test
    void getTotalPantPerItemReturnsCorrectValue() {
        Order order = new Order(DEFAULT_ITEM_2);
        Money expected = DEFAULT_ITEM_2.getPant();
        Money actual = order.getTotalPantPerItem(DEFAULT_ITEM_2);
        assertEquals(expected, actual);

    }

    @Test
    void getTotalPantPerItemThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                DEFAULT_ORDER.getTotalPantPerItem(null);
            });
        }
    }

    @Test
    void getAmountOfVatThrowExeptionForUnvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            DEFAULT_ORDER.getAmountOfVat(-1);
        });

    }

    @Test
    void getNetVatThrowExeptionForUnvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            DEFAULT_ORDER.getNetVat(-1);
        });

    }

    @Test
    void getGrossVatThrowExeptionForUnvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            DEFAULT_ORDER.getNetVat(-1);
        });
    }

    Order addItemsWithDifferentVats() {
        Order order = new Order();
        order.addItem(DEFAULT_ITEM_1);
        order.addItem(DEFAULT_ITEM_2);
        order.addItem(DEFAULT_ITEM_4);
        return order;
    }

    Order removeItemsWithDifferentVats() {
        Order order = new Order(DEFAULT_ITEM_1, DEFAULT_ITEM_2, DEFAULT_ITEM_4);
        order.removeItem(DEFAULT_ITEM_1);
        order.removeItem(DEFAULT_ITEM_2);
        order.removeItem(DEFAULT_ITEM_4);
        return order;
    }

}

