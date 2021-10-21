package se.su.dsv.RegisterSystem;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegisterTest {

    final Currency DEFAULT_CURRENCY = Currency.USD;
    final Currency OTHER_CURRENCY = Currency.SEK;
    final Money DEFAULT_MONEY = new Money(new BigDecimal(1000), DEFAULT_CURRENCY);
    final Money OTHER_MONEY = new Money(new BigDecimal(1000), OTHER_CURRENCY);

    final Item DEFAULT_ITEM = new Item("Mjölk", "0123456789", "Arla", ItemType.GROCERY, DEFAULT_MONEY);
    // DEFAULT_ITEM2 costs twice of DEFAULT_ITEM
    final Item DEFAULT_ITEM2 = new Item("Tryffel", "9876543210", "FancyProducts", ItemType.GROCERY,
            DEFAULT_MONEY.add(DEFAULT_MONEY));
    final Item[] ITEMS = {DEFAULT_ITEM, DEFAULT_ITEM2};

    final Order DEFAULT_ORDER = new Order(ITEMS);
    Register defaultRegister;
    Wallet defaultWallet;

    @BeforeEach
    void initialize() {
        defaultRegister = new Register(DEFAULT_CURRENCY);
        defaultWallet = new Wallet(
                new Customer("Mr Customer", "Street Road", LocalDate.now(), "07070707", "customer@test.com"),
                DEFAULT_MONEY);
        defaultRegister.getInventory().add(DEFAULT_ITEM);
        defaultRegister.getInventory().add(DEFAULT_ITEM);
        defaultRegister.getInventory().add(DEFAULT_ITEM2);

        // Makes sure there is as much money in money as there is in the order
        defaultWallet.add(DEFAULT_ORDER.getTotalPricePlusVat());
    }

    // Tests whether constructor constructs as it is supposed to with valid params.
    @Test
    void constructorSetsCurrencyAndInventoryTest() {
        //makes sure the currency is set
        assertEquals(DEFAULT_CURRENCY, defaultRegister.getCurrency());
        //makes sure an inventory is created in constructor
        assertTrue(defaultRegister.getInventory() != null);
        //makes sure the currency is carried down into the inventory.
        assertEquals(DEFAULT_CURRENCY, defaultRegister.getInventory().getCurrency());

    }

    // Tries to construct an object with null currency
    @Test
    void constructorThrowsIfNullCurrency() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Register(null);
        });
    }

    // Changes currency of the register to SEK from USD, and sees if this change is
    // visible in both Register and its Inventory
    @Test
    void setCurrencyChangesCurrencyInRegisterAndInventoryTest() throws IllegalArgumentException, IOException {
        defaultRegister.setCurrency(OTHER_CURRENCY);

        Currency inventoryCurrency = defaultRegister.getInventory().getCurrency();
        assertEquals(OTHER_CURRENCY, defaultRegister.getCurrency());
        assertEquals(defaultRegister.getCurrency(), inventoryCurrency);
    }

    // Tries to change to null currency.
    @Test
    void setCurrencyWithNullParamThrowsTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultRegister.setCurrency(null);
        });
    }

    @Test
    void itemsInCheckoutNotAvailableInInventoryThrowsTest() {
        // Since there is only one DEFAULT_ITEM2, it is removed - since the order has 2
        // DEFAULT_ITEMs and 1 DEFAULT_ITEM2,
        // it should throw.
        defaultRegister.getInventory().remove(DEFAULT_ITEM2);

        assertThrows(IllegalArgumentException.class, () -> {
            defaultRegister.checkOut(DEFAULT_ORDER, defaultWallet);
        });
    }

    @Test
    void itemsRemovedFromInventoryWhenBoughtInRegisterTest() throws FileNotFoundException {
        defaultRegister.checkOut(DEFAULT_ORDER, defaultWallet);

        Inventory inventory = defaultRegister.getInventory();

        // There shuold still be one DEFAULT_ITEM since only one of two were bought.
        assertTrue(inventory.isAvailable(DEFAULT_ITEM));
        // There shouldn't be any DEFAULT_ITEM2, since they were bought.
        assertFalse(inventory.isAvailable(DEFAULT_ITEM2));
        // Since there used to be two DEFAULT_ITEMs, there should now be only one in
        // inventory:
        assertEquals(1, inventory.getItems().get(DEFAULT_ITEM));
    }

    @Test
    void walletNotEnoughMoneyThrowsTest() {
        // There is as much money in the wallet as there is in the order
        // - that means that removing 1k means there won't be enought money.
        defaultWallet.remove(DEFAULT_MONEY);

        assertThrows(IllegalArgumentException.class, () -> {
            defaultRegister.checkOut(DEFAULT_ORDER, defaultWallet);
        });
    }

    @Test
    void moneyIsRemovedFromWalletDuringCheckoutTest() {

        defaultRegister.checkOut(DEFAULT_ORDER, defaultWallet);
        // compareTo(BigDecimal.ZERO) returns 0 if the contents of the wallet is 0 -
        // which is what we want in this case,
        // because the total cost of the order is supposed to be the same as the
        // contents of the wallet in USD.
        assertEquals(0, defaultWallet.getWalletContent().get(DEFAULT_CURRENCY).getAmount().compareTo(BigDecimal.ZERO));

    }

    @Test
    void nullOrderThrowsTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultRegister.checkOut(null, defaultWallet);
        });
    }

    @Test
    void nullWalletThrowsTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultRegister.checkOut(DEFAULT_ORDER, null);
        });
    }

    // import inventory? maybe something on order?

}
