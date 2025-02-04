package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Farm;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import lombok.Setter;

import java.util.Optional;

@Setter
public class Husband extends Critter<Farm>  implements GreenProduction {
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
        Activate();
        return super.play();
    }

    public Boolean isPaired(){
        return paired;
    }

    @Override
    public Boolean takesSpace() {
        return !paired;
    }

    @Override
    public Boolean Activate() {
        Optional<Card> first = GameState.getPlayerState().cardsInPlay.stream().filter(x -> x instanceof Wife wife && !wife.isPaired()).findFirst();
        if (first.isPresent() && first.get() instanceof Wife wife && !paired){
            setPaired(true);
            wife.setPaired(true);
        }
        if (GameState.getPlayerState().cardsInPlay.stream().anyMatch(Farm.class::isInstance) && paired){
            Optional<ResourceGroup> resourceGroup = DialogUtils.showMultiResourceDialog(1, GameState.getResourceManager().getResourcePool(), "Choose resource");
            resourceGroup.ifPresent(group -> GameState.getResourceManager().tryTakeGroup(GameState.getPlayerState().resources, group));
        }
        return true;
    }
}
