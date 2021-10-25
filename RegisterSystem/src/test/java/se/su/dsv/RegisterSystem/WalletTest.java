package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WalletTest {
    static final Customer DEFAULT_OWNER = new Customer("Erik Andersson", "Lingonvägen 17",
            LocalDate.of(2000, 10, 17), "0722371555", "erik.andresson@gmail.com");
    static final Money DEFAULT_MONEY_USD = new Money(new BigDecimal(10), Currency.USD);
    static final Money DEFAULT_MONEY_SEK = new Money(new BigDecimal(10), Currency.SEK);
    static final Money DEFAULT_MONEY_GBP = new Money(new BigDecimal(10), Currency.GBP);
    static final Money DEFAULT_MONEY_EUR = new Money(new BigDecimal(10), Currency.EUR);
    static final MockBank DEFAULT_BANK = new MockBank();
    static final Wallet DEFAULT_WALLET = new Wallet(DEFAULT_OWNER, DEFAULT_BANK, DEFAULT_MONEY_USD, DEFAULT_MONEY_SEK, DEFAULT_MONEY_GBP, DEFAULT_MONEY_EUR);


    //Test that the constructor sets values properly and that the get methods return the expected values
    @Test
    void constructorTest() {
        Wallet testWallet = new Wallet(DEFAULT_OWNER, DEFAULT_BANK, new Money(new BigDecimal(10), Currency.USD),
                new Money(new BigDecimal(10), Currency.SEK), new Money(new BigDecimal(10), Currency.GBP),
                new Money(new BigDecimal(10), Currency.EUR));

        assertEquals(DEFAULT_WALLET.getOwner(), testWallet.getOwner());
        assertEquals(DEFAULT_WALLET.getWalletContent(), testWallet.getWalletContent());
    }

    //Test that the wallet is empty when no money is added in constructor
    @Test
    void walletContentEmptyWhenConstructorTakesNoMoneyTest() {
        assertEquals(0, new Wallet(DEFAULT_OWNER, DEFAULT_BANK).getWalletContent().size());
    }

    //Add should be able to add several money objects
    //Add should sum money of same currency
    @Test
    void addingMoneyToEmptyWalletTest() {
        //Adding to wallet
        Wallet wallet = new Wallet(DEFAULT_OWNER, DEFAULT_BANK);
        wallet.add(DEFAULT_MONEY_USD);

        //Mock map with entry
        Map<Currency, Money> map = new HashMap<>();
        map.put(Currency.USD, DEFAULT_MONEY_USD);

        //Comparing map to wallets walletContent
        assertEquals(map, wallet.getWalletContent());
    }

    //Test if adding money to wallet of a currency that is already present sums their value
    @Test
    void addingMoneyOfSameCurrencyTest() {
        //Adding money of Existing Currency to wallet
        Wallet wallet = new Wallet(DEFAULT_OWNER, DEFAULT_BANK, DEFAULT_MONEY_USD, DEFAULT_MONEY_EUR);
        wallet.add(DEFAULT_MONEY_USD);
        //Mock map with entry
        HashMap<Currency, Money> map = new HashMap<>();
        map.put(Currency.USD, new Money(new BigDecimal(20), Currency.USD));
        map.put(Currency.EUR, new Money(new BigDecimal(20), Currency.EUR));
        //Comparing map to wallets walletContent
        assertEquals(map.get(Currency.USD), wallet.getWalletContent().get(Currency.USD));
    }


    //Test if remove money works when remove money of a currency that is present in wallet
    //Example: wallet contains USD, test if removing USD from wallet works
    @Test
    void removingMoneyOfSameCurrencyTest() throws IOException {
        // Creating wallet with 10 EUR and 10 USD
        Wallet wallet = new Wallet(DEFAULT_OWNER, DEFAULT_BANK, DEFAULT_MONEY_USD, DEFAULT_MONEY_EUR);

        //Comparison value constructed from total money in wallet in USD, then removing 10 from it.
        BigDecimal value = wallet.totalValueInCurrency(Currency.USD).getAmount();
        BigDecimal newValue = value.subtract(DEFAULT_MONEY_USD.getAmount());

        //Repeating the steps above but through Wallet.
        wallet.remove(DEFAULT_MONEY_USD);
        Money remaining = wallet.totalValueInCurrency(Currency.USD);

        //Comparing comparison value with what was left in the wallet.
        assertEquals(newValue, remaining.getAmount());

        //Testing whether USD mapping is gone since it became 0.
        assertNull(wallet.getWalletContent().get(Currency.USD));
    }

    //Test if removing several money objects of different currency works
    @Test
    void removingMoneyOfDifferentCurrenciesTest() throws IOException {
        // Creating wallet with 100 EUR and 30 USD and 90 GBP
        Wallet wallet = new Wallet(DEFAULT_OWNER, DEFAULT_BANK, new Money(new BigDecimal(30), Currency.USD),
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

    //USES MockBank where the exchange rate is 10 to 1 for SEK to USD
    //Method checks whether getTotalValueInCurrency return expected moneyObject
    @Test
    void getTotalValueInCurrencyTest() throws IOException {
        Wallet wallet = new Wallet(DEFAULT_OWNER, DEFAULT_BANK, new Money(new BigDecimal("100"), Currency.SEK), new Money(new BigDecimal("10"), Currency.USD));
        System.out.println(wallet.getWalletContent());
        Money money = wallet.totalValueInCurrency(DEFAULT_MONEY_USD.getCurrency());
        assertEquals(new BigDecimal("20.0"), money.getAmount());
    }

}