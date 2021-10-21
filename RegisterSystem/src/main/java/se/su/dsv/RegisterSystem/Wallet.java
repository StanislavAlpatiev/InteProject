package se.su.dsv.RegisterSystem;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

//TODO: Comments for each method!
public class Wallet {
    private final Customer owner;
    private Map<Currency, Money> walletContent = new HashMap<Currency, Money>();
    private BankService bank;

    public Wallet(Customer owner, BankService bank, Money... money) {
        this.owner = owner;
        this.bank = bank;
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
        return walletContent.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void add(Money money) {
        if (!walletContent.containsKey(money.getCurrency())) {
            walletContent.put(money.getCurrency(), money);
        } else {
            walletContent.replace(money.getCurrency(), walletContent.get(money.getCurrency()).add(money));
        }
    }

    public void add(Money... money) {
        for (Money m : money) {
            add(m);
        }
    }

    public void remove(Money money) {
        if (walletContent.containsKey(money.getCurrency())) {
            walletContent.replace(money.getCurrency(), walletContent.get(money.getCurrency()).subtract(money));
        }
    }

    public void remove(Money... money) {
        for (Money m : money) {
            remove(m);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
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

    public Money totalValueInCurrency(Currency currency) throws IOException {
        Money moneyOfCurrency = new Money(new BigDecimal("0"), currency);
        for (Map.Entry<Currency, Money> entry : walletContent.entrySet()) {
            System.out.println(entry.getValue());
            moneyOfCurrency = moneyOfCurrency.add(bank.exchange(entry.getValue(), currency));
            System.out.println(moneyOfCurrency);
        }
        return moneyOfCurrency;
    }
}
