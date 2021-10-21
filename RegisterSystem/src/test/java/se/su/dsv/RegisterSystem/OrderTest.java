package se.su.dsv.RegisterSystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    //TODO clean up and write comments, better names for tests


    static Currency DEFAULT_CURRENCY;
    static Item DEFAULT_NEWSPAPER;
    static Item DEFAULT_BEVERAGE;
    static Item DEFAULT_GROCERY;
    static Item DEFAULT_TOBACCO;
    static Order DEFAULT_ORDER;

    @BeforeAll
    static void setUp() {
        DEFAULT_CURRENCY = Currency.SEK;
        DEFAULT_NEWSPAPER = new Item("DN Newspaper", "12345678", "Dn",
                ItemType.NEWSPAPER, new Money(new BigDecimal("200"), DEFAULT_CURRENCY));
        DEFAULT_BEVERAGE = new Item("Coca-cola", "12345678", "Dn",
                new Money(new BigDecimal("20"), DEFAULT_CURRENCY), new BigDecimal("20"));
        DEFAULT_GROCERY = new Item("Watermelon bigpack", "12345678", "Dn",
                ItemType.GROCERY, new Money(new BigDecimal("50"), DEFAULT_CURRENCY));
        DEFAULT_TOBACCO = new Item("Snus", "12345678", "Dn", ItemType.TOBACCO,
                new Money(new BigDecimal("1000"), DEFAULT_CURRENCY));
        DEFAULT_ORDER = new Order(DEFAULT_CURRENCY);
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
    void constructorAddsItemsToOrder() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER, DEFAULT_GROCERY);
        assertTrue(order.getItems().containsKey(DEFAULT_NEWSPAPER));
        assertTrue(order.getItems().containsKey(DEFAULT_GROCERY));
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
                DEFAULT_ORDER.addItem((Item)null);
            });
        }
    }

    @Test
    void addItemsAddsItemToOrder() {
        DEFAULT_ORDER.addItem(DEFAULT_NEWSPAPER);
        assertTrue(DEFAULT_ORDER.getItems().containsKey(DEFAULT_NEWSPAPER));
        assertDoesNotThrow(() -> DEFAULT_ORDER.getTotalPricePerItem(DEFAULT_NEWSPAPER));
        assertDoesNotThrow(() -> DEFAULT_ORDER.getTotalPantPerItem(DEFAULT_NEWSPAPER));
    }

    @Test
    void addItemsIncreasesTotalOrderPriceForEmptyOrder() {
        Order order = new Order(DEFAULT_CURRENCY);
        order.addItem(DEFAULT_NEWSPAPER);
        Money expected = DEFAULT_NEWSPAPER.getPricePlusVat();
        Money actual = order.getTotalPricePlusVat();
        assertEquals(expected, actual);
    }


    @Test
    void addItemsIncreasesTotalOrderPriceForOrderContainingItems() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_BEVERAGE);
        order.addItem(DEFAULT_GROCERY);
        Money expected = DEFAULT_BEVERAGE.getPricePlusVat().add(DEFAULT_GROCERY.getPricePlusVat());
        Money actual = order.getTotalPricePlusVat();
        assertEquals(expected, actual);
    }

    @Test
    void addItemsUpdatesVATMap() {
        Order order = addItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getVATAmountOfPrice();
            Money actual = order.getAmountOfVat(vatRate);
            assertEquals(expected, actual);
        }
    }

    @Test
    void addItemsUpdatesVATMapContainingItems() {
        Order order = addItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            order.addItem(item);
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getVATAmountOfPrice().add(item.getVATAmountOfPrice());
            Money actual = order.getAmountOfVat(vatRate);
            assertEquals(expected, actual);
        }
    }

    @Test
    void addItemsUpdatesNetVATMap() {
        Order order = addItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPrice();
            Money actual = order.getNetVat(vatRate);
            assertEquals(expected, actual);
        }
    }

    @Test
    void addItemsUpdatesNetMapContainingItems() {
        Order order = addItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            order.addItem(item);
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPrice().add(item.getPrice());
            Money actual = order.getNetVat(vatRate);
            assertEquals(expected, actual);
        }
    }


    @Test
    void addItemsUpdatesGrossVATMap() {
        Order order = addItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPricePlusVat();
            Money actual = order.getGrossVat(vatRate);
            assertEquals(expected, actual);
        }
    }

    @Test
    void addItemsUpdatesGrossMapContainingItems() {
        Order order = addItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            order.addItem(item);
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPricePlusVat().add(item.getPricePlusVat());
            Money actual = order.getGrossVat(vatRate);
            assertEquals(expected, actual);
        }
    }


    @Test
    void removeThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                DEFAULT_ORDER.removeItem((Item)null);
            });
        }
    }

    @Test
    void removeItemsRemovesItemFromOrder() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_TOBACCO);
        order.removeItem(DEFAULT_TOBACCO);
        assertFalse(order.getItems().containsKey(DEFAULT_TOBACCO));
        assertThrows(IllegalArgumentException.class, () -> order.getTotalPricePerItem(DEFAULT_TOBACCO));
        assertThrows(IllegalArgumentException.class, () -> order.getTotalPantPerItem(DEFAULT_TOBACCO));
    }

    @Test
    void removeItemsDecreasesAmountOfSameItem() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_GROCERY, DEFAULT_GROCERY);
        order.removeItem(DEFAULT_GROCERY);
        assertEquals(order.getItems().get(DEFAULT_GROCERY), new BigDecimal("1"));
    }

    @Test
    void removeItemsReturnsTrueWhenRemoveSucceed() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_GROCERY);
        assertTrue(order.removeItem(DEFAULT_GROCERY));
    }

    @Test
    void removeItemsReturnsFalseWhenRemoveFailed() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_GROCERY);
        assertFalse(order.removeItem(DEFAULT_TOBACCO));
    }

    @Test
    void removeItemsDecreasesTotalOrderPriceForOrderWithOneItem() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_TOBACCO);
        order.removeItem(DEFAULT_TOBACCO);
        double expected = 0;
        double actual = order.getTotalPricePlusVat().getAmount().doubleValue();
        assertEquals(expected, actual);
    }

    @Test
    void removeItemsDecreasesTotalOrderPriceForOrderWithMultipleItems() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE);
        order.removeItem(DEFAULT_NEWSPAPER);
        Money expected = DEFAULT_BEVERAGE.getPricePlusVat();
        Money actual = order.getTotalPricePlusVat();
        assertEquals(expected, actual);
    }

    @Test
    void totalOrderPriceStaysUnchangedWhenTryToRemoveNonExistingItem() {
        Order order = new Order(DEFAULT_CURRENCY);
        order.removeItem(DEFAULT_NEWSPAPER);
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
    void removeItemsUpdatesAmountOfVATMapContainingItems() {
        Order order = addItemsWithDifferentVats();
        order.addItem(DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE, DEFAULT_TOBACCO);
        order.removeItem(DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE, DEFAULT_TOBACCO);
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getVATAmountOfPrice();
            Money actual = order.getAmountOfVat(vatRate);
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
    void removeItemsUpdatesNetVATMapContainingItems() {
        Order order = addItemsWithDifferentVats();
        order.addItem(DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE, DEFAULT_TOBACCO);
        order.removeItem(DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE, DEFAULT_TOBACCO);
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPrice();
            Money actual = order.getNetVat(vatRate);
            assertEquals(expected, actual);
        }
    }

    @Test
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
    void removeItemsUpdatesGrossVATMapContainingItems() {
        Order order = addItemsWithDifferentVats();
        order.addItem(DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE, DEFAULT_TOBACCO);
        order.removeItem(DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE, DEFAULT_TOBACCO);
        for (Item item : order.getItems().keySet()) {
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPricePlusVat();
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
        Order order = addItemsWithDifferentVats();
        for (Item item : order.getItems().keySet()) {
            Money expected = item.getPant();
            Money actual = order.getTotalPantPerItem(item);
            assertEquals(expected, actual);
        }
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
        Order order = new Order(DEFAULT_CURRENCY);
        order.addItem(DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE, DEFAULT_TOBACCO);
        return order;
    }

    Order removeItemsWithDifferentVats() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE, DEFAULT_TOBACCO);
        order.removeItem(DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE, DEFAULT_TOBACCO);
        return order;
    }

}

