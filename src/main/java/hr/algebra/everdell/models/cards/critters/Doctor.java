package hr.algebra.everdell.models.cards.critters;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.models.cards.constructs.University;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManager;
import javafx.scene.control.Alert;

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
    public void playEffect(PlayerState playerState, PlayerState opponentState, ResourceManager resourceManager) {
        DialogUtils.showDialog("How many", "Berries", Alert.AlertType.CONFIRMATION);
    }
}
