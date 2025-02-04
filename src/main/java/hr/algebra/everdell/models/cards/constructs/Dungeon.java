package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.Locked;
import hr.algebra.everdell.interfaces.Triggered;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;

import java.util.Optional;

public class Dungeon extends Construct implements Triggered, Locked {
    TriggerType triggerType;
    int dungeonSlotsUsed = 0;
    int dungeonSize = 1;
    int maxDungeonSize = 2;

    public Dungeon() {
        super(new ResourceGroup(0, 0, 1, 2),
                CardType.BLUE_GOVERNANCE,
                Dungeon.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Dungeon.class.getSimpleName()),
                true,
                0,
                2);
        triggerType = TriggerType.CARD_BEFORE;
    }

    @Override
    public TriggerType getTriggerType() {
        return triggerType;
    }

    @Override
    public void trigger() {
        if (dungeonSlotsUsed < dungeonSize) {
            Optional<Card> card = DialogUtils.showCardChooseDialog(GameState.getPlayerState().cardsInPlay, "Choose card to discard");
            if (card.isPresent()) {
                GameState.getPlayerState().cardsInPlay.remove(card.get());
                DialogUtils.showMultiResourceDialog(3, GameState.getResourceManager().getResourcePool(), "Choose resources");
                dungeonSlotsUsed++;
            }
        }
        GameUtils.updatePlayer();
    }

    @Override
    public void unlock() {
        dungeonSize = maxDungeonSize;
    }
}
