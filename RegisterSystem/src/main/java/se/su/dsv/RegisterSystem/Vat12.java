package se.su.dsv.RegisterSystem;

public interface Vat12 {
    double VAT = 0.12;

    default double getVAT() {
        return VAT;
    }
}
