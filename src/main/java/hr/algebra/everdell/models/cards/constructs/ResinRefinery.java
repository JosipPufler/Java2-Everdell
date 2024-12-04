package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.CardType;
import hr.algebra.everdell.models.Construct;
import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.models.ResourceGroup;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManager;

public class ResinRefinery extends Construct {
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
    public void playEffect(PlayerState playerState, PlayerState opponentState, ResourceManager resourceManager) {

    }
}
