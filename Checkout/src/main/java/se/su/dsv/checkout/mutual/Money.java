package se.su.dsv.checkout.mutual;

public class Money implements Comparable<Money> {

    private static final int MAJOR_UNIT = 100;

    private Currency currency;
    private long amount;

    public Money(Currency currency, long amount) {
        if (currency == null)
            throw new IllegalArgumentException("Currency can't be null");
        if (amount < 0)
            throw new IllegalArgumentException(
                    "Amount can't be less than zero");
        this.currency = currency;
        this.amount = amount;
    }

    public Money(Currency currency, long amountOfMajorUnit, long amountOfMinorUnit) {
        this(currency, amountOfMajorUnit * MAJOR_UNIT + amountOfMinorUnit);
    }

    public long getAmmountOfMajorUnit() {
        return amount / MAJOR_UNIT;
    }

    public long getAmmountOfMinorUnit() {
        return amount % MAJOR_UNIT;
    }

    public long getTotalAmountInMinorUnit() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money add(long amountOfMinorUnit) {
        if (amount == 0) {
            return this;
        }

        return new Money(currency, amount + amountOfMinorUnit);
    }

    public Money add(Money addend) {
        if (currency != addend.currency)
            throw new IllegalArgumentException(
                    "The currency needs to be the same in order to add");
        return add(addend.amount);
    }

    public Money subtract(long amountOfMinorUnit) {
        return new Money(currency, amount - amountOfMinorUnit);
    }

    public Money subtract(Money subtrahend) {
        if (currency != subtrahend.currency)
            throw new IllegalArgumentException(
                    "The currency needs to be the same in order to subtract");
        if (amount < subtrahend.amount)
            throw new IllegalArgumentException(
                    "You can't subtract more money than you have");
        return subtract(subtrahend.amount);
    }

    public int compareTo(Money other) {
        if (currency != other.currency)
            return currency.name.compareTo(other.currency.name);
        return (int) (amount - other.amount);
    }

    public boolean equals(Object other) {
        if (other instanceof Money) {
            Money m = (Money) other;
            return currency.equals(m.currency) && amount == m.amount;
        } else {
            return false;
        }
    }

    public String toString() {
        return String.format("%d:%d %s", getAmmountOfMajorUnit(),
                getAmmountOfMinorUnit(), getCurrency().symbol);
    }
}
