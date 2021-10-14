package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private static final Order DEFAULT_ORDER = new Order();



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
        Item item1 = new Item("1", new Money(new BigDecimal("1"), Currency.SEK)) {
            @Override
            public double getVAT() {
                return 0;
            }
        };
        Item item2 = new Item("2", new Money(new BigDecimal("2"), Currency.SEK)) {
            @Override
            public double getVAT() {
                return 0;
            }
        };
        Item item3 = new Item("2", new Money(new BigDecimal("2"), Currency.SEK)) {
            @Override
            public double getVAT() {
                return 0;
            }
        };
        Order order = new Order(item1, item2);
        assertTrue(order.getItems().containsKey(item1));
        assertTrue(order.getItems().containsKey(item2));
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
            assertThrows(IllegalArgumentException.class, () -> { DEFAULT_ORDER.addItem(null);
            });
        }
    }

    @Test
    void addItemsAddsItemToOrder() {
        Item item = new Item("3", new Money(new BigDecimal("3"), Currency.SEK)) {
            @Override
            public double getVAT() {
                return 0;
            }
        };
        DEFAULT_ORDER.addItem(item);
        assertTrue(DEFAULT_ORDER.getItems().containsKey(item));
    }

    @Test
    void removeItemsRemovesItemFromOrder() {
        Item item = new Item("3", new Money(new BigDecimal("3"), Currency.SEK)) {
            @Override
            public double getVAT() {
                return 0;
            }
        };
        DEFAULT_ORDER.addItem(item);
        assertTrue(DEFAULT_ORDER.getItems().containsKey(item));
    }


}
