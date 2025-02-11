package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.Destination;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.CardUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Chapel extends Construct implements Destination {
    int maxWorkerSlots = 1;
    private final List<PlayerNumber> workers = new ArrayList<>();
    boolean isOpen = false;

    @Getter
    int pointsPlaced = 0;

    public Chapel(){
        super(new ResourceGroup(0, 2, 1, 1),
                CardType.RED_DESTINATION,
                Chapel.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Chapel.class.getSimpleName()),
                true,
                2,
                2);
    }

    @Override
    public int getNumberOfWorkerSlots() {
        return maxWorkerSlots;
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
    public int calculatePoints() {
        return super.calculatePoints() + pointsPlaced;
    }

    @Override
    public Boolean place() {
        pointsPlaced++;
        List<Card> cards = GameState.getResourceManager().tryDrawFromMainDeck(pointsPlaced * 2);
        CardUtils.addCardsToHand(cards);
        workers.add(GameState.getPlayerState().getPlayerNumber());
        return true;
    }
}
