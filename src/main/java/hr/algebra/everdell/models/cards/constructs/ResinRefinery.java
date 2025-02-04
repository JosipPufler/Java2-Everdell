package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;

public class ResinRefinery extends Construct implements GreenProduction {
    public ResinRefinery() {
        super(
                new ResourceGroup(0,
                        0,
                        1,
                        1),
                CardType.GREEN_PRODUCTION,
                ResinRefinery.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(ResinRefinery.class.getSimpleName()),
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
        GameState.getResourceManager().tryTakeResin(GameState.getPlayerState().resources, 1);
        return true;
    }
}
