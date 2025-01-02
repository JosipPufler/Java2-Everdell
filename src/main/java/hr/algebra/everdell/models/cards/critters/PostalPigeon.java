package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.PostOffice;
import hr.algebra.everdell.models.cards.constructs.Ruins;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;

import javax.tools.Diagnostic;
import java.util.List;
import java.util.Optional;

public class PostalPigeon extends Critter<PostOffice> {
    public PostalPigeon() {
        super(new ResourceGroup(2, 0, 0, 0),
                CardType.TAN_TRAVELER,
                PostalPigeon.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(PostalPigeon.class.getSimpleName()),
                false,
                0,
                PostOffice.class,
                3);
    }

    @Override
    public boolean play() {
        List<Card> cards = GameState.getResourceManager().tryDrawFromMainDeck(2);
        if (cards.isEmpty() || (cards.stream().noneMatch(x -> x.calculatePoints() < 3))) {
            return false;
        }
        Optional<Card> card = DialogUtils.showCardChooseDialog(cards, "Choose card");
        if (card.isPresent()) {
            GameUtils.addCardToCity(card.get(), true);
            return super.play();
        }
        return false;
    }
}
