package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManager;

public class Palace extends Construct {

    public Palace() {
        super(new ResourceGroup(0, 2, 3, 3),
                CardType.PURPLE_PROSPERITY,
                Palace.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Palace.class.getSimpleName()),
                true,
                4,
                2);
    }

    @Override
    public int calculatePoints() {
        int count = (int) GameState.getPlayerState().cardsInPlay.stream().filter(x -> x.isUnique() && x instanceof Construct).count();
        return count + super.calculatePoints();
    }

    @Override
    public boolean play() {
        return super.play();
    }
}
