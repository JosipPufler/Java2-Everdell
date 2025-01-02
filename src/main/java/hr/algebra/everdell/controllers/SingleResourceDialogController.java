package hr.algebra.everdell.controllers;

import hr.algebra.everdell.models.Resource;
import hr.algebra.everdell.utils.ResourceManager;
import hr.algebra.everdell.utils.ResourceManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import lombok.Setter;

public class SingleResourceDialogController {
    @FXML
    public Spinner<Integer> spinner;

    @Setter
    Resource resourceType;

    @Setter
    int maxResources;

    final ResourceManager resourceManager = ResourceManagerFactory.getInstance();

    public void initialize() {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxResources));

        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue > maxResources) {
                spinner.getValueFactory().setValue(oldValue);
            }
        });
    }

    public int getResource(){
        return spinner.getValue();
    }
}
