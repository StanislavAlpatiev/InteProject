package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    //TODO clean up and write comments for every method, better names for tests


    static final Currency DEFAULT_CURRENCY = Currency.SEK;
    static final Item DEFAULT_NEWSPAPER = new Item("DN Newspaper", "12345678", "Dn",
            ItemType.NEWSPAPER, new Money(new BigDecimal("199.5"), DEFAULT_CURRENCY));
    static final Item DEFAULT_BEVERAGE = new Item("Coca-cola", "12345678", "Dn",
            new Money(new BigDecimal("17.85"), DEFAULT_CURRENCY), new BigDecimal("5"));
    static final Item DEFAULT_GROCERY = new Item("Watermelon bigpack", "12345678", "Dn",
            ItemType.GROCERY, new Money(new BigDecimal("69.99"), DEFAULT_CURRENCY));
    static final Item DEFAULT_TOBACCO = new Item("Snus", "12345678", "Dn", ItemType.TOBACCO,
            new Money(new BigDecimal("999.75"), DEFAULT_CURRENCY));
    static final Order DEFAULT_ORDER = new Order(DEFAULT_CURRENCY);


    @Test
    void constructorThrowsExceptionForNullItem() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                new Order(DEFAULT_CURRENCY, (Item) null);
            });
        }
    }

    @Test
    void constructorThrowsExceptionForNullCurrency() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                new Order(null, DEFAULT_GROCERY);
            });
        }
    }


    @Test
    void constructorAddsItemToOrder() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER);
        assertTrue(order.getItems().containsKey(DEFAULT_NEWSPAPER));
        orderContainItems(order, DEFAULT_NEWSPAPER);
    }

    @Test
    void constructorAddsMultipleItemsToOrder() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE);
        orderContainItems(order, DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE);

    }


    @Test
    void constructorSetsOrderNumber() {
        Order order = new Order(DEFAULT_CURRENCY);
        assertNotNull(order.getNumber());
    }

    @Test
    void constructorGeneratesOrderNumberBasedOnCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        String expected = dtf.format(now);
        String actual = DEFAULT_ORDER.getNumber().substring(0, 8);
        assertEquals(expected, actual);
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
                DEFAULT_ORDER.addItem((Item) null);
            });
        }
    }

    @Test
    void addItemsThrowsExceptionWhenItemHasWrongCurrency() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                DEFAULT_ORDER.addItem(new Item("Item", "12345678", "Producer",
                        ItemType.GROCERY, new Money(new BigDecimal("50"), Currency.USD)));
            });
        }
    }

    @Test
    void addItemToOrderTest() {
        Order order = new Order(DEFAULT_CURRENCY);
        order.addItem(DEFAULT_NEWSPAPER);
        orderContainItems(order, DEFAULT_NEWSPAPER);
    }

    private void orderContainItems(Order order, Item... items) {
        for (Item item : items) {
            assertTrue(order.getItems().containsKey(item));
            assertDoesNotThrow(() -> order.getTotalPricePerItem(item));
            assertDoesNotThrow(() -> order.getTotalPantPerItem(item));
        }
    }

    private void orderDoesNotContainItems(Order order, Item... items) {
        for (Item item : items) {
            assertFalse(order.getItems().containsKey(item));
            assertThrows(IllegalArgumentException.class, () -> order.getTotalPricePerItem(item));
            assertThrows(IllegalArgumentException.class, () -> order.getTotalPantPerItem(item));
        }
    }


    @Test
    void addMultipleItemsToOrderTest() {
        Order order = new Order(DEFAULT_CURRENCY);
        order.addItem(DEFAULT_NEWSPAPER, DEFAULT_TOBACCO);
        orderContainItems(order, DEFAULT_TOBACCO, DEFAULT_NEWSPAPER);
    }


    @Test
    void addItemsIncreasesTotalOrderPrice() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_BEVERAGE);
        order.addItem(DEFAULT_GROCERY);
        Money actual = order.getTotalPricePlusVat();
        Money expected = DEFAULT_BEVERAGE.getPricePlusVatAndPant().add(DEFAULT_GROCERY.getPricePlusVatAndPant());
        assertEquals(expected, actual);
    }

    @Test
    void removeThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                DEFAULT_ORDER.removeItem((Item) null);
            });
        }
    }

    @Test
    void removeItemFromOrderTest() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_TOBACCO);
        order.removeItem(DEFAULT_TOBACCO);
        orderDoesNotContainItems(order, DEFAULT_TOBACCO);
    }

    @Test
    void removeMultipleItemsFromOrderTest() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_TOBACCO, DEFAULT_GROCERY);
        order.removeItem(DEFAULT_TOBACCO, DEFAULT_GROCERY);
        orderDoesNotContainItems(order, DEFAULT_TOBACCO, DEFAULT_GROCERY);
    }

    @Test
    void removeItemsDecreasesAmountOfSameItemInOrder() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_GROCERY, DEFAULT_GROCERY);
        order.removeItem(DEFAULT_GROCERY);
        BigDecimal expected = BigDecimal.ONE;
        BigDecimal actual = order.getItems().get(DEFAULT_GROCERY);
        assertEquals(expected, actual);
    }


    @Test
    void successfulRemoveReturnsTrue() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_GROCERY);
        assertTrue(order.removeItem(DEFAULT_GROCERY));
    }

    @Test
    void failedRemoveReturnsFalse() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_GROCERY);
        assertFalse(order.removeItem(DEFAULT_TOBACCO));
    }

    @Test
    void successfulRemoveDecreasesTotalOrderPrice() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_TOBACCO);
        order.removeItem(DEFAULT_TOBACCO);
        Money expected = new Money(BigDecimal.ZERO, DEFAULT_CURRENCY);
        Money actual = order.getTotalPricePlusVat();
        assertEquals(expected, actual);
    }

    @Test
    void clearRemovesItems() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_TOBACCO, DEFAULT_GROCERY);
        order.clear();
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void clearRemovesTotalPrice() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_TOBACCO);
        order.clear();
        Money expected = new Money(BigDecimal.ZERO, DEFAULT_CURRENCY);
        Money actual = order.getTotalPricePlusVat();
        assertEquals(expected, actual);
    }

    @Test
    void getAmountOfVatReturnsCorrectValueForEachPresentVatRate() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER, DEFAULT_GROCERY, DEFAULT_TOBACCO);
        for (Item item : order.getItems().keySet()) {
            order.addItem(item);
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getVATAmountOfPrice().add(item.getVATAmountOfPrice());
            Money actual = order.getAmountOfVat(vatRate);
            assertEquals(expected, actual);
        }
    }

    @Test
    void getNetVatReturnsCorrectValueForEachPresentVatRate() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER, DEFAULT_GROCERY, DEFAULT_TOBACCO);
        for (Item item : order.getItems().keySet()) {
            order.addItem(item);
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPrice().add(item.getPrice());
            Money actual = order.getNetVat(vatRate);
            assertEquals(expected, actual);
        }
    }


    @Test
    void getGrossVatReturnsCorrectValueForEachPresentVatRate() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER, DEFAULT_GROCERY, DEFAULT_TOBACCO);
        for (Item item : order.getItems().keySet()) {
            order.addItem(item);
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPricePlusVatAndPant().add(item.getPricePlusVatAndPant());
            Money actual = order.getGrossVat(vatRate);
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
    void getTotalPricePerItemThrowsExceptionWhenItemNotInOrder() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                DEFAULT_ORDER.getTotalPricePerItem(DEFAULT_TOBACCO);
            });
        }
    }

    @Test
    void getTotalPantPerItemThrowsExceptionWhenItemNotInOrder() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                DEFAULT_ORDER.getTotalPantPerItem(DEFAULT_TOBACCO);
            });
        }
    }

    @Test
    void getTotalPricePerItemReturnsCorrectValue() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER, DEFAULT_GROCERY, DEFAULT_TOBACCO);
        for (Item item : order.getItems().keySet()) {
            order.addItem(item);
            Money expected = item.getPricePlusVatAndPant().add(item.getPricePlusVatAndPant());
            Money actual = order.getTotalPricePerItem(item);
            assertEquals(expected, actual);
        }
    }

    @Test
    void getTotalPantPerItemReturnsCorrectValue() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_BEVERAGE, DEFAULT_BEVERAGE);
        Money expected = DEFAULT_BEVERAGE.getPant().add(DEFAULT_BEVERAGE.getPant());
        Money actual = order.getTotalPantPerItem(DEFAULT_BEVERAGE);
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
    void getAmountOfVatThrowExceptionForInvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            DEFAULT_ORDER.getAmountOfVat(0);
        });

    }

    @Test
    void getNetVatThrowExceptionForInvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            DEFAULT_ORDER.getNetVat(0);
        });

    }

    @Test
    void getGrossVatThrowExceptionForInvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            DEFAULT_ORDER.getNetVat(0);
        });
    }

}
