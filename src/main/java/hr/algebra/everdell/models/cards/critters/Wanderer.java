package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Lookout;
import hr.algebra.everdell.utils.CardUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.List;

public class Wanderer extends Critter<Lookout> {
    public Wanderer() {
        super(new ResourceGroup(2, 0, 0, 0),
                CardType.TAN_TRAVELER,
                Wanderer.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Wanderer.class.getSimpleName()),
                false,
                1,
                Lookout.class,
                3);
    }

    @Override
    public Boolean takesSpace() {
        return false;
    }

    @Override
    public boolean play() {
        List<Card> cards = GameState.getResourceManager().tryDrawFromMainDeck(3);
        CardUtils.addCardsToHand(cards);
        return super.play();
    }
}
