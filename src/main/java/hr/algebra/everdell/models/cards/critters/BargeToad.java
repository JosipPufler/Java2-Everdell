package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.models.CardType;
import hr.algebra.everdell.models.Critter;
import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.models.ResourceGroup;
import hr.algebra.everdell.models.cards.constructs.TwigBarge;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManager;

public class BargeToad extends Critter<TwigBarge>  {
    public BargeToad() {
        super(
                new ResourceGroup(2, 0, 0, 0),
                CardType.GREEN_PRODUCTION,
                BargeToad.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(BargeToad.class.getSimpleName()),
                false,
                1,
                TwigBarge.class,
                3
        );
    }

    @Override
    public void playEffect(PlayerState playerState, PlayerState opponentState, ResourceManager resourceManager) {

    }
}
