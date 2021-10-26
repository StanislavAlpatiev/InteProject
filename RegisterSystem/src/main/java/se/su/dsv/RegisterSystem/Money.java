package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

//TODO: comments for each method!
public class Money implements Comparable<Money> {

    private final Currency currency;
    private final BigDecimal amount;

    //sets amount and currency
    public Money(BigDecimal amount, Currency currency) {

        if (amount == null) {
            throw new IllegalArgumentException("amount is null");
        }

        if (currency == null) {
            throw new IllegalArgumentException("currency is null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount is negative");
        }

        this.amount = amount.setScale(2, RoundingMode.HALF_UP);;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    //adds an amount of the same currency
    public Money add(Money money) {
        if (!money.getCurrency().equals(this.currency)) {
            throw new IllegalArgumentException("mismatching currencies!");
        }

        BigDecimal newAmount = amount.add(money.getAmount()).setScale(2, RoundingMode.HALF_UP);
        return new Money(newAmount, this.currency);
    }

    //subtracts an amount of the same currency
    public Money subtract(Money money) {
        if (!money.getCurrency().equals(this.currency)) {
            throw new IllegalArgumentException("mismatching currencies!");
        }

        BigDecimal newAmount = amount.subtract(money.getAmount()).setScale(2, RoundingMode.HALF_UP);
        return new Money(newAmount, this.currency);
    }

    //returns true if amount and currency are the same
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Money)) {
            return false;
        }
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 && Objects.equals(currency, money.currency);
    }

    //generates hashcode
    @Override
    public int hashCode() {
        return Objects.hash(currency, amount);
    }

    //compares currency and amount
    @Override
    public int compareTo(Money other) {
        if (currency != other.currency) {
            return currency.compareTo(other.currency);
        }
        return amount.compareTo(other.amount);
    }

    //generates a string that is used in Item toString
    public String toExport() {
        return amount + "@" + currency;
    }

    //returns formatted string
    @Override
    public String toString() {
        return this.getAmount() +
                " " +
                this.getCurrency();
    }

}
