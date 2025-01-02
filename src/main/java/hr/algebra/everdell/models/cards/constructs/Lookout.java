package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Destination;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Lookout extends Construct implements Destination {
    int workerSlots = 1;
    final List<PlayerNumber> workers = new ArrayList<>();
    Boolean isOpen = false;

    public Lookout() {
        super(new ResourceGroup(0, 1, 1, 1),
                CardType.RED_DESTINATION,
                Lookout.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Lookout.class.getSimpleName()),
                true,
                2,
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
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public List<PlayerNumber> getWorkers() {
        return workers;
    }

    @Override
    public Boolean placeWorker() {
        List<Location> locations = Location.getLocations();
        Optional<Integer> index = DialogUtils.showCustomListDialog(locations.stream().map(Location::toShorthandString).toList(), "Choose location to copy");
        if (index.isEmpty())
            return false;
        locations.get(index.get()).activate(GameState.getPlayerState(), true);
        workers.add(GameState.getPlayerState().getPlayerNumber());
        return true;
    }
}
