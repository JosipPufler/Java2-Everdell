package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.CardType;
import hr.algebra.everdell.models.Construct;
import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.models.ResourceGroup;
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
    public void playEffect(PlayerState playerState, PlayerState opponentState, ResourceManager resourceManager) {
        resourceManager.tryTakePebbles(playerState.resources, 1);
    }
}
