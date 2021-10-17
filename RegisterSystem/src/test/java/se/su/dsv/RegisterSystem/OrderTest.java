package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {


    static Order DEFAULT_ORDER;
    static Item DEFAULT_ITEM_1;
    static Item DEFAULT_ITEM_2;

    @BeforeAll
    static void setUp() {
        DEFAULT_ITEM_1 = new Item("Test1", null, null, false, null, null) {
            @Override
            public double getVAT() {
                return 0;
            }

            @Override
            public Money getSalesPrice() {
                return null;
            }
        };
        DEFAULT_ITEM_2 = new Item("Test2", null, null, false, null, null) {
            @Override
            public double getVAT() {
                return 0;
            }

            @Override
            public Money getSalesPrice() {
                return null;
            }
        };
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
    }

    @Test
    void addItemsIncreasesAmountOfSameItem() {
        Order order = new Order(DEFAULT_ITEM_1);
        order.addItem(DEFAULT_ITEM_1);
        assertEquals((int) order.getItems().get(DEFAULT_ITEM_1), 2);
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




}
