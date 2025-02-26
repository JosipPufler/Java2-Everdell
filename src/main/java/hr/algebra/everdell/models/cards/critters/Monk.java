package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.Monastery;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.Optional;

public class Monk extends Critter<Monastery> implements GreenProduction {
    public Monk() {
        super(new ResourceGroup(1, 0, 0, 0),
                CardType.GREEN_PRODUCTION,
                Monk.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Monk.class.getSimpleName()),
                true,
                0,
                Monastery.class,
                2);
    }

    @Override
    public boolean play() {
        if(Activate())
            return super.play();
        return false;
    }

    @Override
    public Boolean Activate() {
        int maxBerries = 2;
        if (GameState.getPlayerState().resources.getBerries() == 0)
            return true;
        if (GameState.getPlayerState().resources.getBerries() < maxBerries)
            maxBerries = GameState.getPlayerState().resources.getBerries();
        Optional<Integer> giftBerries = DialogUtils.showSingleResourceDialog(maxBerries, Resource.BERRIES, "Gift berries");
        if (giftBerries.isPresent()) {
            GameState.getPlayerState().resources.removeBerries(giftBerries.get());
            GameState.getOpponentState().resources.addBerries(giftBerries.get());
            GameState.getPlayerState().addPoints(giftBerries.get()*2);
            GameState.getPlayerState().cardsInPlay.stream().filter(x -> getAssociatedLocation().isInstance(x)).forEach(x -> (getAssociatedLocation().cast(x)).unlock());
        }
        return true;
    }
}
