package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.ResinRefinery;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.Optional;

public class ChipSweep extends Critter<ResinRefinery> implements GreenProduction {
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
        if (Activate())
            return super.play();
        return false;
    }

    @Override
    public Boolean Activate() {
        Optional<Card> card = DialogUtils.showCardChooseDialog(GameState.getPlayerState().cardsInPlay.stream().filter(x -> x.getType() == CardType.GREEN_PRODUCTION).toList(), getName());
        return card.map(Card::play).orElse(false);
    }
}
