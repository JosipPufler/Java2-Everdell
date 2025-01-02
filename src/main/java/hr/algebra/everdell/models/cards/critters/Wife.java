package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Farm;
import hr.algebra.everdell.utils.FileUtils;

import java.util.Optional;

public class Wife extends Critter<Farm> {
    Boolean paired = false;

    public Wife() {
        super(new ResourceGroup(2, 0, 0, 0),
                CardType.PURPLE_PROSPERITY,
                Wife.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Wife.class.getSimpleName()),
                false,
                2,
                Farm.class,
                4);
    }

    @Override
    public int calculatePoints() {
        if (paired){
            return super.calculatePoints() + 3;
        }
        return super.calculatePoints();
    }

    @Override
    public boolean play() {
        Optional<Card> first = GameState.getPlayerState().cardsInPlay.stream().filter(x -> x instanceof Husband && !((Husband) x).isPaired()).findFirst();
        if (first.isPresent() && first.get() instanceof Husband) {
            setPaired(true);
            ((Husband) first.get()).setPaired(true);
        }
        return super.play();
    }

    public void setPaired(Boolean paired){
        this.paired = paired;
    }

    public Boolean isPaired(){
        return paired;
    }
}
