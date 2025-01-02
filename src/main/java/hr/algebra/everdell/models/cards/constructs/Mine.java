package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManager;

public class Mine extends Construct {
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
        GameState.getResourceManager().tryTakePebbles(GameState.getPlayerState().resources, 1);
        return super.play();
    }
}
