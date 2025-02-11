package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.Destination;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.CardUtils;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostOffice extends Construct implements Destination {
    final List<PlayerNumber> workers = new ArrayList<>();
    int workerSlots = 1;
    Boolean isOpen = true;

    public PostOffice() {
        super(new ResourceGroup(0, 1, 2, 0),
                CardType.RED_DESTINATION,
                PostOffice.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(PostOffice.class.getSimpleName()),
                false,
                2,
                3);
    }

    @Override
    public boolean play() {
        return super.play();
    }

    @Override
    public int getNumberOfWorkerSlots() {
        return workerSlots;
    }

    @Override
    public int getNumberOfDeployedWorkers() {
        return workers.size();
    }

    @Override
    public Boolean isOpen() {
        return isOpen;
    }

    @Override
    public List<PlayerNumber> getWorkers() {
        return workers;
    }

    @Override
    public Boolean place() {
        PlayerState playerState = GameState.getPlayerState();
        CardUtils.addCardsToOpponentsHand(GameState.getResourceManager().tryDrawFromMainDeck(2));
        Optional<Integer> cardsToDiscard = DialogUtils.showSingleResourceDialog(playerState.cardsInHand.size(), Resource.CARD, "Cards to discard");
        if (cardsToDiscard.isPresent()) {
            for (int i = cardsToDiscard.get() - 1; i > 0; i--) {
                Optional<Card> card = DialogUtils.showCardChooseDialog(playerState.cardsInHand, "Card to discard");
                card.ifPresent(CardUtils::removeCardFromHand);
            }
            CardUtils.addCardsToHand(GameState.getResourceManager().tryDrawFromMainDeck(cardsToDiscard.get()));
            workers.add(GameState.getPlayerState().getPlayerNumber());
            return true;
        }
        return false;
    }
}
