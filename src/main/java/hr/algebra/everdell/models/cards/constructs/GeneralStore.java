package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;

public class GeneralStore extends Construct implements GreenProduction {

    public GeneralStore() {
        super(new ResourceGroup(0, 0, 1, 1),
                CardType.GREEN_PRODUCTION,
                GeneralStore.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(GeneralStore.class.getSimpleName()),
                false,
                1,
                3);
    }

    @Override
    public boolean play() {
        Activate();
        return super.play();
    }


    @Override
    public Boolean Activate() {
        boolean hasFarm = false;
        for (Card card : GameState.getPlayerState().cardsInPlay){
            if (card instanceof Farm){
                hasFarm = true;
                break;
            }
        }
        if (hasFarm){
            GameState.getResourceManager().tryTakeBerries(GameState.getPlayerState().resources, 2);
        } else {
            GameState.getResourceManager().tryTakeBerries(GameState.getPlayerState().resources, 1);
        }
        return true;
    }
}
