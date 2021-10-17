package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;

public interface Pant2 extends Pant{

    static final BigDecimal PANT = new BigDecimal(2);

    @Override
    default BigDecimal getPant(){ return PANT; }

}

