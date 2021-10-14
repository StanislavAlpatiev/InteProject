package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {
    static final Customer DEFAULT_OWNER = new Customer("Erik Andersson", "Lingonvägen 17",
            "19990325-5634", "0722371555", "erik.andresson@gmail.com");
    static final Money DEFAULT_MONEY_USD = new Money(new BigDecimal(10), Currency.USD);
    static final Money DEFAULT_MONEY_SEK = new Money(new BigDecimal(10), Currency.SEK);
    static final Money DEFAULT_MONEY_GBP = new Money(new BigDecimal(10), Currency.GBP);
    static final Money DEFAULT_MONEY_EUR = new Money(new BigDecimal(10), Currency.EUR);
    static final Wallet DEFAULT_WALLET = new Wallet(DEFAULT_OWNER, DEFAULT_MONEY_USD, DEFAULT_MONEY_SEK, DEFAULT_MONEY_GBP, DEFAULT_MONEY_EUR);

    @Test
    void constructorTest() {
        assertEquals(DEFAULT_WALLET, new Wallet(DEFAULT_OWNER, new Money(new BigDecimal(10), Currency.USD),
                new Money(new BigDecimal(10), Currency.SEK), new Money(new BigDecimal(10), Currency.GBP),
                new Money(new BigDecimal(10), Currency.EUR)));
    }

    //Test that the wallet is empty when no money is added in constructor
    @Test
    void walletContentEmptyWhenConstructorTakesNoMoneyTest() {
        assertEquals(0, new Wallet(DEFAULT_OWNER).getWalletContent().size());
    }

    //Add should be able to add several money objects
    //Add should sum money of same currency
    @Test
    void addingMoneyToEmptyWalletTest() {
        //Adding to wallet
        Wallet wallet = new Wallet(DEFAULT_OWNER);
        wallet.add(DEFAULT_MONEY_USD);

        //Mock map with entry
        Map<Currency, Money> map = new HashMap<>();
        map.put(Currency.USD, DEFAULT_MONEY_USD);

        //Comparing map to wallets walletContent
        assertEquals(map, wallet.getWalletContent());
    }

    @Test
    void addingMoneyOfSameCurrencyTest() {
        //Adding money of Existing Currency to wallet
        Wallet wallet = new Wallet(DEFAULT_OWNER, DEFAULT_MONEY_USD, DEFAULT_MONEY_EUR);
        wallet.add(DEFAULT_MONEY_USD);
        //Mock map with entry
        HashMap<Currency, Money> map = new HashMap<>();
        map.put(Currency.USD, new Money(new BigDecimal(20), Currency.USD));
        map.put(Currency.EUR, new Money(new BigDecimal(20), Currency.EUR));
        //Comparing map to wallets walletContent
        assertEquals(map.get(Currency.USD), wallet.getWalletContent().get(Currency.USD));
    }

    @Test
    void removingMoneyOfSameCurrencyTest() {
        // Creating wallet with 10 EUR and 10 USD
        Wallet wallet = new Wallet(DEFAULT_OWNER, DEFAULT_MONEY_USD, DEFAULT_MONEY_EUR);
        //Removing 10 USD
        wallet.getWalletContent().get(Currency.USD).remove(DEFAULT_MONEY_USD);
        //Comparing map to wallets walletContent
        assertEquals(new Money(BigDecimal.ZERO, Currency.USD), wallet.getWalletContent().get(Currency.USD));
    }


}