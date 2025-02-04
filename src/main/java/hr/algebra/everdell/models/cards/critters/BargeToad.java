package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Farm;
import hr.algebra.everdell.models.cards.constructs.TwigBarge;
import hr.algebra.everdell.utils.FileUtils;

public class BargeToad extends Critter<TwigBarge> implements GreenProduction {
    public BargeToad() {
        super(
                new ResourceGroup(2, 0, 0, 0),
                CardType.GREEN_PRODUCTION,
                BargeToad.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(BargeToad.class.getSimpleName()),
                false,
                1,
                TwigBarge.class,
                3
        );
    }

    @Override
    public boolean play() {
        Activate();
        return super.play();
    }

    @Override
    public Boolean Activate() {
        int farmCount = 0;
        for (Card card : GameState.getPlayerState().cardsInPlay){
            if (card instanceof Farm){
                farmCount++;
            }
        }
        GameState.getResourceManager().tryTakeTwigs(GameState.getPlayerState().resources, farmCount*2);
        return true;
    }
}
