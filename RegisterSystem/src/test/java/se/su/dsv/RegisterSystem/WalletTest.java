package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {
    static final Customer DEFAULT_OWNER = new Customer("Erik Andersson", "Lingonvägen 17",
             LocalDate.of(2000, 10, 17), "0722371555", "erik.andresson@gmail.com");
    static final Money DEFAULT_MONEY_USD = new Money(new BigDecimal(10), Currency.USD);
    static final Money DEFAULT_MONEY_SEK = new Money(new BigDecimal(10), Currency.SEK);
    static final Money DEFAULT_MONEY_GBP = new Money(new BigDecimal(10), Currency.GBP);
    static final Money DEFAULT_MONEY_EUR = new Money(new BigDecimal(10), Currency.EUR);
    static final Wallet DEFAULT_WALLET = new Wallet(DEFAULT_OWNER, DEFAULT_MONEY_USD, DEFAULT_MONEY_SEK, DEFAULT_MONEY_GBP, DEFAULT_MONEY_EUR);

    @Test
    void constructorTest() {
        Wallet testWallet = new Wallet(DEFAULT_OWNER, new Money(new BigDecimal(10), Currency.USD),
                new Money(new BigDecimal(10), Currency.SEK), new Money(new BigDecimal(10), Currency.GBP),
                new Money(new BigDecimal(10), Currency.EUR));

        assertEquals(DEFAULT_WALLET.getOwner(), testWallet.getOwner());
        assertEquals(DEFAULT_WALLET.getWalletContent(), testWallet.getWalletContent());
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
        wallet.remove(DEFAULT_MONEY_USD);
        //Comparing map to wallets walletContent
        assertEquals(new Money(BigDecimal.ZERO, Currency.USD), wallet.getWalletContent().get(Currency.USD));
    }

    //Test if removing several money objects of different currency works
    @Test
    void removingMoneyOfDifferentCurrenciesTest() {
        // Creating wallet with 100 EUR and 30 USD and 90 GBP
        Wallet wallet = new Wallet(DEFAULT_OWNER, new Money(new BigDecimal(30), Currency.USD),
                new Money(new BigDecimal(100), Currency.EUR), new Money(new BigDecimal(90), Currency.GBP));

        //MockWalletContent with money expected money objects after removal of money
        HashMap<Currency, Money> mockWalletContent = new HashMap<>();
        mockWalletContent.put(Currency.USD, new Money(new BigDecimal(20), Currency.USD));
        mockWalletContent.put(Currency.EUR, new Money(new BigDecimal(50), Currency.EUR));
        mockWalletContent.put(Currency.GBP, new Money(new BigDecimal(70), Currency.GBP));

        //Removing 10 USD from 30 USD in Wallet
        //Removing 50 EUR from 100 EUR in Wallet
        //Removing 20 GBP from 90 GBP in Wallet
        wallet.remove(DEFAULT_MONEY_USD, new Money(new BigDecimal(50), Currency.EUR),
                new Money(new BigDecimal(20), Currency.GBP));

        //Comparing map to wallets walletContent
        assertEquals(mockWalletContent, wallet.getWalletContent());
    }


}