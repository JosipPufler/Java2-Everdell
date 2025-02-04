package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.Triggered;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.GeneralStore;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;

public class Shopkeeper extends Critter<GeneralStore> implements Triggered {
    TriggerType triggerType;

    public Shopkeeper() {
        super(new ResourceGroup(2, 0, 0, 0),
                CardType.BLUE_GOVERNANCE,
                Shopkeeper.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Shopkeeper.class.getSimpleName()),
                true,
                1,
                GeneralStore.class,
                3);
        triggerType = TriggerType.CRITTER_AFTER;
    }

    @Override
    public boolean play() {
        return super.play();
    }

    @Override
    public TriggerType getTriggerType() {
        return triggerType;
    }

    @Override
    public void trigger() {
        GameState.getResourceManager().tryTakeBerries(GameState.getPlayerState().resources, 1);
        GameUtils.updatePlayer();
    }
}
