package hr.algebra.everdell.utils.dialogs;

import hr.algebra.everdell.controllers.MultiResourceDialogController;
import hr.algebra.everdell.models.Card;
import hr.algebra.everdell.models.ResourceGroup;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.util.List;

public class MultiResourceDialogPane extends DialogPane {
    List<Card> cards;
    MultiResourceDialogController controller;

    public MultiResourceDialogPane(int maxResources) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/hr/algebra/everdell/MultiResourceDialog.fxml"));
        loader.setRoot(this);

        try {
            loader.load();
            controller = loader.getController();
            controller.setMaxResources(maxResources);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResourceGroup getResourcePool(){
        return controller.getResourcePool();
    }
}