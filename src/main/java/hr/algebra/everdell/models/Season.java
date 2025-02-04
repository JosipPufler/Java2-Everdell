package hr.algebra.everdell.models;

public enum Season {
    WINTER,
    SPRING,
    SUMMER,
    AUTUMN;

    private static final Season[] vals = values();

    public Season next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
