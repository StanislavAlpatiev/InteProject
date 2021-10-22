package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    //TODO clean up and write comments for every method, better names for tests
    // add test checking if order is empty, zero price and no items. used for constructor and after remove
    // test för framtida metod för att skicka in fler av samma items


    static final Currency DEFAULT_CURRENCY = Currency.SEK;
    static final Item DEFAULT_NEWSPAPER = new Item("DN Newspaper", "12345678", "Dn",
            ItemType.NEWSPAPER, new Money(new BigDecimal("199.5"), DEFAULT_CURRENCY));
    static final Item DEFAULT_BEVERAGE = new Item("Coca-cola", "12345678", "Dn",
            new Money(new BigDecimal("17.85"), DEFAULT_CURRENCY), new BigDecimal("5"));
    static final Item DEFAULT_GROCERY = new Item("Watermelon bigpack", "12345678", "Dn",
            ItemType.GROCERY, new Money(new BigDecimal("69.99"), DEFAULT_CURRENCY));
    static final Item DEFAULT_TOBACCO = new Item("Snus", "12345678", "Dn", ItemType.TOBACCO,
            new Money(new BigDecimal("999.75"), DEFAULT_CURRENCY));
    static final Money ZERO = new Money(BigDecimal.ZERO, DEFAULT_CURRENCY);

    Order defaultOrder;




    @BeforeEach
    void setUp(){
        defaultOrder = new Order(DEFAULT_CURRENCY);
    }

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
        String actual = defaultOrder.getNumber().substring(0, 8);
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
                defaultOrder.addItem((Item) null);
            });
        }
    }

    @Test
    void addItemsThrowsExceptionWhenItemHasWrongCurrency() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.addItem(new Item("Item", "12345678", "Producer",
                        ItemType.GROCERY, new Money(new BigDecimal("50"), Currency.USD)));
            });
        }
    }

    @Test
    void addItemToOrderTest() {
        defaultOrder.addItem(DEFAULT_NEWSPAPER);
        orderContainItems(defaultOrder, DEFAULT_NEWSPAPER);
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
        defaultOrder.addItem(DEFAULT_NEWSPAPER, DEFAULT_TOBACCO);
        orderContainItems(defaultOrder, DEFAULT_TOBACCO, DEFAULT_NEWSPAPER);
    }


    @Test
    void addItemsIncreasesTotalOrderPrice() {
        defaultOrder.addItem(DEFAULT_GROCERY, DEFAULT_BEVERAGE);
        Money actual = defaultOrder.getTotalPricePlusVat();
        Money expected = DEFAULT_BEVERAGE.getPricePlusVatAndPant().add(DEFAULT_GROCERY.getPricePlusVatAndPant());
        assertEquals(expected, actual);
    }

    @Test
    void removeThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.removeItem((Item) null);
            });
        }
    }

    @Test
    void removeItemFromOrderTest() {
        defaultOrder.addItem(DEFAULT_TOBACCO);
        defaultOrder.removeItem(DEFAULT_TOBACCO);
        orderDoesNotContainItems(defaultOrder, DEFAULT_TOBACCO);
    }

    @Test
    void removeMultipleItemsFromOrderTest() {
        defaultOrder.addItem(DEFAULT_GROCERY, DEFAULT_TOBACCO);
        defaultOrder.removeItem(DEFAULT_TOBACCO, DEFAULT_GROCERY);
        orderDoesNotContainItems(defaultOrder, DEFAULT_TOBACCO, DEFAULT_GROCERY);
    }

    @Test
    void removeItemsDecreasesAmountOfSameItemInOrder() {
        defaultOrder.addItem(DEFAULT_GROCERY, DEFAULT_GROCERY);
        defaultOrder.removeItem(DEFAULT_GROCERY);
        BigDecimal expected = BigDecimal.ONE;
        BigDecimal actual = defaultOrder.getItems().get(DEFAULT_GROCERY);
        assertEquals(expected, actual);
    }


    @Test
    void successfulRemoveReturnsTrue() {
        defaultOrder.addItem(DEFAULT_GROCERY);
        assertTrue(defaultOrder.removeItem(DEFAULT_GROCERY));
    }

    @Test
    void failedRemoveReturnsFalse() { ;
        assertFalse(defaultOrder.removeItem(DEFAULT_TOBACCO));
    }

    @Test
    void successfulRemoveDecreasesTotalOrderPrice() {
        defaultOrder.addItem(DEFAULT_TOBACCO);
        defaultOrder.removeItem(DEFAULT_TOBACCO);
        Money actual = defaultOrder.getTotalPricePlusVat();
        assertEquals(ZERO, actual);
    }

    @Test
    void successfulRemoveDecreasesTotalOrderPriceMultipleValues() {
        defaultOrder.addItem(DEFAULT_GROCERY, DEFAULT_BEVERAGE, DEFAULT_TOBACCO);
        defaultOrder.removeItem(DEFAULT_TOBACCO, DEFAULT_BEVERAGE);
        Money expected = DEFAULT_GROCERY.getPricePlusVatAndPant();
        Money actual = defaultOrder.getTotalPricePlusVat();
        assertEquals(expected, actual);
    }

    @Test
    void clearRemovesItems() {
        defaultOrder.addItem(DEFAULT_TOBACCO, DEFAULT_GROCERY);
        defaultOrder.clear();
        assertTrue(defaultOrder.getItems().isEmpty());
    }

    @Test
    void clearRemovesTotalPrice() {
        defaultOrder.addItem(DEFAULT_TOBACCO, DEFAULT_GROCERY);
        defaultOrder.clear();
        Money actual = defaultOrder.getTotalPricePlusVat();
        assertEquals(ZERO, actual);
    }

    @Test
    void getAmountOfVatReturnsCorrectValueForEachPresentVatRate() {
        defaultOrder.addItem(DEFAULT_NEWSPAPER, DEFAULT_GROCERY, DEFAULT_TOBACCO);
        for (Item item : defaultOrder.getItems().keySet()) {
            defaultOrder.addItem(item);
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getVATAmountOfPrice().add(item.getVATAmountOfPrice());
            Money actual = defaultOrder.getAmountOfVat(vatRate);
            assertEquals(expected, actual);
        }
    }

    @Test
    void getNetVatReturnsCorrectValueForEachPresentVatRate() {
        defaultOrder.addItem(DEFAULT_NEWSPAPER, DEFAULT_GROCERY, DEFAULT_TOBACCO);
        for (Item item : defaultOrder.getItems().keySet()) {
            defaultOrder.addItem(item);
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPrice().add(item.getPrice());
            Money actual = defaultOrder.getNetVat(vatRate);
            assertEquals(expected, actual);
        }
    }


    @Test
    void getGrossVatReturnsCorrectValueForEachPresentVatRate() {
        defaultOrder.addItem(DEFAULT_NEWSPAPER, DEFAULT_GROCERY, DEFAULT_TOBACCO);
        for (Item item : defaultOrder.getItems().keySet()) {
            defaultOrder.addItem(item);
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPricePlusVatAndPant().add(item.getPricePlusVatAndPant());
            Money actual = defaultOrder.getGrossVat(vatRate);
            assertEquals(expected, actual);
        }
    }


    //kanske utnyttja remove på dessa
    @ParameterizedTest
    @EnumSource(VAT.class)
    void getAmountOfVatReturnsZeroForVatRateNotRepresentedInOrder(VAT vat) {
        assertEquals(ZERO, defaultOrder.getAmountOfVat(vat.label));
    }

    @ParameterizedTest
    @EnumSource(VAT.class)
    void getNetVatReturnsZeroForVatRateNotRepresentedInOrder(VAT vat) {
        assertEquals(ZERO, defaultOrder.getNetVat(vat.label));
    }
    @ParameterizedTest
    @EnumSource(VAT.class)
    void getGrossVatReturnsZeroForVatRateNotRepresentedInOrder(VAT vat) {
        assertEquals(ZERO, defaultOrder.getGrossVat(vat.label));
    }

    @Test
    void getTotalPricePerItemThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.getTotalPricePerItem(null);
            });
        }
    }

    @Test
    void getTotalPricePerItemThrowsExceptionWhenItemNotInOrder() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.getTotalPricePerItem(DEFAULT_TOBACCO);
            });
        }
    }

    @Test
    void getTotalPantPerItemThrowsExceptionWhenItemNotInOrder() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.getTotalPantPerItem(DEFAULT_TOBACCO);
            });
        }
    }

    @Test
    void getTotalPricePerItemReturnsCorrectValue() {
        defaultOrder.addItem(DEFAULT_NEWSPAPER, DEFAULT_GROCERY, DEFAULT_TOBACCO);
        for (Item item : defaultOrder.getItems().keySet()) {
            defaultOrder.addItem(item);
            Money expected = item.getPricePlusVatAndPant().add(item.getPricePlusVatAndPant());
            Money actual = defaultOrder.getTotalPricePerItem(item);
            assertEquals(expected, actual);
        }
    }

    @Test
    void getTotalPantPerItemReturnsCorrectValue() {
        defaultOrder.addItem(DEFAULT_BEVERAGE, DEFAULT_BEVERAGE);
        Money expected = DEFAULT_BEVERAGE.getPant().add(DEFAULT_BEVERAGE.getPant());
        Money actual = defaultOrder.getTotalPantPerItem(DEFAULT_BEVERAGE);
        assertEquals(expected, actual);
    }

    @Test
    void getTotalPantPerItemThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.getTotalPantPerItem(null);
            });
        }
    }

    @Test
    void getAmountOfVatThrowExceptionForInvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultOrder.getAmountOfVat(0);
        });

    }

    @Test
    void getNetVatThrowExceptionForInvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultOrder.getNetVat(0);
        });

    }

    @Test
    void getGrossVatThrowExceptionForInvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultOrder.getNetVat(0);
        });
    }

}
