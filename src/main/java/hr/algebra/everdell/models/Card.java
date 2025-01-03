package hr.algebra.everdell.models;

import hr.algebra.everdell.utils.GameUtils;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

public abstract class Card implements Serializable {
    @Getter
    CardType type;
    @Getter
    ResourceGroup cost;
    @Getter
    String name;
    @Getter
    String imageFilePath;
    @Getter
    Boolean unique;
    int inherentPointValue;
    @Getter
    int numberInDeck;

    public Card(ResourceGroup cost, CardType type, String name, String imageFilePath, Boolean unique, Integer inherentPointValue, int numberInDeck) {
        this.cost = cost;
        this.type = type;
        this.name = name;
        this.imageFilePath = imageFilePath;
        this.unique = unique;
        this.inherentPointValue = inherentPointValue;
        this.numberInDeck = numberInDeck;
    }

    public Boolean isUnique() {
        return unique;
    }

    public Boolean takesSpace(){
        return true;
    }

    public int calculatePoints() {
        return inherentPointValue;
    }

    public boolean play(){
        GameUtils.addCardToCity(this, false);
        GameUtils.updatePlayer();
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Card other = (Card) obj;
        return Objects.equals(this.name, other.name);
    }
}
