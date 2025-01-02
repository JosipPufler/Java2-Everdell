package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.University;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManager;
import javafx.scene.control.Alert;

import java.util.Optional;

public class Doctor extends Critter<University> {

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
        Optional<Integer> count = DialogUtils.showSingleResourceDialog(3, Resource.BERRIES, getName());
        if (count.isPresent()) {
            GameState.getPlayerState().addPoints(count.get());
            return super.play();
        }else {
            return false;
        }
    }
}
