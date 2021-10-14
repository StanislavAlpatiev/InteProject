package se.su.dsv.RegisterSystem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Wallet {
    private final Customer owner;
    private Map<Currency, Money> walletContent = new HashMap<Currency, Money>();

    public Wallet(Customer owner, Money... money) {
        this.owner = owner;
        if (Arrays.stream(money).count() > 0) {
            Arrays.stream(money).forEach(e -> {
                walletContent.put(e.getCurrency(), e);
            });
        }
    }

    public Customer getOwner() {
        return owner;
    }

    public Map<Currency, Money> getWalletContent() {
        return walletContent;
    }

    public void add(Money money) {
        if (!walletContent.containsKey(money.getCurrency())) {
            walletContent.put(money.getCurrency(), money);
        } else {
            walletContent.replace(money.getCurrency(), walletContent.get(money.getCurrency()).add(money));
        }
    }

    public void add(Money... money) {
        for(Money m : money) {
            add(m);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Wallet)) {
            return false;
        }
        Wallet otherWallet = (Wallet) o;
        return Objects.equals(owner, otherWallet.getOwner()) && Objects.equals(walletContent, otherWallet.getWalletContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner);
    }
}
