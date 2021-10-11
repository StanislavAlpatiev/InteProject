package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;

public class Money{

    private BigDecimal amount;
    private Currency currency;


    public Money(BigDecimal amount, Currency currency){

        if(currency == null){
            throw new IllegalArgumentException("currency is null");
        }
        if(amount.doubleValue() < new BigDecimal(0).doubleValue()){
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

}
