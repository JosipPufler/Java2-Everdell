package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.models.CardType;
import hr.algebra.everdell.models.Critter;
import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.models.ResourceGroup;
import hr.algebra.everdell.models.cards.constructs.Dungeon;
import hr.algebra.everdell.utils.FileUtils;

public class Ranger extends Critter<Dungeon> {
    public Ranger() {
        super(new ResourceGroup(2, 0, 0, 0),
                CardType.TAN_TRAVELER,
                Ranger.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Ranger.class.getSimpleName()),
                true,
                1,
                Dungeon.class,
                2);
    }

    @Override
    public boolean play() {
        throw new UnsupportedOperationException("Not supported yet.");
        //playerState.cardsInPlay.stream().filter(x -> x.getClass() == super.getAssociatedLocation() && x instanceof Locked).forEach(x -> ((Locked)x).unlock());
        //return super.play();
    }
}
