package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.Triggered;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Inn;
import hr.algebra.everdell.models.cards.constructs.Monastery;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;
import javafx.scene.control.Alert;

import java.util.Objects;
import java.util.Optional;

public class Innkeeper extends Critter<Inn> implements Triggered {
    TriggerType triggerType;

    public Innkeeper() {
        super(new ResourceGroup(1, 0, 0, 0),
                CardType.BLUE_GOVERNANCE,
                Innkeeper.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Innkeeper.class.getSimpleName()),
                true,
                1,
                Inn.class,
                3);
        triggerType = TriggerType.CRITTER_BEFORE;
    }

    @Override
    public TriggerType getTriggerType() {
        return triggerType;
    }

    @Override
    public void trigger() {
        Boolean result = DialogUtils.showConfirmDialog("Discard Innkeeper?", "Discard innkeeper to gain 3 berries?", Alert.AlertType.CONFIRMATION);
        if (result) {
            GameState.getResourceManager().tryTakeBerries(GameState.getPlayerState().resources, 3);
            Optional<Card> first = GameState.getPlayerState().cardsInPlay.stream().filter(x -> Objects.equals(x.getName(), getName())).findFirst();
            first.ifPresent(GameUtils::removeCardFromCity);
            GameUtils.updatePlayer();
        }
    }

    @Override
    public boolean play() {
        return super.play();
    }
}
