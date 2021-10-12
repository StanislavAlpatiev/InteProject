package se.su.dsv.RegisterSystem;

public interface Vat6 {
    double VAT = 0.06;

    default double getVAT() {
        return VAT;
    }
}
