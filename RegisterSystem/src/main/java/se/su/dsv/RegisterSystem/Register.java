package se.su.dsv.RegisterSystem;

public class Register {

    private Currency currency;


    public Register(Currency currency){
        this.currency = currency;
    }

    public Currency getCurrency(){
        return currency;
    }
}
