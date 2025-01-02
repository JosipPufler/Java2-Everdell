package hr.algebra.everdell.utils.dialogs;

import hr.algebra.everdell.controllers.SingleResourceDialogController;
import hr.algebra.everdell.models.Card;
import hr.algebra.everdell.models.Resource;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.util.List;

public class SingleResourceDialogPane extends DialogPane {
    List<Card> cards;
    SingleResourceDialogController controller;

    public SingleResourceDialogPane(int maxResources, Resource resource) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/hr/algebra/everdell/SingleResourceDialog.fxml"));
        loader.setRoot(this);

        try {
            loader.load();
            controller = loader.getController();
            controller.setMaxResources(maxResources);
            controller.setResourceType(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getResource(){
        return controller.getResource();
    }
}
