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
        assertDoesNotThrow(() -> {
            Wallet wallet = new Wallet(DEFAULT_OWNER, new Money(new BigDecimal(10), Currency.USD),
                    new Money(new BigDecimal(35), Currency.SEK), new Money(new BigDecimal(5), Currency.GBP),
                    new Money(new BigDecimal(20), Currency.EUR));
        });
    }

    void walletContentTest() {

        Map<Currency, Money> expectedWalletContent = new HashMap<>();
        expectedWalletContent.put(Currency.USD, DEFAULT_MONEY_USD);
        expectedWalletContent.put(Currency.SEK, DEFAULT_MONEY_SEK);
        expectedWalletContent.put(Currency.GBP, DEFAULT_MONEY_GBP);
        expectedWalletContent.put(Currency.EUR, DEFAULT_MONEY_EUR);

        assertEquals(expectedWalletContent, DEFAULT_WALLET.getWalletContent());

    }

}