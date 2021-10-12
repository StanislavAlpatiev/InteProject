package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockBankTest {


    @Test
    void getRateFromAPIResultSuccess() {
        assertDoesNotThrow(() -> {MockBank.getRate(Currency.USD, Currency.SEK);});
    }
    
}