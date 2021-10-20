package se.su.dsv.RegisterSystem;

public enum VAT {

    SIX(0.06),
    TWELVE(0.12),
    TWENTY_FIVE(0.25);


    public double label;

    private VAT(double label) {
        this.label = label;
    }

    public static double valueOfLabel(double label) {
        for (VAT vat : values()) {
            if (vat.label == label)
                return label;
        }
        return 0;
    }

    public String toString() {
        return String.valueOf(label);
    }

}

