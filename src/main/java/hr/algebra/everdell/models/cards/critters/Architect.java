package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Crane;
import hr.algebra.everdell.utils.FileUtils;

public class Architect extends Critter<Crane>{
    public Architect() {
        super(new ResourceGroup(4, 0, 0,0 ),
                CardType.PURPLE_PROSPERITY,
                Architect.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Architect.class.getSimpleName()),
                true,
                2,
                Crane.class,
                2);
    }

    @Override
    public int calculatePoints() {
        int unspent = GameState.getPlayerState().resources.getResin() + GameState.getPlayerState().resources.getResin();
        if (unspent > 6)
            unspent = 6;
        return unspent + super.calculatePoints();
    }
}
