package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.CardType;
import hr.algebra.everdell.models.Critter;
import hr.algebra.everdell.models.GameState;
import hr.algebra.everdell.models.ResourceGroup;
import hr.algebra.everdell.models.cards.constructs.Ruins;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.Optional;

public class Peddler extends Critter<Ruins> implements GreenProduction {
    public Peddler() {
        super(new ResourceGroup(2, 0, 0, 0),
                CardType.GREEN_PRODUCTION,
                Peddler.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Peddler.class.getSimpleName()),
                false,
                1,
                Ruins.class,
                3);
    }

    @Override
    public boolean play() {
        if (Activate())
            return super.play();
        return false;
    }

    @Override
    public Boolean Activate() {
        Optional<ResourceGroup> resourceGroup = DialogUtils.showMultiResourceDialog(2, GameState.getPlayerState().resources, "Pay to gain");
        if (resourceGroup.isPresent()) {
            GameState.getPlayerState().resources.subtract(resourceGroup.get());
            GameState.getResourceManager().deposit(resourceGroup.get());
            if (resourceGroup.get().sumAllResources() > 0){
                Optional<ResourceGroup> reward = DialogUtils.showMultiResourceDialog(resourceGroup.get().sumAllResources(), GameState.getResourceManager().getResourcePool(), "Reward");
                reward.ifPresent(_ -> GameState.getResourceManager().tryTakeGroup(GameState.getPlayerState().resources, reward.get()));
                return reward.isPresent();
            }
        }
        return false;
    }
}
