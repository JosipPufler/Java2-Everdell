package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;

public class Castle extends Construct {

    public Castle() {
        super(new ResourceGroup(0, 2, 3, 3),
                CardType.PURPLE_PROSPERITY,
                Castle.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Castle.class.getSimpleName()),
                true,
                4,
                2);
    }

    @Override
    public int calculatePoints() {
        int count = (int) GameState.getPlayerState().cardsInPlay.stream().filter(x -> !x.isUnique() && x instanceof Construct).count();
        return count + super.calculatePoints();
    }
}
