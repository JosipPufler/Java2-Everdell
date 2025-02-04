package hr.algebra.everdell.models;

import lombok.Getter;

@Getter
public abstract class Critter<T extends Construct> extends BaseCard {
    Class<T> associatedLocation;

    public Critter(ResourceGroup cost, CardType type, String name, String imageFilePath, Boolean unique, Integer inherentPointValue, Class<T> associatedLocation, int numberInDeck) {
        super(cost, type, name, imageFilePath, unique, inherentPointValue, numberInDeck);
        this.associatedLocation = associatedLocation;
    }

}
