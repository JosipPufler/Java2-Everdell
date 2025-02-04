package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.University;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.Optional;

public class Doctor extends Critter<University> implements GreenProduction {

    public Doctor() {
        super(
                new ResourceGroup(4, 0, 0, 0),
                CardType.GREEN_PRODUCTION,
                Doctor.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Doctor.class.getSimpleName()),
                true,
                4,
                University.class,
                2);
    }

    @Override
    public boolean play() {
        if (Activate()){
            return super.play();
        }
        return false;
    }

    @Override
    public Boolean Activate() {
        Optional<Integer> count = DialogUtils.showSingleResourceDialog(3, Resource.BERRIES, super.getName());
        if (count.isPresent()) {
            GameState.getPlayerState().addPoints(count.get());
            return true;
        } else {
            return false;
        }
    }
}
