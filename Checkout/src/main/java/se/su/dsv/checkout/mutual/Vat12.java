package se.su.dsv.checkout.mutual;

public interface Vat12 extends Vat{
    // For groceries
    double VAT = 0.12;

    default double getVAT() {
        return VAT;
    }
}
