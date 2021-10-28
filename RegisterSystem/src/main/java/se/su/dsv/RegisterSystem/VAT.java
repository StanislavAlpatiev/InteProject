package se.su.dsv.RegisterSystem;

public enum VAT {

    ZERO(0.00),
    SIX(0.06),
    TWELVE(0.12),
    TWENTY_FIVE(0.25);


    public final double label;

    VAT(double label) {
        this.label = label;
    }

    public String toString() {
        return String.valueOf(label);
    }

}

