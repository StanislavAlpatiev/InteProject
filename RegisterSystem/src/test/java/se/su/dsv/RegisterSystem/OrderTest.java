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

    //TODO clean up and write comments for every method, better names for test
    // test för kontroll av ordernummer!!!


    static final Currency DEFAULT_CURRENCY = Currency.SEK;

    //Item with VAT 6.00
    static final Item DEFAULT_NEWSPAPER = new Item("DN Newspaper", "12345678", "Dn",
            ItemType.NEWSPAPER, new Money(new BigDecimal("199.5"), DEFAULT_CURRENCY));

    //Item with pant (also VAT 12.00)
    static final Item DEFAULT_BEVERAGE = new Item("Coca-cola", "12345678", "Dn",
            new Money(new BigDecimal("17.85"), DEFAULT_CURRENCY), new BigDecimal("5"));

    //Item with VAT 12.00
    static final Item DEFAULT_GROCERY = new Item("Watermelon bigpack", "12345678", "Dn",
            ItemType.GROCERY, new Money(new BigDecimal("69.99"), DEFAULT_CURRENCY));

    //Item with VAT 25.00
    static final Item DEFAULT_TOBACCO = new Item("Snus", "12345678", "Dn", ItemType.TOBACCO,
            new Money(new BigDecimal("999.75"), DEFAULT_CURRENCY));

    static final Money ZERO = new Money(BigDecimal.ZERO, DEFAULT_CURRENCY);

    //Order containing two items each of different VAT rates
    static final Order DIFFERENT_VAT_RATES_ORDER = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER, DEFAULT_NEWSPAPER,
            DEFAULT_GROCERY, DEFAULT_GROCERY, DEFAULT_TOBACCO, DEFAULT_TOBACCO);

    Order defaultOrder;


    @BeforeEach
    void setUpBeforeEach() {
        defaultOrder = new Order(DEFAULT_CURRENCY);
    }

    /**
     * Trying to create an order with null Item parameter should throw an exception
     */
    @Test
    void constructorThrowsExceptionForNullItem() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                new Order(DEFAULT_CURRENCY, (Item) null);
            });
        }
    }

    /**
     * Trying to create an order with null Currency parameter should throw an exception
     */
    @Test
    void constructorThrowsExceptionForNullCurrency() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                new Order(null, DEFAULT_GROCERY);
            });
        }
    }

    /**
     * Tests if the constructor creates an empty order
     */
    @Test
    void constructorCreatesEmptyOrderByDefault() {
        Order order = new Order(DEFAULT_CURRENCY);
        orderIsEmpty(order);
    }


    /**
     * Tests if the constructor with an Item parameter adds the item to the order
     */
    @Test
    void constructorAddsItemToOrder() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER);

        orderContainItems(order, DEFAULT_NEWSPAPER);
    }


    /**
     * Tests if the constructor with multiple Item parameters adds the items to the order
     */
    @Test
    void constructorAddsMultipleItemsToOrder() {
        Order order = new Order(DEFAULT_CURRENCY, DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE);
        orderContainItems(order, DEFAULT_NEWSPAPER, DEFAULT_BEVERAGE);
    }


    /**
     * Tests if the constructor signs a value to the order number field
     */
    @Test
    void constructorSetsOrderNumber() {
        Order order = new Order(DEFAULT_CURRENCY);
        assertNotNull(order.getNumber());
    }

    /**
     * Tests whether the first part of the order number matches the current date
     */
    @Test
    void constructorGeneratesOrderNumberBasedOnCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();

        //expected string as todays date in YYYYMMDD
        String expected = dtf.format(now);

        //the first eight characters in the order number should be todays date in YYYYMMDD format
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


    /**
     * Trying to add a null Item parameter should throw an exception
     */
    @Test
    void addItemsThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.addItem((Item) null);
            });
        }
    }


    /**
     * Trying to add an Item with the wrong currency in its money value should throw an exception
     */
    @Test
    void addItemsThrowsExceptionWhenItemMoneyValueHasWrongCurrency() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.addItem(new Item("Item", "12345678", "Producer",
                        ItemType.GROCERY, new Money(new BigDecimal("50"), Currency.USD)));
            });
        }
    }

    /**
     * Tests if item is correctly added to the order
     */
    @Test
    void addItemToOrderTest() {
        defaultOrder.addItem(DEFAULT_NEWSPAPER);
        orderContainItems(defaultOrder, DEFAULT_NEWSPAPER);
    }


    /**
     * Tests if multiple items is correctly added to the order
     */
    @Test
    void addMultipleItemsToOrderTest() {
        defaultOrder.addItem(DEFAULT_NEWSPAPER, DEFAULT_TOBACCO);
        orderContainItems(defaultOrder, DEFAULT_TOBACCO, DEFAULT_NEWSPAPER);
    }


    /**
     * Tests if the items increments the total price with the correct value when added to order
     */
    @Test
    void addItemsIncreasesTotalOrderPrice() {
        defaultOrder.addItem(DEFAULT_GROCERY, DEFAULT_BEVERAGE);

        //Since only two items are added to the order the total price should be the sum of those items
        Money actual = defaultOrder.getTotalGrossPrice();
        Money expected = DEFAULT_BEVERAGE.getPricePlusVatAndPant().add(DEFAULT_GROCERY.getPricePlusVatAndPant());
        assertEquals(expected, actual);
    }


    /**
     * Trying to remove a null Item parameter should throw an exception
     */
    @Test
    void removeThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.removeItem((Item) null);
            });
        }
    }


    /**
     * Tests if an item is removed correctly
     */
    @Test
    void removeItemFromOrderTest() {
        defaultOrder.addItem(DEFAULT_TOBACCO);
        defaultOrder.removeItem(DEFAULT_TOBACCO);
        orderDoesNotContainItems(defaultOrder, DEFAULT_TOBACCO);
    }


    /**
     * Tests if multiple items are removed correctly
     */
    @Test
    void removeMultipleItemsFromOrderTest() {
        defaultOrder.addItem(DEFAULT_GROCERY, DEFAULT_TOBACCO);
        defaultOrder.removeItem(DEFAULT_TOBACCO, DEFAULT_GROCERY);
        orderDoesNotContainItems(defaultOrder, DEFAULT_TOBACCO, DEFAULT_GROCERY);
    }

    /**
     * Tests if the removal of an item with more than one of it decreases the number of it
     */
    @Test
    void removeItemsDecreasesAmountOfSameItemInOrder() {
        defaultOrder.addItem(DEFAULT_GROCERY, DEFAULT_GROCERY);
        defaultOrder.removeItem(DEFAULT_GROCERY);

        //Since two groceries are added and one of them is removed the amount of it left should be one
        BigDecimal expected = BigDecimal.ONE;
        BigDecimal actual = defaultOrder.getItems().get(DEFAULT_GROCERY);
        assertEquals(expected, actual);
    }


    /**
     * The remove method should remove true when an item is removed
     */
    @Test
    void successfulRemoveReturnsTrue() {
        defaultOrder.addItem(DEFAULT_GROCERY);

        assertTrue(defaultOrder.removeItem(DEFAULT_GROCERY));

        //we make sure the item is not left in the order
        orderDoesNotContainItems(defaultOrder, DEFAULT_GROCERY);
    }

    /**
     * The remove method should remove false when the removal fails due to the item not being in the order
     */
    @Test
    void failedRemoveReturnsFalse() {
        assertFalse(defaultOrder.removeItem(DEFAULT_TOBACCO));
    }

    /**
     * Tests if the total price of the order decrements by the prices of the removed item
     */
    @Test
    void successfulRemoveDecreasesTotalOrderPrice() {
        defaultOrder.addItem(DEFAULT_TOBACCO);
        defaultOrder.removeItem(DEFAULT_TOBACCO);

        //Since one item was added and then removed the total price of the order should be zero
        Money actual = defaultOrder.getTotalGrossPrice();
        assertEquals(ZERO, actual);
    }

    /**
     * Tests if the total price of the order decrements by the prices of the removed items
     */
    @Test
    void successfulRemoveDecreasesTotalOrderPriceMultipleValues() {
        defaultOrder.addItem(DEFAULT_GROCERY, DEFAULT_BEVERAGE, DEFAULT_TOBACCO);
        defaultOrder.removeItem(DEFAULT_TOBACCO, DEFAULT_BEVERAGE);

        //Since one grocery, one beverage and one tobacco was added and
        //the tobacco and beverage was removed the total price should consist only of the price of the grocery
        Money expected = DEFAULT_GROCERY.getPricePlusVatAndPant();
        Money actual = defaultOrder.getTotalGrossPrice();
        assertEquals(expected, actual);
    }


    /**
     * Tests if clear method removes all items in the order
     */
    @Test
    void clearRemovesItems() {
        defaultOrder.addItem(DEFAULT_TOBACCO, DEFAULT_GROCERY);
        defaultOrder.clear();
        orderIsEmpty(defaultOrder);
    }


    /**
     * Tests if the getAmountOfVAT method returns correct values
     */
    @Test
    void getAmountOfVatReturnsCorrectValueForEachPresentVatRate() {

        //Loops through the items added in the order containing items with different VAT rates
        for (Item item : DIFFERENT_VAT_RATES_ORDER.getItems().keySet()) {

            //we call the method for the VAT rate of each item.
            //since there are two of every item with each VAT rate the
            //expected amount of money returned should be the doubled amount of VAT of each item
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getVATAmountOfPrice().add(item.getVATAmountOfPrice());
            Money actual = DIFFERENT_VAT_RATES_ORDER.getAmountOfVat(vatRate);
            assertEquals(expected, actual);
        }
    }


    /**
     * Tests if the getNetVAT method returns correct values
     */
    @Test
    void getNetVatReturnsCorrectValueForEachPresentVatRate() {

        //Loops through the items added in the order containing items with different VAT rates
        for (Item item : DIFFERENT_VAT_RATES_ORDER.getItems().keySet()) {

            //we call the method for the VAT rate of each item.
            //since there are two of every item with each VAT rate the
            //expected amount of money returned should be doubled net price of each item
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPrice().add(item.getPrice());
            Money actual = DIFFERENT_VAT_RATES_ORDER.getNetVat(vatRate);
            assertEquals(expected, actual);
        }
    }


    /**
     * Tests if the getGrossVAT method returns correct values
     */
    @Test
    void getGrossVatReturnsCorrectValueForEachPresentVatRate() {

        //Loops through the items added in the order containing items with different VAT rates
        for (Item item : DIFFERENT_VAT_RATES_ORDER.getItems().keySet()) {

            //we call the method for the VAT rate of each item.
            //since there are two of every item with each VAT rate the
            //expected amount of money returned should be the doubled gross price of each item
            double vatRate = item.getVat().doubleValue();
            Money expected = item.getPricePlusVatAndPant().add(item.getPricePlusVatAndPant());
            Money actual = DIFFERENT_VAT_RATES_ORDER.getGrossVat(vatRate);
            assertEquals(expected, actual);
        }
    }


    /**
     * Tests if the getAmountOfVAT method returns zero value for a vat rate not represented by an item in the order
     */
    @ParameterizedTest
    @EnumSource(VAT.class)
    void getAmountOfVatReturnsZeroForVatRateNotRepresentedInOrder(VAT vat) {

        //checks every VAT rate. Since the order is empty it should return zero for each VAT rate
        assertEquals(ZERO, defaultOrder.getAmountOfVat(vat.label));
    }


    /**
     * Tests if the getNetVAT method returns zero value for a vat rate not represented by an item in the order
     */
    @ParameterizedTest
    @EnumSource(VAT.class)
    void getNetVatReturnsZeroForVatRateNotRepresentedInOrder(VAT vat) {

        //checks every VAT rate. Since the order is empty it should return zero for each VAT rate
        assertEquals(ZERO, defaultOrder.getNetVat(vat.label));
    }


    /**
     * Tests if the getNetVAT method returns zero value for a vat rate not represented by an item in the order
     */
    @ParameterizedTest
    @EnumSource(VAT.class)
    void getGrossVatReturnsZeroForVatRateNotRepresentedInOrder(VAT vat) {

        //checks every VAT rate. Since the order is empty it should return zero for each VAT rate
        assertEquals(ZERO, defaultOrder.getGrossVat(vat.label));
    }


    /**
     * Tests if getAmountOfVAT method throws an exception if the inserted VAT rate is not valid
     */
    @Test
    void getAmountOfVatThrowExceptionForInvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultOrder.getAmountOfVat(0); //could be any unvalid VAT rate
        });

    }

    /**
     * Tests if getNetVAT method throws an exception if the inserted VAT rate is not valid
     */
    @Test
    void getNetVatThrowExceptionForInvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultOrder.getNetVat(0); //could be any unvalid VAT rate
        });

    }

    /**
     * Tests if getGrossVAT method throws an exception if the inserted VAT rate is not valid
     */
    @Test
    void getGrossVatThrowExceptionForInvalidVATRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultOrder.getNetVat(0); //could be any unvalid VAT rate
        });
    }


    /**
     * Tests if the getTotalPricePerItem method throws exception if null parameter is inserted
     */
    @Test
    void getTotalPricePerItemThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.getTotalPricePerItem(null);
            });
        }
    }

    /**
     * Tests if the getTotalPricePerItem method throws exception if the item is not present in the order
     */
    @Test
    void getTotalPricePerItemThrowsExceptionWhenItemNotInOrder() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.getTotalPricePerItem(DEFAULT_TOBACCO);
            });
        }
    }


    /**
     * Tests if the returned price are correct for getTotalPricePerItem method
     */
    @Test
    void getTotalPricePerItemReturnsCorrectValue() {
        //loops through every item in order containing three different items (the VAT rate doesn't matter)
        for (Item item : DIFFERENT_VAT_RATES_ORDER.getItems().keySet()) {

            //since there are two of each item the value returned should be the doubled price of each item
            Money expected = item.getPricePlusVatAndPant().add(item.getPricePlusVatAndPant());
            Money actual = defaultOrder.getTotalPricePerItem(item);
            assertEquals(expected, actual);
        }
    }


    /**
     * Tests if the getTotalPantPerItem method throws exception if null parameter is inserted
     */
    @Test
    void getTotalPantPerItemThrowsExceptionWhenParameterIsNull() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.getTotalPantPerItem(null);
            });
        }
    }

    /**
     * Tests if the getTotalPantPerItem method throws exception if the item is not present in the order
     */
    @Test
    void getTotalPantPerItemThrowsExceptionWhenItemNotInOrder() {
        {
            assertThrows(IllegalArgumentException.class, () -> {
                defaultOrder.getTotalPantPerItem(DEFAULT_TOBACCO);
            });
        }
    }

    /**
     * Tests if the returned price are correct for getTotalPantPerItem method
     */
    @Test
    void getTotalPantPerItemReturnsCorrectValue() {

        //adds two beverage items (which automatically has some sort of pant other than zero)
        defaultOrder.addItem(DEFAULT_BEVERAGE, DEFAULT_BEVERAGE);

        //since there are two of the same items the value returned should be the doubled price of each item
        Money expected = DEFAULT_BEVERAGE.getPant().add(DEFAULT_BEVERAGE.getPant());
        Money actual = defaultOrder.getTotalPantPerItem(DEFAULT_BEVERAGE);
        assertEquals(expected, actual);
    }

    /**
     * Helper method for testing if an item is present in the order
     */
    private void orderContainItems(Order order, Item... items) {
        for (Item item : items) {

            //the order map should contain the item
            assertTrue(order.getItems().containsKey(item));

            //the getTotalPricePerItem-method should not throw an exception if the item is in the order
            assertDoesNotThrow(() -> order.getTotalPricePerItem(item));

            //the getTotalPantPerItem-method should not throw an exception if the item is in the order
            assertDoesNotThrow(() -> order.getTotalPantPerItem(item));
        }
    }

    /**
     * Helper method for testing if an item is not present in the order
     */
    private void orderDoesNotContainItems(Order order, Item... items) {
        for (Item item : items) {

            //the order map should not contain the item
            assertFalse(order.getItems().containsKey(item));

            //the getTotalPricePerItem-method should throw an exception if the item is not in the order
            assertThrows(IllegalArgumentException.class, () -> order.getTotalPricePerItem(item));

            //the getTotalPantPerItem-method should throw an exception if the item is not in the order
            assertThrows(IllegalArgumentException.class, () -> order.getTotalPantPerItem(item));
        }
    }

    /**
     * Helper method to test that the order is empty of items and the total price is zero
     */
    private void orderIsEmpty(Order order) {
        assertTrue(order.getItems().isEmpty());
        assertEquals(ZERO, order.getTotalGrossPrice());
    }

}
