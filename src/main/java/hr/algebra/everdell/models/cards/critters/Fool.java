package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.models.CardType;
import hr.algebra.everdell.models.Critter;
import hr.algebra.everdell.models.ResourceGroup;
import hr.algebra.everdell.models.cards.constructs.FairGrounds;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.GameUtils;

public class Fool extends Critter<FairGrounds> {
    public Fool() {
        super(new ResourceGroup(3, 0, 0, 0),
                CardType.TAN_TRAVELER,
                Fool.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Fool.class.getSimpleName()),
                true,
                -2,
                FairGrounds.class,
                2);
    }

    @Override
    public boolean play() {
        GameUtils.addCardToOpponentsCity(this);
        return true;
    }
}
