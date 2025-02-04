package hr.algebra.everdell.models;

import hr.algebra.everdell.interfaces.Card;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

public abstract class BaseCard implements Card, Serializable {
    @Getter
    CardType type;
    @Getter
    ResourceGroup cost;
    @Getter
    String name;
    @Getter
    String imageFilePath;
    Boolean unique;
    int inherentPointValue;
    @Getter
    int numberInDeck;

    protected BaseCard(ResourceGroup cost, CardType type, String name, String imageFilePath, Boolean unique, Integer inherentPointValue, int numberInDeck) {
        this.cost = cost;
        this.type = type;
        this.name = name;
        this.imageFilePath = imageFilePath;
        this.unique = unique;
        this.inherentPointValue = inherentPointValue;
        this.numberInDeck = numberInDeck;
    }

    @Override
    public Boolean isUnique() {
        return unique;
    }

    @Override
    public int calculatePoints() {
        return inherentPointValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final BaseCard other = (BaseCard) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, cost, name, imageFilePath, unique, inherentPointValue, numberInDeck);
    }
}
