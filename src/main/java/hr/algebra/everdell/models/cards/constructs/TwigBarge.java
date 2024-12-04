package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.CardType;
import hr.algebra.everdell.models.Construct;
import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.models.ResourceGroup;
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
    public void playEffect(PlayerState playerState, PlayerState opponentState, ResourceManager resourceManager) {
        resourceManager.tryTakeTwigs(playerState.resources, 2);
    }
}
