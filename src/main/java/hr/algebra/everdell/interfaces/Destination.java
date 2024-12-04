package hr.algebra.everdell.interfaces;

import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.utils.ResourceManager;

public interface Destination {
    int getNumberOfWorkerSlots();
    int getNumberOfFreeWorkerSlots();
    int getNumberOfDeployedWorkers();
    boolean isOpen();

    void clearWorkers(PlayerState playerState);
    void placeWorker(PlayerState playerState, PlayerState opponentState, ResourceManager resourceManager);
}
