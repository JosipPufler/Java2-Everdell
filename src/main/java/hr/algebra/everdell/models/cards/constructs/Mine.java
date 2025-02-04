package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;

public class Mine extends Construct implements GreenProduction {
    public Mine(){
        super(
                new ResourceGroup(0,
                        1,
                        1,
                        1),
                CardType.GREEN_PRODUCTION,
                Mine.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Mine.class.getSimpleName()),
                false,
                2,
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
        GameState.getResourceManager().tryTakePebbles(GameState.getPlayerState().resources, 1);
        return true;
    }
}
