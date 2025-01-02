package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManager;

public class TwigBarge extends Construct {
    public TwigBarge() {
        super(
                new ResourceGroup(0,
                        1,
                        0,
                        1),
                CardType.GREEN_PRODUCTION,
                TwigBarge.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(TwigBarge.class.getSimpleName()),
                false,
                1,
                3
        );
    }

    @Override
    public boolean play() {
        GameState.getResourceManager().tryTakeTwigs(GameState.getPlayerState().resources, 2);
        return super.play();
    }
}
