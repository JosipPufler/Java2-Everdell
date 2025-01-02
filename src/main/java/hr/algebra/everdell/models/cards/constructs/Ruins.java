package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;

import java.util.List;
import java.util.Optional;

public class Ruins extends Construct {
    public Ruins(){
        super(new ResourceGroup(0, 0, 0, 0),
                CardType.TAN_TRAVELER,
                Ruins.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Ruins.class.getSimpleName()),
                false,
                0,
                3);
    }

    @Override
    public boolean play() {
        if (GameState.getPlayerState().cardsInPlay.stream().noneMatch(x -> x instanceof Construct))
            return false;
        Optional<Card> card = DialogUtils.showCardChooseDialog(GameState.getPlayerState().cardsInPlay.stream().filter(x -> x instanceof Construct).toList(), getName());
        if(card.isPresent()){
            GameUtils.removeCardFromCity(card.get());
            GameState.getResourceManager().tryTakeGroup(GameState.getPlayerState().resources, card.get().getCost());
            List<Card> cards = GameState.getResourceManager().tryDrawFromMainDeck(2);
            GameUtils.addCardsToHand(cards);
            return super.play();
        } else {
            return false;
        }
    }
}
