package hr.algebra.everdell.models;

import hr.algebra.everdell.interfaces.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GameActionTransferable implements Serializable {
    GameAction action;
    List<Card> cardsInPlay;
    List<Card> cardsInHand;
    ResourceGroup resourceGroup;
    int freeWorkers;
    ResourceGroup boardResourceGroup;
    int points;
    int deckSize;
}
