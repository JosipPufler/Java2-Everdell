package hr.algebra.everdell.utils.dialogs;

import hr.algebra.everdell.controllers.CustomListDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.util.List;

public class CustomListDialogPane extends DialogPane {
    CustomListDialogController controller;

    public CustomListDialogPane(List<?> list) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/hr/algebra/everdell/CustomListDialog.fxml"));
        loader.setRoot(this);

        try {
            loader.load();
            controller = loader.getController();
            controller.setChoices(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getChoice(){
        return controller.getChoice();
    }
}
