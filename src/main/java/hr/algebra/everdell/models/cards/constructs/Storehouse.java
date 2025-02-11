package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Destination;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Storehouse extends Construct implements Destination {
    int workerSlots = 1;
    final List<PlayerNumber> workers = new ArrayList<>();
    Boolean isOpen = false;
    ResourceGroup stockpile = new ResourceGroup(0, 0, 0, 0);

    public Storehouse() {
        super(new ResourceGroup(0, 1, 1, 1),
                CardType.GREEN_PRODUCTION,
                Storehouse.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Storehouse.class.getSimpleName()),
                false,
                2,
                3);
    }

    @Override
    public boolean play() {
        List<String> options = new ArrayList<>(List.of("3 Twig", "2 Resin", "1 Pebble", "2 Berries"));
        Optional<Integer> index = DialogUtils.showCustomListDialog(options, "Store in the storehouse");
        if (index.isPresent()) {
            switch (index.get()) {
                case 0:
                    GameState.getResourceManager().tryTakeTwigs(stockpile, 3);
                    break;
                case 1:
                    GameState.getResourceManager().tryTakeResin(stockpile, 2);
                    break;
                case 2:
                    GameState.getResourceManager().tryTakePebbles(stockpile, 1);
                    break;
                case 3:
                    GameState.getResourceManager().tryTakeBerries(stockpile, 2);
                    break;
            }
            return super.play();
        }
        return false;
    }

    @Override
    public int getNumberOfWorkerSlots() {
        return workerSlots;
    }

    @Override
    public int getNumberOfFreeWorkerSlots() {
        return workerSlots - workers.size();
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
        return List.of();
    }

    @Override
    public Boolean place() {
        GameState.getPlayerState().resources.merge(stockpile);
        workers.add(GameState.getPlayerState().getPlayerNumber());
        return true;
    }
}
