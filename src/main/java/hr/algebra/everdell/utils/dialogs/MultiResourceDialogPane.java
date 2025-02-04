package hr.algebra.everdell.utils.dialogs;

import hr.algebra.everdell.controllers.MultiResourceDialogController;
import hr.algebra.everdell.models.ResourceGroup;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;

import java.io.IOException;

public class MultiResourceDialogPane extends DialogPane {
    MultiResourceDialogController controller;

    public MultiResourceDialogPane(int maxResources, ResourceGroup referenceGroup) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/hr/algebra/everdell/MultiResourceDialog.fxml"));
        loader.setRoot(this);

        try {
            loader.load();
            controller = loader.getController();
            controller.setParameters(maxResources, referenceGroup);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResourceGroup getResourcePool(){
        return controller.getResourcePool();
    }
}