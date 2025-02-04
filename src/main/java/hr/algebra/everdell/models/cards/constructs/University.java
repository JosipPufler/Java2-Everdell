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

public class University extends Construct implements Destination {
    int workerSlots = 1;
    final List<PlayerNumber> workers = new ArrayList<>();
    boolean isOpen = false;


    public University() {
        super(
                new ResourceGroup(0,
                        0,
                        1,
                        2),
                CardType.RED_DESTINATION,
                University.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(University.class.getSimpleName()),
                true,
                3,
                2
        );
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
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public List<PlayerNumber> getWorkers() {
        return workers;
    }

    @Override
    public Boolean placeWorker() {
        PlayerState playerState = GameState.getPlayerState();
        Optional<Card> card = DialogUtils.showCardChooseDialog(playerState.cardsInPlay, getName());
        if (card.isPresent()){
            CardUtils.removeCardFromCity(card.get());
            GameState.getResourceManager().tryTakeGroup(playerState.resources, card.get().getCost());
        } else {
            return false;
        }
        Optional<ResourceGroup> resource = DialogUtils.showMultiResourceDialog(1, GameState.getResourceManager().getResourcePool(), getName());
        if (resource.isPresent()){
            GameState.getResourceManager().tryTakeGroup(playerState.resources, resource.get());
            playerState.addPoints(1);
            workers.add(playerState.getPlayerNumber());
            return true;
        }
        return false;
    }
}
