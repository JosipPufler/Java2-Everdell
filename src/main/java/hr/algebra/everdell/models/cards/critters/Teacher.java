package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.School;
import hr.algebra.everdell.utils.CardUtils;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;

import java.util.List;
import java.util.Optional;

public class Teacher extends Critter<School> implements GreenProduction {

    public Teacher() {
        super(new ResourceGroup(2, 0, 0, 0),
                CardType.GREEN_PRODUCTION,
                Teacher.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Teacher.class.getSimpleName()),
                false,
                2,
                School.class,
                3);
    }

    @Override
    public boolean play() {
        if (Boolean.TRUE.equals(Activate()))
            return super.play();
        return false;
    }

    @Override
    public Boolean Activate() {
        List<Card> cards = GameState.getResourceManager().tryDrawFromMainDeck(2);
        Optional<Card> card = DialogUtils.showCardChooseDialog(cards, "Choose card to keep");
        if (card.isPresent()){
            cards.remove(card.get());
            CardUtils.addCardToOpponentsHand(cards.getFirst());
            CardUtils.addCardToHand(card.get());
            return true;
        }
        return false;
    }
}
