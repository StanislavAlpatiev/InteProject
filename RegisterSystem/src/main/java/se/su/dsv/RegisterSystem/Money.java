package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;
import java.util.Objects;

public class Money implements Comparable<Money> {

    private BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {

        if (currency == null) {
            throw new IllegalArgumentException("currency is null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount is negative");
        }

        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money add(Money money) {
        if (!money.getCurrency().equals(this.currency)) {
            throw new IllegalArgumentException("mismatching currencies!");
        }

        BigDecimal newAmount = amount.add(money.getAmount());
        return new Money(newAmount, this.currency);
    }

    public Money subtract(Money money) {
        if (!money.getCurrency().equals(this.currency)) {
            throw new IllegalArgumentException("mismatching currencies!");
        }

        BigDecimal newAmount = amount.subtract(money.getAmount());
        return new Money(newAmount, this.currency);
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Money)) {
            return false;
        }
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, amount);
    }

    @Override
    public int compareTo(Money other) {
        if (currency != other.currency) {
            return currency.compareTo(other.currency);
        }
        return amount.compareTo(other.amount);
    }

    public String toExport() {
        StringBuilder sb = new StringBuilder();
        sb.append(amount + "@" + currency);
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getAmount());
        sb.append(" ");
        sb.append(this.getCurrency());
        return sb.toString();
    }

}
