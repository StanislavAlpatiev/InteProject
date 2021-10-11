package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;

public class Money{

    private BigDecimal amount;
    private Currency currency;


    public Money(BigDecimal amount, Currency currency){

        if(currency == null){
            throw new IllegalArgumentException("currency is null");
        }

        if(amount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("amount is negative");
        }

        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public Currency getCurrency(){
        return currency;
    }

    public Money add(Money money){
        if(!money.getCurrency().equals(this.currency)){
            throw new IllegalArgumentException("mismatching currencies!");
        }

        BigDecimal newAmount = amount.add(money.getAmount());
        return new Money(newAmount, this.currency);
    }

    public Money subtract(Money money){
        if(!money.getCurrency().equals(this.currency)){
            throw new IllegalArgumentException("mismatching currencies!");
        }

        BigDecimal newAmount = amount.subtract(money.getAmount());
        return new Money(newAmount, this.currency);
    }
}
