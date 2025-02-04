package hr.algebra.everdell.interfaces;

import hr.algebra.everdell.models.CardType;
import hr.algebra.everdell.models.ResourceGroup;
import hr.algebra.everdell.utils.CardUtils;
import hr.algebra.everdell.utils.GameUtils;

public interface Card {
    CardType getType();
    Boolean isUnique();
    ResourceGroup getCost();
    String getName();
    String getImageFilePath();
    int getNumberInDeck();
    default Boolean takesSpace(){
        return true;
    }
    int calculatePoints();
    default boolean play(){
        CardUtils.addCardToCity(this, false);
        GameUtils.updatePlayer();
        return true;
    }
}
