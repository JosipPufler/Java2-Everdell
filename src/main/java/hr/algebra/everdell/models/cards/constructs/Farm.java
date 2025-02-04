package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;

public class Farm extends Construct implements GreenProduction {
    public Farm() {
        super(
                new ResourceGroup(0,
                        2,
                        1,
                        0),
                CardType.GREEN_PRODUCTION,
                Farm.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Farm.class.getSimpleName()),
                false,
                1,
                8
        );
    }

    @Override
    public boolean play() {
        Activate();
        return super.play();
    }

    @Override
    public Boolean Activate() {
        GameState.getResourceManager().tryTakeBerries(GameState.getPlayerState().resources, 1);
        return true;
    }
}
