package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;

public class EverTree extends Construct {

    public EverTree() {
        super(new ResourceGroup(0, 3, 3, 3),
                CardType.PURPLE_PROSPERITY,
                EverTree.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(EverTree.class.getSimpleName()),
                true,
                5,
                2);
    }

    @Override
    public int calculatePoints() {
        int count = (int) GameState.getPlayerState().cardsInPlay.stream().filter(x -> x.getType() == CardType.PURPLE_PROSPERITY).count();
        return count + super.calculatePoints();
    }
}
