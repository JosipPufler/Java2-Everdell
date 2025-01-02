package hr.algebra.everdell.models;

public enum Season {
    Winter,
    Spring,
    Summer,
    Autumn;

    private static final Season[] vals = values();

    public Season next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
