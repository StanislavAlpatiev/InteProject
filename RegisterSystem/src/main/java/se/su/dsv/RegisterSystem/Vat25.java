package se.su.dsv.RegisterSystem;

public interface Vat25 extends Vat {
    double VAT = 0.25;

    default double getVAT() {
        return VAT;
    }
}
