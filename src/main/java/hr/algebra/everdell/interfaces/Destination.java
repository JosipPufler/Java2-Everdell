package hr.algebra.everdell.interfaces;

import hr.algebra.everdell.models.GameState;
import hr.algebra.everdell.models.PlayerNumber;
import hr.algebra.everdell.models.PlayerState;

import java.util.List;

public interface Destination {
    int getNumberOfWorkerSlots();

    default int getNumberOfFreeWorkerSlots() {
        return getNumberOfWorkerSlots() - getNumberOfDeployedWorkers();
    }

    int getNumberOfDeployedWorkers();
    boolean isOpen();
    List<PlayerNumber> getWorkers();

    default Boolean tryPlaceWorker() {
        if (getNumberOfFreeWorkerSlots() > 0){
            return placeWorker();
        }
        return false;
    }

    default int clearWorkers(PlayerState playerState) {
        int workersReturned = 0;
        for (int i = getWorkers().size()-1 ; i >= 0 ; i--){
            if (getWorkers().get(i).equals(playerState.getPlayerNumber())){
                getWorkers().remove(i);
                workersReturned++;
            }
        }
        GameState.getPlayerState().returnWorkersFromCards(workersReturned);
        return workersReturned;
    }

    Boolean placeWorker();
}
