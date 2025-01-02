package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManager;

public class Theater extends Construct {

    public Theater() {
        super(new ResourceGroup(0, 3, 1, 1),
                CardType.PURPLE_PROSPERITY,
                Theater.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Theater.class.getSimpleName()),
                true,
                3,
                2);
    }

    @Override
    public int calculatePoints() {
        int count = (int) GameState.getPlayerState().cardsInPlay.stream().filter(x -> !x.isUnique() && x instanceof Critter<?>).count();
        return count + super.calculatePoints();
    }

    @Override
    public boolean play() {
        return super.play();
    }
}
