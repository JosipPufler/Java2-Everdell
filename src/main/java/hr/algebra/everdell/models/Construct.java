package hr.algebra.everdell.models;

public abstract class Construct extends Card{
    Boolean isOccupied;

    public Construct(ResourceGroup cost, CardType type, String name, String imageFilePath, Boolean unique, Integer inherentPointValue, int numberInDeck) {
        super(cost, type, name, imageFilePath, unique, inherentPointValue, numberInDeck);
        this.isOccupied = false;
    }

    public void setOccupied(Boolean occupied) {
        isOccupied = occupied;
    }

    public Boolean getOccupied() {
        return isOccupied;
    }
}
