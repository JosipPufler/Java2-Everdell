package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Farm;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.Optional;

public class Husband extends Critter<Farm> {
    Boolean paired = false;

    public Husband() {
        super(new ResourceGroup(3, 0, 0, 0),
                CardType.GREEN_PRODUCTION,
                Husband.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Husband.class.getSimpleName()),
                false,
                2,
                Farm.class,
                4);
    }

    @Override
    public boolean play() {
        Optional<Card> first = GameState.getPlayerState().cardsInPlay.stream().filter(x -> x instanceof Wife && !((Wife) x).isPaired()).findFirst();
        if (first.isPresent() && first.get() instanceof Wife && !paired){
            setPaired(true);
            ((Wife) first.get()).setPaired(true);

        }
        if (GameState.getPlayerState().cardsInPlay.stream().anyMatch(x -> x instanceof Farm) && paired){
            Optional<ResourceGroup> resourceGroup = DialogUtils.showMultiResourceDialog(1, GameState.getResourceManager().getResourcePool(), "Choose resource");
            resourceGroup.ifPresent(group -> GameState.getResourceManager().tryTakeGroup(GameState.getPlayerState().resources, group));
        }
        return super.play();
    }

    public void setPaired(Boolean paired){
        this.paired = paired;
    }

    public Boolean isPaired(){
        return paired;
    }

    @Override
    public Boolean takesSpace() {
        return !paired;
    }
}
