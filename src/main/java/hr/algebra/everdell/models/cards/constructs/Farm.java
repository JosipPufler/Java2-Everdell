package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.CardType;
import hr.algebra.everdell.models.Construct;
import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.models.ResourceGroup;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManager;

public class Farm extends Construct {
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
    public void playEffect(PlayerState playerState, PlayerState opponentState, ResourceManager resourceManager) {
        resourceManager.tryTakeBerries(playerState.resources, 1);
    }
}
