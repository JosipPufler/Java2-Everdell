package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.CardUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.List;

public class FairGrounds extends Construct implements GreenProduction {
    public FairGrounds() {
        super(
                new ResourceGroup(0, 1, 2, 1),
                CardType.GREEN_PRODUCTION,
                FairGrounds.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(FairGrounds.class.getSimpleName()),
                true,
                3,
                3);
    }

    @Override
    public boolean play() {
        Activate();
        return super.play();
    }

    @Override
    public Boolean Activate() {
        List<Card> cards = GameState.getResourceManager().tryDrawFromMainDeck(3);
        CardUtils.addCardsToHand(cards);
        return true;
    }
}
