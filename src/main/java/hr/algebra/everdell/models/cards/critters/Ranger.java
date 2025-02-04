package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.Locked;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Dungeon;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.List;
import java.util.Optional;

public class Ranger extends Critter<Dungeon> {
    public Ranger() {
        super(new ResourceGroup(2, 0, 0, 0),
                CardType.TAN_TRAVELER,
                Ranger.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Ranger.class.getSimpleName()),
                true,
                1,
                Dungeon.class,
                2);
    }

    @Override
    public boolean play() {
        GameState.getPlayerState().cardsInPlay.stream().filter(x -> x.getClass() == super.getAssociatedLocation() && x instanceof Locked).forEach(x -> ((Locked)x).unlock());
        List<Location> deployedLocations = Location.getLocations();
        Optional<Integer> chooseWorkerToReturn = DialogUtils.showCustomListDialog(deployedLocations, "Choose worker to return");
        if (chooseWorkerToReturn.isPresent()) {
            Location.getLocations().remove(deployedLocations.get(chooseWorkerToReturn.get()));
            GameState.getPlayerState().returnWorkers(1);
        }
        return super.play();
    }
}
