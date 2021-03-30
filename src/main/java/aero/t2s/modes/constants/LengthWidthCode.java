package aero.t2s.modes.constants;

public enum  LengthWidthCode {
    CAT0(15, 11.5),
    CAT1(15, 23),
    CAT2(25, 28.5),
    CAT3(25, 34),
    CAT4(35, 33),
    CAT5(35, 38),
    CAT6(45, 39.5),
    CAT7(45, 45),
    CAT8(55, 45),
    CAT9(55, 52),
    CAT10(65, 59.5),
    CAT11(65, 67),
    CAT12(75, 72.5),
    CAT13(75, 80),
    CAT14(85, 80),
    CAT15(85, 90),
    ;

    private final double length;
    private final double width;

    LengthWidthCode(double length, double width) {
        this.length = length;
        this.width = width;
    }

    public static LengthWidthCode from(int category) {
        if (category > values().length | category < 0) {
            return CAT15;
        }

        return values()[category];
    }

    public double getMaxWidth() {
        return width;
    }

    public double getMaxLength() {
        return length;
    }

    @Override
    public String toString() {
        return String.format(
            "%s (Length: %fM | Width: %fM)",
            name(),
            length,
            width
        );
    }
}
