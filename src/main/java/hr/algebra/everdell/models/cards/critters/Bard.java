package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Theater;
import hr.algebra.everdell.utils.CardUtils;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.Optional;

public class Bard extends Critter<Theater> {
    public Bard(){
        super(
                new ResourceGroup(3, 0, 0, 0),
                CardType.TAN_TRAVELER,
                Bard.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Bard.class.getSimpleName()),
                true,
                0,
                Theater.class,
                2
        );
    }

    @Override
    public boolean play() {
        for (int i = 0; i < 5; i++){
            Optional<Card> card = DialogUtils.showCardChooseDialog(GameState.getPlayerState().cardsInHand, getName());
            if (card.isPresent()){
                CardUtils.removeCardFromHand(card.get());
                GameState.getPlayerState().addPoints(1);
            }else {
                return super.play();
            }
        }
        return super.play();
    }
}
