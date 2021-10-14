package se.su.dsv.RegisterSystem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    private final Customer owner;
    private Map<Currency, Money> walletContent = new HashMap<Currency, Money>();

    public Wallet(Customer owner, Money... money) {
        this.owner = owner;
        if (Arrays.stream(money).count() > 0) {
            Arrays.stream(money).forEach(e -> {
                if(walletContent.containsKey(e.getCurrency())) {
                    walletContent.get(e.getCurrency()).add(e);
                } else {
                    walletContent.put(e.getCurrency(), e);
                }
            });
        }
    }
}
