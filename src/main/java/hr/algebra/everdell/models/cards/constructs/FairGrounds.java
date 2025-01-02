package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;
import hr.algebra.everdell.utils.ResourceManager;

import java.util.List;

public class FairGrounds extends Construct {
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
        List<Card> cards = GameState.getResourceManager().tryDrawFromMainDeck(3);
        GameUtils.addCardsToHand(cards);
        return super.play();
    }
}
