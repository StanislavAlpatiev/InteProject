package se.su.dsv.checkout.mutual;

public interface Vat6 extends Vat{
    // For newspapers
    double VAT = 0.06;

    default double getVAT() {
        return VAT;
    }
}
