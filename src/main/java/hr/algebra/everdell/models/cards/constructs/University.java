package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Destination;
import hr.algebra.everdell.models.CardType;
import hr.algebra.everdell.models.Construct;
import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.models.ResourceGroup;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class University extends Construct implements Destination {
    int workerSlots = 1;
    int workersDeployed = 0;
    final List<String> workers = new ArrayList<>();
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
    public void playEffect(PlayerState playerState, PlayerState opponentState, ResourceManager resourceManager) {
    }

    @Override
    public int getNumberOfWorkerSlots() {
        return workerSlots;
    }

    @Override
    public int getNumberOfFreeWorkerSlots() {
        return workerSlots - workersDeployed;
    }

    @Override
    public int getNumberOfDeployedWorkers() {
        return workersDeployed;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void clearWorkers(PlayerState playerState) {
        for (int i = workers.size()-1 ; i > 0 ; i--){
            if (workers.get(i).equals(PlayerState.playerName)){
                workers.remove(i);
            }
        }
    }

    @Override
    public void placeWorker(PlayerState playerState, PlayerState opponentState, ResourceManager resourceManager) {

    }
}
