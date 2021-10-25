package se.su.dsv.RegisterSystem;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegisterTest {

    //Default customer who is 20 years old.
    final Customer DEFAULT_CUSTOMER = new Customer("Mr Customer", "Street Road", LocalDate.now().minusYears(20), "0707070700",
    "customer@test.com");

    //Customer 0 years old.
    final Customer YOUNG_CUSTOMER = new Customer("Young Customer", "Street street", LocalDate.now(), "0707070700", "young@test.com");

    final Currency DEFAULT_CURRENCY = Currency.USD;
    final Currency OTHER_CURRENCY = Currency.SEK;

    final Money DEFAULT_MONEY = new Money(new BigDecimal(1000.0), DEFAULT_CURRENCY);
    final Money OTHER_MONEY = new Money(new BigDecimal(1000.0), OTHER_CURRENCY);

    final Item DEFAULT_ITEM = new Item("Mjölk", "0123456789", "Arla", ItemType.GROCERY, DEFAULT_MONEY);
    final Item DEFAULT_ITEM2 = new Item("Tryffel", "9876543210", "FancyProducts", ItemType.GROCERY,
            DEFAULT_MONEY.add(DEFAULT_MONEY));

    final Item[] ITEMS = { DEFAULT_ITEM, DEFAULT_ITEM2 };
    final Order DEFAULT_ORDER = new Order(DEFAULT_CURRENCY, ITEMS);

    Register defaultRegister;
    Wallet defaultWallet;

    @BeforeAll
    static void setUp() {
        
    }

    @BeforeEach
    void initialize() throws IOException {
        defaultRegister = new Register(DEFAULT_CURRENCY);
        defaultWallet = new Wallet(DEFAULT_CUSTOMER, new MockBank());
        defaultRegister.getInventory().add(DEFAULT_ITEM);
        defaultRegister.getInventory().add(DEFAULT_ITEM);
        defaultRegister.getInventory().add(DEFAULT_ITEM2);
        // Makes sure there is as much money in money as there is in the order
        defaultWallet.add(DEFAULT_ORDER.getTotalGrossPrice());
    }

    // Tests whether constructor constructs as it is supposed to with valid params.
    @Test
    void constructorSetsCurrencyAndInventoryTest() {
        // makes sure the currency is set
        assertEquals(DEFAULT_CURRENCY, defaultRegister.getCurrency());
        // makes sure an inventory is created in constructor
        assertNotNull(defaultRegister.getInventory());
        // makes sure the currency is carried down into the inventory.
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

    //Tries to checkout with null order
    @Test
    void nullOrderThrowsTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultRegister.checkOut(null, defaultWallet);
        });
    }

    //Tries to checkout with null wallet
    @Test
    void nullWalletThrowsTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            defaultRegister.checkOut(DEFAULT_ORDER, null);
        });
    }

    //Tries to buy objects not in inventory
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

    //Sees whether bought items are removed from the inventory
    @Test
    void itemsRemovedFromInventoryWhenBoughtInRegisterTest() throws IOException {
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

    //Tries to buy items without enough money in wallet.
    @Test
    void walletNotEnoughMoneyThrowsTest() throws IOException {
        // There is as much money in the wallet as there is in the order
        // - that means that removing 1k means there won't be enought money.
        defaultWallet.remove(DEFAULT_MONEY);

        assertThrows(IllegalArgumentException.class, () -> {
            defaultRegister.checkOut(DEFAULT_ORDER, defaultWallet);
        });
    }

    //Sees whether money is removed from wallet during checkout.
    @Test
    void moneyIsRemovedFromWalletDuringCheckoutTest() throws IOException {
        defaultWallet.add(DEFAULT_MONEY);

        defaultRegister.checkOut(DEFAULT_ORDER, defaultWallet);

        //More wallet already contains money since before this test (see the beforeEach-method)
        //Therefore, this tests sees whether what remains after checkout is as much as how
        //much more money was in the wallet than the cost of the order
        assertEquals(DEFAULT_MONEY, defaultWallet.getWalletContent().get(DEFAULT_CURRENCY));
    }

    // Test whether money in wallet is available in several currencies instead of only in separate ones
    // That is, if you have 100 in SEK, and 100 in USD, you can buy for whatever those add up to.
    @Test
    void walletMoneyInOtherCurrency() throws IOException {

        defaultWallet.remove(DEFAULT_MONEY);

        // 10 loops because MockBank uses a 1 to 10 conversion USD to SEK. Wouldn't work with bank api
        // So, removing 1000, in USD, and then adding 10000 in SEK should balance out +-0 in actual value.
        for (int i = 0; i < 10; i++) {
            defaultWallet.add(OTHER_MONEY);
        }

        defaultRegister.checkOut(DEFAULT_ORDER, defaultWallet);

        //Since wallet content is equals to cost of order, after checkout there should be 0 left in wallet.
        assertEquals(BigDecimal.valueOf(0), defaultWallet.totalValueInCurrency(DEFAULT_CURRENCY).getAmount());
        //assertEquals(BigDecimal.ZERO, defaultWallet.getTotalAmount(DEFAULT_CURRENCY));
        //Since wallet content is equals to cost of order, after checkout there should be 0 left in wallet. 
        assertEquals(BigDecimal.valueOf(0), defaultWallet.totalValueInCurrency(DEFAULT_CURRENCY).getAmount());
    }

    @Test
    void tooYoungCustomerForTobaccoTest() throws IOException {
        //creates wallet owned by customer under 18
        Wallet wallet = new Wallet(YOUNG_CUSTOMER, new MockBank());
        //Adds money to make sure customer has enough money
        wallet.add(new Money(new BigDecimal(100000), DEFAULT_CURRENCY));
        //Agerestricted item
        Item item = new Item("test", "012", "test", ItemType.TOBACCO, DEFAULT_MONEY);
        //Creates order containing agerestricted item
        Order order = new Order(DEFAULT_CURRENCY, item);

        assertThrows(IllegalArgumentException.class, () -> {
            defaultRegister.checkOut(order, wallet);
        });



    }
}
