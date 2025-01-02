package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Triggered;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;
import javafx.scene.control.Alert;

import java.util.Objects;
import java.util.Optional;

public class Crane extends Construct implements Triggered {
    TriggerType triggerType;

    public Crane() {
        super(new ResourceGroup(0, 0, 0, 1),
                CardType.BLUE_GOVERNANCE,
                Crane.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Crane.class.getSimpleName()),
                true,
                1,
                3);
        triggerType = TriggerType.CONSTRUCT_BEFORE;
    }

    @Override
    public TriggerType getTriggerType() {
        return triggerType;
    }

    @Override
    public void trigger() {
        Boolean result = DialogUtils.showConfirmDialog("Discard Crane?", "Discard crane to gain 3 resources?", Alert.AlertType.CONFIRMATION);
        if (result) {
            Optional<ResourceGroup> craneDiscardReward = DialogUtils.showMultiResourceDialog(3, GameState.getResourceManager().getResourcePool(), "Crane discard reward");
            if (craneDiscardReward.isPresent()) {
                GameState.getPlayerState().resources.merge(craneDiscardReward.get());
                Optional<Card> first = GameState.getPlayerState().cardsInPlay.stream().filter(x -> Objects.equals(x.getName(), getName())).findFirst();
                first.ifPresent(GameUtils::removeCardFromCity);
                GameUtils.updatePlayer();
            }
        }
        GameUtils.updatePlayer();
    }
}
