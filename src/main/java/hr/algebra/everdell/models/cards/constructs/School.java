package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;

public class School extends Construct {
    public School() {
        super(new ResourceGroup(0, 2, 2, 0),
                CardType.PURPLE_PROSPERITY,
                School.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(School.class.getSimpleName()),
                true,
                2,
                2);
    }

    @Override
    public int calculatePoints() {
        int count = (int) GameState.getPlayerState().cardsInPlay.stream().filter(x -> x instanceof Critter<?> && !x.isUnique()).count();
        return super.calculatePoints() + count;
    }

    @Override
    public boolean play() {
        return super.play();
    }
}
