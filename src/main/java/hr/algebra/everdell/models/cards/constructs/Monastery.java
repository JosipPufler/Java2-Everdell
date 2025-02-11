package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Destination;
import hr.algebra.everdell.interfaces.Locked;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.critters.Monk;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Monastery extends Construct implements Destination, Locked {
    int workerSlots = 1;
    int lockedSlots = 1;
    final List<PlayerNumber> workers = new ArrayList<>();
    Boolean isOpen = false;

    public Monastery() {
        super(new ResourceGroup(0, 1, 1, 1),
                CardType.RED_DESTINATION,
                Monastery.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Monastery.class.getSimpleName()),
                true,
                1,
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
        Optional<ResourceGroup> resourceGroup = DialogUtils.showMultiResourceDialog(2, GameState.getPlayerState().resources, "Gift resources");
        if (resourceGroup.isPresent()) {
            GameState.getPlayerState().resources.subtract(resourceGroup.get());
            GameState.getOpponentState().resources.merge(resourceGroup.get());
            GameState.getPlayerState().addPoints(4);
            workers.add(GameState.getPlayerState().getPlayerNumber());
            return true;
        }
        return false;
    }

    @Override
    public void unlock() {
        workerSlots += lockedSlots;
        lockedSlots = 0;
    }

    @Override
    public boolean play() {
        if (GameState.getPlayerState().cardsInPlay.stream().anyMatch(x -> x instanceof Monk)){
            unlock();
        }
        return super.play();
    }
}
