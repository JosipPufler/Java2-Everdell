package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Chapel;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManager;

public class Shepherd extends Critter<Chapel> {

    public Shepherd() {
        super(new ResourceGroup(3, 0, 0, 0),
                CardType.TAN_TRAVELER,
                Bard.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Bard.class.getSimpleName()),
                true,
                1,
                Chapel.class,
                2);
    }

    @Override
    public boolean play() {
        GameState.getOpponentState().resources.addBerries(3);
        Chapel chapel = new Chapel();
        for (Card card : GameState.getPlayerState().cardsInPlay){
            if (card instanceof Chapel){
                chapel = (Chapel) card;
            }
        }
        GameState.getPlayerState().addPoints(chapel.getPointsPlaced());
        return super.play();
    }
}
