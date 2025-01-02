package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.Triggered;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.School;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;

import java.util.List;

public class Historian extends Critter<School> implements Triggered {
    TriggerType triggerType;

    public Historian() {
        super(new ResourceGroup(2, 0, 0, 0),
                CardType.BLUE_GOVERNANCE,
                Historian.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Historian.class.getSimpleName()),
                true,
                1,
                School.class,
                3);
        triggerType = TriggerType.CARD_AFTER;
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
        List<Card> cards = GameState.getResourceManager().tryDrawFromMainDeck(1);
        GameUtils.addCardsToHand(cards);
        GameUtils.updatePlayer();
    }
}
