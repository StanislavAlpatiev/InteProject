package se.su.dsv.checkout.mutual;

public interface Vat25 extends Vat {
    // For tobacco
    double VAT = 0.25;

    default double getVAT() {
        return VAT;
    }

}
