package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.Destination;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Palace;
import hr.algebra.everdell.utils.CardUtils;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Queen extends Critter<Palace> implements Destination {
    final List<PlayerNumber> workers = new ArrayList<>();
    int workerSlots = 1;
    Boolean isOpen = false;

    public Queen() {
        super(new ResourceGroup(5, 0, 0, 0),
                CardType.RED_DESTINATION,
                Queen.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Queen.class.getSimpleName()),
                true,
                4,
                Palace.class,
                2);
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
        List<Card> cards = new ArrayList<>(GameState.getPlayerState().cardsInHand);
        cards.addAll(GameState.getResourceManager().getMeadow());
        List<Card> options = cards.stream().filter(x -> x.calculatePoints() < 3).toList();
        if (!options.isEmpty()){
            Optional<Card> card = DialogUtils.showCardChooseDialog(cards, "Choose card to play");
            if (card.isPresent()){
                CardUtils.addCardToCity(card.get(), true);
                GameState.getPlayerState().playCard(card.get(), true);
                workers.add(GameState.getPlayerState().getPlayerNumber());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean play() {
        return super.play();
    }
}
