package se.su.dsv.RegisterSystem;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Wallet {
    private final Customer owner;
    private Map<Currency, Money> walletContent = new HashMap<Currency, Money>();
    private BankService bank;

    //Constructor takes owner bank and money as arguments
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

    //Add method adds money object to walletContent
    //If walletContent does not contain money of currency it adds a new entry
    //Else it replaces an entry with the currency of money with new value
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

    //Remove method subtracts money from entry in walletContent
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

    //Method takes currency as parameter and sums the value of all money objects in wallet content as per given currency
    //Return a new money object representing the amount of money in passed currency inside wallet
    public Money totalValueInCurrency(Currency currency) throws IOException {
        Money moneyOfCurrency = new Money(new BigDecimal("0"), currency);
        for (Map.Entry<Currency, Money> entry : walletContent.entrySet()) {
            moneyOfCurrency = moneyOfCurrency.add(bank.exchange(entry.getValue(), currency));
        }
        return moneyOfCurrency;
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

}
