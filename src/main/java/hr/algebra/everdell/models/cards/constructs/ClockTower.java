package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Triggered;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;

import java.util.Optional;

public class    ClockTower extends Construct implements Triggered {
    int points;
    TriggerType triggerType;

    public ClockTower() {
        super(new ResourceGroup(0, 3, 0, 1),
                CardType.BLUE_GOVERNANCE,
                ClockTower.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(ClockTower.class.getSimpleName()),
                true,
                0,
                3);
        triggerType = TriggerType.SEASON_CHANGE;
    }

    @Override
    public int calculatePoints() {
        return super.calculatePoints() + points;
    }

    @Override
    public boolean play() {
        points = 3;
        return super.play();
    }

    @Override
    public TriggerType getTriggerType() {
        return triggerType;
    }

    @Override
    public void trigger() {
        Optional<Integer> index = DialogUtils.showCustomListDialog(GameState.getPlayerState().locationsDeployed, "Choose location");
        index.ifPresent(integer -> GameState.getPlayerState().locationsDeployed.get(integer).activate(GameState.getPlayerState(), true));
        GameUtils.updatePlayer();
    }
}
