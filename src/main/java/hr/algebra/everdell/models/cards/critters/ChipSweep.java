package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.ResinRefinery;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.Optional;

public class ChipSweep extends Critter<ResinRefinery> {
    public ChipSweep() {
        super(
                new ResourceGroup(3, 0, 0, 0),
                CardType.GREEN_PRODUCTION,
                ChipSweep.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(ChipSweep.class.getSimpleName()),
                false,
                1,
                ResinRefinery.class,
                3
        );
    }

    @Override
    public boolean play() {
        Optional<Card> card = DialogUtils.showCardChooseDialog(GameState.getPlayerState().cardsInPlay.stream().filter(x -> x.getType() == CardType.GREEN_PRODUCTION).toList(), getName());
        if (card.isPresent()){
            boolean played = card.get().play();
            if (played)
                super.play();
            return false;
        }
        return false;
    }
}
