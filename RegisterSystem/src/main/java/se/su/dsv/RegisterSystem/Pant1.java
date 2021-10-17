package se.su.dsv.RegisterSystem;

import java.math.BigDecimal;

public interface Pant1 extends Pant{

    static final BigDecimal PANT = new BigDecimal(1);

    @Override
    default BigDecimal getPant(){ return PANT; }

}
