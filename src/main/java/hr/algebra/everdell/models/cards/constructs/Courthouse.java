package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Triggered;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Courthouse extends Construct implements Triggered {
    TriggerType triggerType;

    public Courthouse() {
        super(new ResourceGroup(0, 1, 1, 2),
                CardType.BLUE_GOVERNANCE,
                Courthouse.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Courthouse.class.getSimpleName()),
                true,
                2,
                2);
        triggerType = TriggerType.CONSTRUCT_AFTER;
    }

    @Override
    public TriggerType getTriggerType() {
        return triggerType;
    }

    @Override
    public void trigger() {
        List<String> options = new ArrayList<>(List.of("1 Twig", "1 Resin", "1 Pebble"));
        Optional<Integer> index = DialogUtils.showCustomListDialog(options, "Courthouse trigger");
        if (index.isPresent()) {
            switch (index.get()) {
                case 0:
                    GameState.getResourceManager().tryTakeTwigs(GameState.getPlayerState().resources, 1);
                    break;
                case 1:
                    GameState.getResourceManager().tryTakeResin(GameState.getPlayerState().resources, 1);
                    break;
                case 2:
                    GameState.getResourceManager().tryTakePebbles(GameState.getPlayerState().resources, 1);
                    break;
            }
            GameUtils.updatePlayer();
        }
    }
}
